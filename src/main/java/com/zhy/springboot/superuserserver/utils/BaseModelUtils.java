package com.zhy.springboot.superuserserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public abstract void preProcess(TaskInfo taskInfo, String obj, String objRelaventPath, List<XYZ> coors, int[] patchSize);
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
