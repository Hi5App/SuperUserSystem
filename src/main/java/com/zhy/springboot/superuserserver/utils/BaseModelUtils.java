package com.zhy.springboot.superuserserver.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

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

    public static void loadModel(ModelType modelType){
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

    public static void unloadModel(ModelType modelType){
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

    public static String detectByModel(ModelType modelType, String url){
        switch(modelType){
            case Missing:{
                log.info("调用检测Missing的模型...");
                HttpClient httpClient = new HttpClient();
                PostMethod postMethod = new PostMethod(url);

                postMethod.addRequestHeader("accept", "*/*");
                //设置Content-Type，根据实际情况确定
                postMethod.addRequestHeader("Content-Type", "multipart/form-data");
                //必须设置下面这个Header
                //添加请求参数
                // Map paraMap = new HashMap();
                // paraMap.put("type", "wx");
                // paraMap.put("mchid", "10101");
                // postMethod.addParameter("consumerAppId", "test");
                // postMethod.addParameter("serviceName", "queryMerchantService");
                // postMethod.addParameter("params", JSON.toJSONString(paraMap));
                String result = "";
                try {
                    int code = httpClient.executeMethod(postMethod);
                    if (code == 200){
                        result = postMethod.getResponseBodyAsString();
                        log.info("detectbyModel_result:" + result);
                        return result;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
            case Crossing:{
                log.info("调用检测Crossing的模型...");
                HttpClient httpClient = new HttpClient();
                PostMethod postMethod = new PostMethod(url);

                postMethod.addRequestHeader("accept", "*/*");
                //设置Content-Type，根据实际情况确定
                postMethod.addRequestHeader("Content-Type", "multipart/form-data");
                //必须设置下面这个Header
                //添加请求参数
                // Map paraMap = new HashMap();
                // paraMap.put("type", "wx");
                // paraMap.put("mchid", "10101");
                // postMethod.addParameter("consumerAppId", "test");
                // postMethod.addParameter("serviceName", "queryMerchantService");
                // postMethod.addParameter("params", JSON.toJSONString(paraMap));
                String result = "";
                try {
                    int code = httpClient.executeMethod(postMethod);
                    if (code == 200){
                        result = postMethod.getResponseBodyAsString();
                        log.info("detectbyModel_result:" + result);
                        return result;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                break;
        }
        return "";
    }

}
