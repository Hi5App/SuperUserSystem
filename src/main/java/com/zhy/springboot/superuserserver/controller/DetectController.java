package com.zhy.springboot.superuserserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.bean.CrossingInfo;
import com.zhy.springboot.superuserserver.bean.MissingInfo;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.service.DetectService;
import com.zhy.springboot.superuserserver.utils.R;
import com.zhy.springboot.superuserserver.utils.XYZ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhy
 * @Date 2023/5/10 17:11
 * @Description DetectController
 * @Since version-1.0
 */
@Slf4j
@RestController
@RequestMapping("/detect")
public class DetectController {
    @Autowired
    DetectService detectService;

    @Autowired
    private GlobalConfigs globalConfigs;

    private String swcPathForMissing;

    private String swcPathForCrossing;

    @PostMapping(value = {"/file/for-missing"})
    public R getSwcFileForMissing(MultipartFile swcFile) {
        R r = new R();
        String swcPath = "";

        if (swcFile != null && !swcFile.isEmpty()) {
            try {
                String swcName = swcFile.getOriginalFilename();
                swcPath = String.join(File.separator, globalConfigs.getSavePathForPredict(), "tip", swcName);
                swcPathForMissing = swcPath;
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {
                    inputStream = swcFile.getInputStream();
                    File f = new File(swcPath);
                    f.createNewFile();
                    outputStream = new FileOutputStream(f);
                    int size = 0;
                    byte[] buffer = new byte[1024];
                    while ((size = inputStream.read(buffer, 0, 1024)) != -1) {
                        outputStream.write(buffer, 0, size);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                    r.setMsg(e.getMessage());
                    r.setCode("202");
                    return r;
                } finally {
                    outputStream.close();
                    inputStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                r.setMsg(e.getMessage());
                r.setCode("202");
                return r;
            }

        } else {
            log.info("swcfile not exists!");
            r.setMsg("swcfile not exists!");
            r.setCode("201");
            return r;
        }
        r.setCode("200");
        r.setMsg("success!");
        log.info("send file success!");
        return r;
    }

    @PostMapping(value = {"/file/for-crossing"})
    public R getSwcFileForCrossing(MultipartFile swcFile) {
        R r = new R();
        String swcPath = "";

        if (swcFile != null && !swcFile.isEmpty()) {
            try {
                String swcName = swcFile.getOriginalFilename();
                swcPath = String.join(File.separator, globalConfigs.getSavePathForPredict(), "crossing", swcName);
                swcPathForCrossing = swcPath;
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {
                    inputStream = swcFile.getInputStream();
                    File f = new File(swcPath);
                    f.createNewFile();
                    outputStream = new FileOutputStream(f);
                    int size = 0;
                    byte[] buffer = new byte[1024];
                    while ((size = inputStream.read(buffer, 0, 1024)) != -1) {
                        outputStream.write(buffer, 0, size);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                    r.setMsg(e.getMessage());
                    r.setCode("202");
                    return r;
                } finally {
                    outputStream.close();
                    inputStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                r.setMsg(e.getMessage());
                r.setCode("202");
                return r;
            }

        } else {
            log.info("swcfile not exists!");
            r.setMsg("swcfile not exists!");
            r.setCode("201");
            return r;
        }
        r.setCode("200");
        r.setMsg("success!");
        log.info("send file success!");
        return r;
    }


    @PostMapping(value = {"/crossing"})
    public R detectCrossing(@RequestBody CrossingInfo info) {
        R r = new R();
        String obj = info.getObj();
        String res = info.getRes();
        List<XYZ> coors = info.getCoors();
        File f = new File(swcPathForCrossing);
        if (!f.exists()) {
            log.info("swcfile not exists!");
            r.setMsg("swcfile not exists!");
            r.setCode("201");
            return r;
        }
        String baseDir = swcPathForCrossing.substring(0, swcPathForCrossing.length() - ".ano.eswc".length());
        File dir = new File(baseDir);
        boolean flag = false;
        if (!dir.exists()) {
            flag = dir.mkdirs();
            if (!flag) {
                log.info("cannot create dir!");
                r.setMsg("cannot create dir!");
                r.setCode("203");
                return r;
            }
        }

        JSONArray result = detectService.detectCrossing(swcPathForCrossing, baseDir, obj, res, coors);
        if (result == null) {
            log.info("fail to call the model!");
            r.setMsg("fail to call the model!");
            r.setCode("204");
            return r;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        int size = result.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> tmpMap = new HashMap<>(4);
            JSONObject jsonObject = result.getJSONObject(i);
            List<Integer> allCoors = new ArrayList<>();
            List<Integer> rCoors = new ArrayList<>();
            String name = jsonObject.getString("name");
            for (String retval : name.split("_")) {
                allCoors.add(Integer.parseInt(retval));
            }
            for (int j = 0; j < 3; j++) {
                rCoors.add((allCoors.get(j) + allCoors.get(j)) / 2);
            }
            tmpMap.put("coors", rCoors);
            tmpMap.put("y_pred", jsonObject.getInteger("y_pred"));
            list.add(tmpMap);
        }
        r.setCode("200");
        r.setMsg("success!");
        log.info("detect crossing success!");
        r.setData(list);
        return r;
    }

    @PostMapping(value = {"/missing"})
    public R detectMissing(@RequestBody MissingInfo info) {
        R r = new R();
        String obj = info.getObj();
        String res = info.getRes();
        List<XYZ> coors = info.getCoors();
        // swcPathForMissing="C:\\Users\\10422\\Downloads\\01864_P020_T01-S030_ROL_R0613_RJ-20221021_RJ_02.ano.eswc";
        System.out.println(info);
        System.out.println(swcPathForMissing);
        File f = new File(swcPathForMissing);
        if (!f.exists()) {
            log.info("swcfile not exists!");
            r.setMsg("swcfile not exists!");
            r.setCode("201");
            return r;
        }
        String baseDir = swcPathForMissing.substring(0, swcPathForMissing.length() - ".ano.eswc".length());
        File dir = new File(baseDir);
        boolean flag = false;
        if (!dir.exists()) {
            flag = dir.mkdirs();
            if (!flag) {
                log.info("cannot create dir!");
                r.setMsg("cannot create dir!");
                r.setCode("203");
                return r;
            }
        }

        JSONArray result = detectService.detectMissing(swcPathForMissing, baseDir, obj, res, coors);
        if (result == null) {
            log.info("fail to call the model!");
            r.setMsg("fail to call the model!");
            r.setCode("204");
            return r;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        int size = result.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> tmpMap = new HashMap<>(4);
            JSONObject jsonObject = result.getJSONObject(i);
            List<Integer> allCoors = new ArrayList<>();
            List<Integer> rCoors = new ArrayList<>();
            String name = jsonObject.getString("name");
            for (String retval : name.split("_")) {
                allCoors.add(Integer.parseInt(retval));
            }
            for (int j = 0; j < 3; j++) {
                rCoors.add((allCoors.get(j) + allCoors.get(j)) / 2);
            }
            tmpMap.put("coors", rCoors);
            tmpMap.put("y_pred", jsonObject.getInteger("y_pred"));
            list.add(tmpMap);
        }
        r.setCode("200");
        r.setMsg("success!");
        log.info("detect missing success!");
        r.setData(list);
        return r;
    }
}

