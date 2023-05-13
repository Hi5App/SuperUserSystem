package com.zhy.springboot.superuserserver.service;

import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.utils.BaseModelUtils;
import com.zhy.springboot.superuserserver.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhy
 * @Date 2023/5/10 17:12
 * @Description This is description of class
 * @Since version-1.0
 */
@Service
public class DetectService {
    @Autowired
    GlobalConfigs globalConfigs;

    public String detectCrossing(){
        Utils.preProcess();

        BaseModelUtils.loadModel(BaseModelUtils.ModelType.Crossing);

        String result=BaseModelUtils.detectByModel(BaseModelUtils.ModelType.Crossing,globalConfigs.getUrlForCrossingModel());

        BaseModelUtils.unloadModel(BaseModelUtils.ModelType.Crossing);

        Utils.postProcess();

        return "";
    }

    public String detectMissing(){
        Utils.preProcess();

        BaseModelUtils.loadModel(BaseModelUtils.ModelType.Missing);

        String result=BaseModelUtils.detectByModel(BaseModelUtils.ModelType.Missing,globalConfigs.getUrlForMissingModel());

        BaseModelUtils.unloadModel(BaseModelUtils.ModelType.Missing);

        Utils.postProcess();

        return "";
    }
}
