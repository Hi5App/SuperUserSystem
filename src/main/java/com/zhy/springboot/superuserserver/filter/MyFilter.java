package com.zhy.springboot.superuserserver.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.controller.DetectController;
import com.zhy.springboot.superuserserver.utils.TaskInfo;
import com.zhy.springboot.superuserserver.utils.Utils;
import com.zhy.springboot.superuserserver.utils.XYZ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhy
 * @Date 2024/2/26 19:18
 * @Description This is description of class
 * @Since version-1.0
 */
@Slf4j
public class MyFilter extends HttpFilter {
    private static final long serialVersionUID = 8991118181953196532L;
    @Autowired
    DetectController detectController;
    @Autowired
    GlobalConfigs globalConfigs;
    @Autowired
    Utils utils;
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = String.valueOf(request.getRequestURL());
        if(!url.endsWith("/detect/nouse")){
            chain.doFilter(request, response);
        }else{
            // Wrapper 封装 Request 和 Response
            ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
            // 继续执行请求链
            chain.doFilter(cachingRequest, response);

            // 请求体
            byte[] requestBody = cachingRequest.getContentAsByteArray();

            System.out.println(url);
            String requestBodyString = new String(requestBody);
            // System.out.println(requestBodyString);

            if(url.endsWith("missing")) {
                JSONObject jsonObjectTemp = (JSONObject) JSONObject.parse(requestBodyString);
                String obj = jsonObjectTemp.getString("obj");
                String swcNameWithNoSuffix = jsonObjectTemp.getString("swcNameWithNoSuffix");
                JSONArray coorsArray = jsonObjectTemp.getJSONArray("coors");
                List<XYZ> coors = new ArrayList<>();
                for (int i = 0; i < coorsArray.size(); i++) {
                    JSONObject coor = coorsArray.getJSONObject(i);
                    coors.add(new XYZ(coor.getFloat("x"), coor.getFloat("y"), coor.getFloat("z")));
                }

                TaskInfo taskInfo;
                if (detectController.mapForMissing.containsKey(swcNameWithNoSuffix)) {
                    taskInfo = detectController.mapForMissing.get(swcNameWithNoSuffix);
                } else {
                    return;
                }
                int[] patchSize = globalConfigs.getTipPatchSize();
                //生成切割后的swc文件
                generateCroppedSwcFile(obj, taskInfo, coors, patchSize);
            }
        }
    }

    private void generateCroppedSwcFile(String obj, TaskInfo taskInfo, List<XYZ> coors, int[] patchSize) {
        //get username and password
        String username = globalConfigs.getUsername();
        String password = globalConfigs.getPassword();

        //get res
        List<XYZ> resList = null;
        if (utils.getResMap().containsKey(obj)) {
            resList = utils.getResMap().get(obj);
        } else {
            List<String> maxResAndSubMaxRes = utils.getMaxResAndSubMaxRes(obj, username, password);
            resList = utils.transResString2XYZ(maxResAndSubMaxRes);
            utils.getResMap().put(obj, resList);
        }

        XYZ imageMaxRes = resList.get(0);
        XYZ imageCurRes = resList.get(1);

        //获取切割swc所需参数
        File swcFile = new File(taskInfo.getSwcPath());
        String swcName = swcFile.getName();
        int dotIndex = swcName.lastIndexOf(".");
        if (dotIndex > 0) {
            swcName = swcName.substring(0, dotIndex);
        }
        String resForCropSwc = String.join(File.separator, "/tempswc", obj);

        for (XYZ coor : coors) {
            //坐标转换
            XYZ convertedCoor = utils.convertMaxRes2CurrResCoords(imageMaxRes, imageCurRes, coor.x, coor.y, coor.z);

            String fileName = (int) convertedCoor.x + "_" + (int) convertedCoor.y + "_" + (int) convertedCoor.z;
            String dirPath = String.join(File.separator, taskInfo.getBaseDirPath(), fileName);

            File eachDir = new File(dirPath);
            if (!eachDir.isDirectory()) {
                continue;
            }

            XYZ pa1 = new XYZ((int) convertedCoor.x - patchSize[0] / 2, (int) convertedCoor.y - patchSize[0] / 2, (int) convertedCoor.z - patchSize[0] / 2);
            XYZ pa2 = new XYZ((int) convertedCoor.x + patchSize[0] / 2, (int) convertedCoor.y + patchSize[0] / 2, (int) convertedCoor.z + patchSize[0] / 2);
            //获取切割后的swc
            utils.getCroppedSwc(pa1, pa2, swcName, resForCropSwc, username, password, dirPath);
        }
    }
}