package com.zhy.springboot.superuserserver.utils;

import com.alibaba.fastjson.JSONArray;
import com.zhy.springboot.superuserserver.bean.entity.TaskInfo;
import com.zhy.springboot.superuserserver.bean.entity.XYZ;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

/**
 * @Author zhy
 * @Date 2023/10/8 15:52
 * @Description CrossingModel工具类
 * @Since version-1.0
 */
@Slf4j
@Component
public class CrossingModelUtils extends BaseModelUtils{
    public ModelType modelType = ModelType.Crossing;

    @Override
    public void loadModel() {
        log.info("load the crossing model...");
    }

    @Override
    public void unloadModel() {
        log.info("unload the crossing model...");
    }

    @Override
    public JSONArray detectByModel(String url, String json) {
        log.info("调用检测crossing的模型");
        return super.detectByModel(url, json);
    }

    public void preProcess(TaskInfo taskInfo, String obj, String objRelaventPath, List<Pair<XYZ, XYZ>> coors, int[] patchSize) {
        log.info("enter crossingModelUtils preProcess...");
        //get username and password
        String username = globalConfigs.getUsername();
        String password = globalConfigs.getPassword();

        //get res
        List<XYZ> resList = null;
        if(utils.getResMap().containsKey(obj)){
            resList = utils.getResMap().get(obj);
        }else{
            List<String> maxResAndSubMaxRes = utils.getMaxResAndSubMaxRes(obj, username, password);
            resList = utils.transResString2XYZ(maxResAndSubMaxRes);
            utils.getResMap().put(obj, resList);
        }

        XYZ imageMaxRes = resList.get(0);
        XYZ imageCurRes = resList.get(1);

        //swc坐标变换
        utils.convertCoorsInSwc(taskInfo.getSwcPath(), imageMaxRes, imageCurRes);

        //获取切割swc所需参数
        File swcFile = new File(taskInfo.getSwcPath());
        String swcName = swcFile.getName();
        int dotIndex = swcName.lastIndexOf(".");
        if (dotIndex > 0) {
            swcName = swcName.substring(0, dotIndex);
        }
        String resForCropSwc = String.join(File.separator, "/tempswc", obj);

        utils.copySwcFile2AnotherPath(obj, taskInfo.getSwcPath(), swcFile.getName());

        for (Pair<XYZ, XYZ> coorPair : coors) {
            // 坐标转换
            XYZ convertedCoor1 = utils.convertMaxRes2CurrResCoords(imageMaxRes, imageCurRes, coorPair.getKey().x, coorPair.getKey().y, coorPair.getKey().z);
            XYZ convertedCoor2 = utils.convertMaxRes2CurrResCoords(imageMaxRes, imageCurRes, coorPair.getValue().x, coorPair.getValue().y, coorPair.getValue().z);

            String fileName = (int) convertedCoor1.x + "_" + (int) convertedCoor1.y + "_" + (int) convertedCoor1.z;
            String convertedCoor1Str = convertedCoor1.x + "_" + convertedCoor1.y + "_" + convertedCoor1.z;
            String convertedCoor2Str = convertedCoor2.x + "_" + convertedCoor2.y + "_" + convertedCoor2.z;
            taskInfo.getCurCoor2MaxCoorMap().put(convertedCoor1Str, coorPair.getKey().x + "_" + coorPair.getKey().y + "_" + coorPair.getKey().z);
            taskInfo.getCurCoor2MaxCoorMap().put(convertedCoor2Str, coorPair.getValue().x + "_" + coorPair.getValue().y + "_" + coorPair.getValue().z);

            String dirPath = String.join(File.separator, taskInfo.getBaseDirPath(), fileName);
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
            File eachDir = new File(dirPath);
            if (!eachDir.isDirectory()) {
                boolean ok =eachDir.mkdirs();
                if(!ok){
                    log.info("创建文件夹失败!");
                    return;
                }
                try {
                    Files.setPosixFilePermissions(Paths.get(String.valueOf(eachDir)), perms);
                } catch (IOException e) {
                    log.info(e.getMessage());
                    e.printStackTrace();
                    return;
                }
            }

            // 获取图像块
            XYZ pa1 = new XYZ((int) (convertedCoor1.x - patchSize[0] / 2), (int) (convertedCoor1.y - patchSize[0] / 2), (int) (convertedCoor1.z - patchSize[0] / 2));
            XYZ pa2 = new XYZ((int) (convertedCoor1.x + patchSize[0] / 2), (int) (convertedCoor1.y + patchSize[0] / 2), (int) (convertedCoor1.z + patchSize[0] / 2));
            String imagePath = obj;
            if(objRelaventPath != null){
                imagePath = String.join(File.separator, objRelaventPath, obj);
            }
            utils.getCroppedImage(pa1, pa2, dirPath, imagePath, imageCurRes, username, password);

            //获取切割后的swc
            utils.getCroppedSwc(pa1, pa2, swcName, resForCropSwc, username, password, dirPath);
        }

    }

    public void postProcess(String swcPath, String baseDir) {
        log.info("enter crossingModelUtils postProcess...");
        File swcFile = new File(swcPath);
        boolean ok = swcFile.delete();
        if(!ok){
            log.info("删除文件失败!");
        }
        File dirPath = new File(baseDir);
        utils.deleteFile(dirPath);
    }
}
