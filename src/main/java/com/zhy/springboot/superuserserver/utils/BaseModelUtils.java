package com.zhy.springboot.superuserserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhy
 * @Date 2023/5/10 11:58
 * @Description 模型相关的工具类
 * @Since version-1.0
 */
@Slf4j
@Component
public class BaseModelUtils {
    /**
     * @Author zhy
     * @Date 2023/5/10 12:12
     * @Description 模型类型枚举
     * @Since version-1.0
     */
    public enum ModelType{
        /**
         * 检测Crossing
         */
        Crossing,
        /**
         * 检测Missing
         */
        Missing
    }
    @Resource
    private OkHttpUtil okHttpUtil;

    public void loadModel(ModelType modelType){
        switch(modelType){
            case Missing:{
                log.info("加载检测Missing的模型...");
                break;
            }
            case Crossing:{
                log.info("加载检测Crossing的模型...");
                break;
            }
            default:
                break;
        }
    }

    public void unloadModel(ModelType modelType){
        switch(modelType){
            case Missing:{
                log.info("卸载检测Missing的模型...");
                break;
            }
            case Crossing:{
                log.info("卸载检测Crossing的模型...");
                break;
            }
            default:
                break;
        }
    }

    public JSONArray detectByModel(ModelType modelType, String url, String json){
        switch(modelType){
            case Missing:{
                log.info("调用检测Missing的模型...");
                String result=okHttpUtil.postForJsonString(url, json);
                if(result==null){
                    log.info("调用模型失败！");
                    return null;
                }
                System.out.println(result);
                JSONObject jsonObjectTemp = (JSONObject) JSONObject.parse(result);
                JSONArray jsonResultArrayTemp = (JSONArray) jsonObjectTemp.get("output");
                return jsonResultArrayTemp;
            }
            case Crossing:{
                log.info("调用检测Crossing的模型...");
                String result=okHttpUtil.postForJsonString(url, json);
                if(result==null){
                    log.info("调用模型失败！");
                    return null;
                }
                JSONObject jsonObjectTemp = (JSONObject) JSONObject.parse(result);
                JSONArray jsonResultArrayTemp = (JSONArray) jsonObjectTemp.get("output");
                return jsonResultArrayTemp;
            }
            default:
                break;
        }
        return null;
    }

}
