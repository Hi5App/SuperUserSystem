package com.zhy.springboot.superuserserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.utils.BaseModelUtils;
import com.zhy.springboot.superuserserver.utils.Utils;
import com.zhy.springboot.superuserserver.utils.XYZ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhy
 * @Date 2023/5/10 17:12
 * @Description This is description of class
 * @Since version-1.0
 */
@Slf4j
@Service
public class DetectService {
    @Autowired
    GlobalConfigs globalConfigs;

    @Autowired
    Utils utils;

    @Autowired
    BaseModelUtils baseModelUtils;

    public JSONArray detectCrossing(String swcPath, String baseDir, String obj, String res, List<XYZ> coors){
        log.info("enter detectCrossing...");
        utils.preProcess(baseDir, obj, res, coors);

        baseModelUtils.loadModel(BaseModelUtils.ModelType.Crossing);

        Map<String, Object> mapStr = new HashMap<String,Object>();
        mapStr.put("swc_path", swcPath);
        mapStr.put("input", baseDir);
        Map<String, Object> inputMap=new HashMap<String, Object>();
        inputMap.put("input", mapStr);
        String json= JSON.toJSONString(inputMap);
        JSONArray result=baseModelUtils.detectByModel(BaseModelUtils.ModelType.Crossing,globalConfigs.getUrlForCrossingModel(), json);

        baseModelUtils.unloadModel(BaseModelUtils.ModelType.Crossing);

        utils.postProcess(swcPath, baseDir);

        return result;
    }

    public JSONArray detectMissing(String swcPath, String baseDir, String obj, String res, List<XYZ> coors){
        log.info("enter detectMissing...");
        utils.preProcess(baseDir, obj, res, coors);

        baseModelUtils.loadModel(BaseModelUtils.ModelType.Missing);

        Map<String, Object> mapStr = new HashMap<String,Object>();
        mapStr.put("swc_path", swcPath);
        mapStr.put("input", baseDir);
        Map<String, Object> inputMap=new HashMap<String, Object>();
        inputMap.put("input", mapStr);
        String json= JSON.toJSONString(inputMap);
        JSONArray result=baseModelUtils.detectByModel(BaseModelUtils.ModelType.Missing,globalConfigs.getUrlForMissingModel(), json);

        baseModelUtils.unloadModel(BaseModelUtils.ModelType.Missing);

        utils.postProcess(swcPath, baseDir);

        return result;
        // return null;
    }
}
