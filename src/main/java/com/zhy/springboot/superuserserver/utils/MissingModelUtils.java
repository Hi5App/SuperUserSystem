package com.zhy.springboot.superuserserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author zhy
 * @Date 2023/10/8 15:52
 * @Description MissingModel工具类
 * @Since version-1.0
 */
@Slf4j
@Component
public class MissingModelUtils extends BaseModelUtils {
    public ModelType modelType = ModelType.Missing;

    @Override
    public void loadModel() {
        log.info("load the missing model...");
    }

    @Override
    public void unloadModel() {
        log.info("unload the missing model...");
    }

    @Override
    public JSONArray detectByModel(String url, String json) {
        log.info("调用检测missing的模型");
        return super.detectByModel(url, json);
    }
    public void preProcess(String baseDir, String swcPath, String obj, List<XYZ> coors, int[] patchSize) {
        log.info("enter MissingModelUtils preProcess...");
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
        utils.convertCoorsInSwc(swcPath, imageMaxRes, imageCurRes);

        //获取切割swc所需参数
        File swcFile = new File(swcPath);
        String swcName = swcFile.getName();
        int dotIndex = swcName.lastIndexOf(".");
        if (dotIndex > 0) {
            swcName = swcName.substring(0, dotIndex);
        }
        String resForCropSwc = String.join(File.separator, "/test", obj);

        utils.copySwcFile2AnotherPath(obj, swcPath, swcFile.getName());

        for (XYZ coor : coors) {
            // String xmin = String.format("%06d", (int) coor.x - patchSize[0] / 2);
            // String ymin = String.format("%06d", (int) coor.y - patchSize[0] / 2);
            // String zmin = String.format("%06d", (int) coor.z - patchSize[0] / 2);
            // String xmax = String.format("%06d", (int) coor.x + patchSize[0] / 2);
            // String ymax = String.format("%06d", (int) coor.y + patchSize[0] / 2);
            // String zmax = String.format("%06d", (int) coor.z + patchSize[0] / 2);
            //坐标转换
            XYZ convertedCoor = utils.convertMaxRes2CurrResCoords(imageMaxRes, imageCurRes, coor.x, coor.y, coor.z);

            String fileName = (int) convertedCoor.x + "_" + (int) convertedCoor.y + "_" + (int) convertedCoor.z;
            String dirPath = String.join(File.separator, baseDir, fileName);
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
            XYZ pa1 = new XYZ((int) convertedCoor.x - patchSize[0] / 2, (int) convertedCoor.y - patchSize[0] / 2, (int) convertedCoor.z - patchSize[0] / 2);
            XYZ pa2 = new XYZ((int) convertedCoor.x + patchSize[0] / 2, (int) convertedCoor.y + patchSize[0] / 2, (int) convertedCoor.z + patchSize[0] / 2);
            utils.getCroppedImage(pa1, pa2, dirPath, obj, imageCurRes, username, password);

            //获取切割后的swc
            utils.getCroppedSwc(pa1, pa2, swcName, resForCropSwc, username, password, dirPath);
        }

    }

    public void postProcess(String swcPath, String baseDir) {
        log.info("enter MissingModelUtils postProcess...");
        File swcFile = new File(swcPath);
        boolean ok = swcFile.delete();
        if(!ok){
            log.info("删除文件失败!");
        }
        File dirPath = new File(baseDir);
        utils.deleteFile(dirPath);
    }

}
