package com.zhy.springboot.superuserserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    MissingModelUtils missingModelUtils;

    @Autowired
    CrossingModelUtils crossingModelUtils;

    @Autowired
    BranchingModelUtils branchingModelUtils;

    public JSONArray detectMissing(TaskInfo taskInfo, String obj, List<XYZ> coors) {
        log.info("enter detectMissing...");
        missingModelUtils.preProcess(taskInfo, obj, coors, globalConfigs.getTipPatchSize());

        missingModelUtils.loadModel();

        String json = handleInputForMissing(taskInfo.getSwcPath(), taskInfo.getBaseDirPath(), obj);
        JSONArray result = missingModelUtils.detectByModel(globalConfigs.getUrlForMissingModel(), json);

        missingModelUtils.unloadModel();

        // utils.postProcess(swcPath, baseDir);

        return result;
        // return null;
    }

    public JSONArray detectCrossing(TaskInfo taskInfo, String obj, List<List<PointInfo>> infos) {
        log.info("enter detectCrossing...");
        List<XYZ> coors = new ArrayList<>();
        for (List<PointInfo> info : infos) {
            XYZ tmp = new XYZ(info.get(0).getX(), info.get(0).getY(), info.get(0).getZ());
            coors.add(tmp);
        }
        crossingModelUtils.preProcess(taskInfo, obj, coors, globalConfigs.getCrossingPatchSize());

        crossingModelUtils.loadModel();

        String json = handleInputForCrossing(infos, taskInfo.getSwcPath(), taskInfo.getBaseDirPath(), obj);
        JSONArray result = crossingModelUtils.detectByModel(globalConfigs.getUrlForCrossingModel(), json);

        crossingModelUtils.unloadModel();

        // utils.postProcess(swcPath, baseDir);

        return result;
    }

    public JSONArray detectBranching(TaskInfo taskInfo, String obj, List<XYZ> coors){
        log.info("enter detectBranching...");
        branchingModelUtils.preProcess(taskInfo, obj, coors, globalConfigs.getBranchingPatchSize());

        branchingModelUtils.loadModel();

        String json = handleInputForBranching(taskInfo.getBaseDirPath(), obj);
        JSONArray result = branchingModelUtils.detectByModel(globalConfigs.getUrlForBranchingModel(), json);

        branchingModelUtils.unloadModel();

        // utils.postProcess(swcPath, baseDir);

        return result;
    }

    public String handleInputForMissing(String swcPath, String baseDir, String obj) {
        Map<String, Object> mapStr = new HashMap<String, Object>();
        mapStr.put("swc_path", swcPath);
        mapStr.put("input_path", baseDir);
        // mapStr.put("x_ratio", resList.get(0).x / resList.get(1).x);
        // mapStr.put("y_ratio", resList.get(0).y / resList.get(1).y);
        // mapStr.put("z_ratio", resList.get(0).z / resList.get(1).z);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("input", mapStr);
        return JSON.toJSONString(inputMap);
    }

    public String handleInputForBranching(String baseDir, String obj) {
        Map<String, Object> mapStr = new HashMap<String, Object>();
        mapStr.put("input_path", baseDir);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("input", mapStr);
        return JSON.toJSONString(inputMap);
    }

    public String handleInputForCrossing(List<List<PointInfo>> infos, String swcPath, String baseDir, String obj) {
        //get res
        String username = globalConfigs.getUsername();
        String password = globalConfigs.getPassword();

        List<XYZ> resList = null;
        if(utils.getResMap().containsKey(obj)){
            resList = utils.getResMap().get(obj);
        }else{
            List<String> maxResAndSubMaxRes = utils.getMaxResAndSubMaxRes(obj, username, password);
            resList = utils.transResString2XYZ(maxResAndSubMaxRes);
            utils.getResMap().put(obj, resList);
        }

        for (List<PointInfo> info : infos) {
            for (PointInfo tempPointInfo : info) {
                XYZ tmpCoor = utils.convertMaxRes2CurrResCoords(resList.get(0), resList.get(1), tempPointInfo.x, tempPointInfo.y, tempPointInfo.z);
                tempPointInfo.x = tmpCoor.x;
                tempPointInfo.y = tmpCoor.y;
                tempPointInfo.z = tmpCoor.z;
                for (int p = 0; p < tempPointInfo.getParentsCoors().size(); p++) {
                    tempPointInfo.getParentsCoors().set(p, utils.convertMaxRes2CurrResCoords(resList.get(0), resList.get(1),
                            tempPointInfo.getParentsCoors().get(p).x, tempPointInfo.getParentsCoors().get(p).y,
                            tempPointInfo.getParentsCoors().get(p).z));
                }
                for (int p = 0; p < tempPointInfo.getOffspringsCoors().size(); p++) {
                    tempPointInfo.getOffspringsCoors().set(p, utils.convertMaxRes2CurrResCoords(resList.get(0), resList.get(1),
                            tempPointInfo.getOffspringsCoors().get(p).x, tempPointInfo.getOffspringsCoors().get(p).y,
                            tempPointInfo.getOffspringsCoors().get(p).z));
                }
            }
        }
        String jsonString = JSON.toJSONString(infos);
        String jsonFilePath = swcPath.substring(0, swcPath.length() - ".eswc".length()) + ".json";
        utils.generateJsonFile(jsonString, jsonFilePath);

        Map<String, Object> mapStr = new HashMap<String, Object>();
        mapStr.put("json_path", jsonFilePath);
        mapStr.put("input_path", baseDir);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("input", mapStr);
        return JSON.toJSONString(inputMap);
    }

}
