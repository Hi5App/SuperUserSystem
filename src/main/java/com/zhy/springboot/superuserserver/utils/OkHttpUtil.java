package com.zhy.springboot.superuserserver.utils;

import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author zhy
 * @Date 2023/5/10 12:34
 * @Description Http请求发送helper
 * @Since version-1.0
 */
@Slf4j
@Component
public class OkHttpUtil {

    private static final MediaType JSON= MediaType.get("application/json; charset=utf-8");

    @Resource
    private OkHttpClient okHttpClient;

    /**
     * post
     *
     * @param url  请求的url
     * @param json post请求提交的参数
     * @return
     */
    public byte[] postForFile(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            int status = response.code();
            if (status == 200) {
                return response.body().bytes();
            } else {
                log.error("postForFile: " + status);
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null ;
    }

    public String post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            } else {
                log.error("post: " + status);
                log.error(response.body().string());
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null ;
    }
}
