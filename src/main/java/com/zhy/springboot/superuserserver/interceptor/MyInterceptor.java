package com.zhy.springboot.superuserserver.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.bean.MissingInfo;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.controller.DetectController;
import com.zhy.springboot.superuserserver.utils.TaskInfo;
import com.zhy.springboot.superuserserver.utils.Utils;
import com.zhy.springboot.superuserserver.utils.XYZ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author zhy
 * @Date 2024/2/26 12:25
 * @Description 自定义拦截器
 * @Since version-1.0
 */
@Component
@Slf4j
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("\n-------- detect preHandle --- ");
        log.info("Start Time: " + System.currentTimeMillis());

        // // 使用 ContentCachingRequestWrapper 包装 HttpServletRequest 对象
        // ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        // // ContentCachingRequestWrapper cachingRequestWrapper = (ContentCachingRequestWrapper) request;
        // request = cachingRequestWrapper;

        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("\n-------- detect afterCompletion --- ");
        // // 从缓存中获取请求体的内容
        // ContentCachingRequestWrapper cachingRequestWrapper = (ContentCachingRequestWrapper) request;

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        log.info("End Time: " + endTime);
        log.info("Time Taken: " + (endTime - startTime));

        // String url = String.valueOf(request.getRequestURL());
        //
        // byte[] requestBody = request.getContentAsByteArray();
        // String requestBodyString = new String(requestBody);
        // System.out.println(requestBodyString);
        //
        // if(url.endsWith("missing")){
        //     JSONObject jsonObjectTemp = (JSONObject) JSONObject.parse(requestBodyString);
        //     String obj = jsonObjectTemp.getString("obj");
        //     String swcNameWithNoSuffix = jsonObjectTemp.getString("swcNameWithNoSuffix");
        //     JSONArray coorsArray = jsonObjectTemp.getJSONArray("coors");
        //     List<XYZ> coors = new ArrayList<>();
        //     for(int i=0; i<coorsArray.size(); i++){
        //         JSONObject coor = coorsArray.getJSONObject(i);
        //         coors.add(new XYZ(coor.getFloat("x"), coor.getFloat("y"), coor.getFloat("z")));
        //     }
        //
        //     TaskInfo taskInfo;
        //     if (detectController.mapForMissing.containsKey(swcNameWithNoSuffix)) {
        //         taskInfo = detectController.mapForMissing.get(swcNameWithNoSuffix);
        //     } else {
        //         return;
        //     }
        //     int[] patchSize = globalConfigs.getTipPatchSize();
        //     //生成切割后的swc文件
        //     generateCroppedSwcFile(obj, taskInfo, coors, patchSize);
        // }

        // String obj = (String) request.getAttribute("obj");
        // Object coors = request.getAttribute("coors");
        // if (!(coors instanceof List)) {
        //     return;
        // }
        // List<?> tempList = (List<?>) coors;
        // if(!(tempList.get(0) instanceof XYZ)){
        //     return;
        // }
        // List<XYZ> coorsList = (List<XYZ>) tempList;

    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }

}
