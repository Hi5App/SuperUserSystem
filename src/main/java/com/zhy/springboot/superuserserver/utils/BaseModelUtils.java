package com.zhy.springboot.superuserserver.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.bean.entity.TaskInfo;
import com.zhy.springboot.superuserserver.bean.entity.XYZ;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zhy
 * @Date 2023/5/10 11:58
 * @Description 模型相关的工具类基类
 * @Since version-1.0
 */
@Slf4j
public abstract class BaseModelUtils {
    /**
     * @Author zhy
     * @Date 2023/5/10 12:12
     * @Description 模型类型枚举
     * @Since version-1.0
     */
    public enum ModelType {
        Crossing,
        Missing,
        Branching
    }

    public ModelType modelType;

    @Autowired
    public GlobalConfigs globalConfigs;

    @Resource
    public OkHttpUtil okHttpUtil;

    @Autowired
    public Utils utils;

    public abstract void loadModel() ;
    public abstract void unloadModel() ;
    public abstract void postProcess(String swcPath, String baseDir);
    public JSONArray detectByModel(String url, String json) {
        String result = okHttpUtil.postForJsonString(url, json);
        if (result == null) {
            log.info("调用模型失败！");
            return null;
        }
        JSONObject jsonObjectTemp = (JSONObject) JSONObject.parse(result);
        // return null;
        return (JSONArray) jsonObjectTemp.get("output");
    }

}
