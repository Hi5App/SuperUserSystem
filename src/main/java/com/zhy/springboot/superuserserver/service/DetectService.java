package com.zhy.springboot.superuserserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.utils.BaseModelUtils;
import com.zhy.springboot.superuserserver.utils.PointInfo;
import com.zhy.springboot.superuserserver.utils.Utils;
import com.zhy.springboot.superuserserver.utils.XYZ;
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
    BaseModelUtils baseModelUtils;

    public JSONArray detectCrossing(String swcPath, String baseDir, String obj, String res, List<List<PointInfo>> infos){
        log.info("enter detectCrossing...");
        List<XYZ> coors=new ArrayList<>();
        for (List<PointInfo> info : infos) {
            XYZ tmp = new XYZ(info.get(0).getX(), info.get(0).getY(), info.get(0).getZ());
            coors.add(tmp);
        }
        List<XYZ> resList=utils.preProcess(baseDir, swcPath, obj, res, coors, globalConfigs.getCrossingPatchSize());
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
        String jsonFilePath = swcPath.substring(0,swcPath.length()-".eswc".length()) + ".json";
        utils.generateJsonFile(jsonString, jsonFilePath);

        baseModelUtils.loadModel(BaseModelUtils.ModelType.Crossing);

        Map<String, Object> mapStr = new HashMap<String,Object>();
        mapStr.put("json_path", jsonFilePath);
        mapStr.put("input_path", baseDir);
        Map<String, Object> inputMap=new HashMap<String, Object>();
        inputMap.put("input", mapStr);
        String json= JSON.toJSONString(inputMap);
        JSONArray result=baseModelUtils.detectByModel(BaseModelUtils.ModelType.Crossing,globalConfigs.getUrlForCrossingModel(), json);

        baseModelUtils.unloadModel(BaseModelUtils.ModelType.Crossing);

        // utils.postProcess(swcPath, baseDir);

        return result;
    }

    public JSONArray detectMissing(String swcPath, String baseDir, String obj, String res, List<XYZ> coors){
        log.info("enter detectMissing...");
        List<XYZ> resList=utils.preProcess(baseDir,swcPath, obj, res, coors, globalConfigs.getTipPatchSize());

        baseModelUtils.loadModel(BaseModelUtils.ModelType.Missing);

        Map<String, Object> mapStr = new HashMap<String,Object>();
        mapStr.put("swc_path", swcPath);
        mapStr.put("input_path", baseDir);
        mapStr.put("x_ratio", resList.get(0).x/resList.get(1).x);
        mapStr.put("y_ratio", resList.get(0).y/resList.get(1).y);
        mapStr.put("z_ratio", resList.get(0).z/resList.get(1).z);
        Map<String, Object> inputMap=new HashMap<String, Object>();
        inputMap.put("input", mapStr);
        String json= JSON.toJSONString(inputMap);
        JSONArray result=baseModelUtils.detectByModel(BaseModelUtils.ModelType.Missing,globalConfigs.getUrlForMissingModel(), json);

        baseModelUtils.unloadModel(BaseModelUtils.ModelType.Missing);

        // utils.postProcess(swcPath, baseDir);

        return result;
        // return null;
    }
}
