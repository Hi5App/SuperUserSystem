package com.zhy.springboot.superuserserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.bean.dto.*;
import com.zhy.springboot.superuserserver.bean.entity.*;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.service.DetectService;
import com.zhy.springboot.superuserserver.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public Utils utils;

    @Autowired
    private GlobalConfigs globalConfigs;

    public final Map<String, TaskInfo> mapForMissing = new ConcurrentHashMap<>();
    public final Map<String, TaskInfo> mapForCrossing = new ConcurrentHashMap<>();

    @PostMapping(value = {"/file/for-missing"})
    public R getSwcFileForMissing(MultipartFile swcFile) {
        log.info("enter getSwcFileForMissing");
        System.out.println(swcFile);
        R r = new R();
        String swcPath = "";
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        if (swcFile != null && !swcFile.isEmpty()) {
            try {
                String swcName = swcFile.getOriginalFilename();
                String swcNameWithNoSuffix;
                if (swcName != null) {
                    swcNameWithNoSuffix = swcName.substring(0, swcName.length() - ".ano.eswc".length());
                } else {
                    log.info("swcfile not exists!");
                    r.setMsg("swcfile not exists!");
                    r.setCode("201");
                    return r;
                }

                String baseDir = "";
                baseDir = String.join(File.separator, globalConfigs.getSavePathForPredict(), "tip", swcNameWithNoSuffix);

                File dir = new File(baseDir);
                boolean flag = false;
                if (!dir.exists()) {
                    flag = dir.mkdirs();
                    try {
                        Files.setPosixFilePermissions(Paths.get(String.valueOf(dir)), perms);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!flag) {
                        log.info("cannot create dir!");
                        r.setMsg("cannot create dir!");
                        r.setCode("203");
                        return r;
                    }
                }

                LocalDateTime currentTime = LocalDateTime.now();
                // 定义日期时间格式化器
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                // 将当前时间按照指定格式转换为字符串
                String formattedDate = currentTime.format(formatter);
                String timeDirPath = String.join(File.separator, baseDir, formattedDate);
                File timeDir = new File(timeDirPath);
                // log.info(timeDirPath);
                if (!timeDir.exists()) {
                    // log.info("entered...");
                    boolean isSuccess = timeDir.mkdirs();
                    if(!isSuccess){
                        log.info("fail to mkdirs");
                        r.setMsg("fail to mkdirs");
                        r.setCode("202");
                        return r;
                    }
                    try {
                        Files.setPosixFilePermissions(Paths.get(String.valueOf(timeDir)), perms);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                swcPath = String.join(File.separator, timeDirPath, swcName);
                mapForMissing.put(swcNameWithNoSuffix, new TaskInfo(swcPath, timeDirPath));

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
        try {
            Files.setPosixFilePermissions(Paths.get(swcPath), perms);
        } catch (IOException e) {
            e.printStackTrace();
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
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        if (swcFile != null && !swcFile.isEmpty()) {
            try {
                String swcName = swcFile.getOriginalFilename();
                String swcNameWithNoSuffix;
                if (swcName != null) {
                    swcNameWithNoSuffix = swcName.substring(0, swcName.length() - ".ano.eswc".length());
                } else {
                    log.info("swcfile not exists!");
                    r.setMsg("swcfile not exists!");
                    r.setCode("201");
                    return r;
                }
                String baseDir = "";
                baseDir = String.join(File.separator, globalConfigs.getSavePathForPredict(), "crossing", swcNameWithNoSuffix);
                File dir = new File(baseDir);
                boolean flag = false;
                if (!dir.exists()) {
                    flag = dir.mkdirs();
                    try {
                        Files.setPosixFilePermissions(Paths.get(String.valueOf(dir)), perms);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!flag) {
                        log.info("cannot create dir!");
                        r.setMsg("cannot create dir!");
                        r.setCode("203");
                        return r;
                    }
                }

                LocalDateTime currentTime = LocalDateTime.now();
                // 定义日期时间格式化器
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                // 将当前时间按照指定格式转换为字符串
                String formattedDate = currentTime.format(formatter);
                String timeDirPath = String.join(File.separator, baseDir, formattedDate);
                File timeDir = new File(timeDirPath);
                if (!timeDir.exists()) {
                    boolean isSuccess = timeDir.mkdirs();
                    if(!isSuccess){
                        log.info("fail to mkdirs");
                        r.setMsg("fail to mkdirs");
                        r.setCode("202");
                        return r;
                    }
                    try {
                        Files.setPosixFilePermissions(Paths.get(String.valueOf(timeDir)), perms);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                swcPath = String.join(File.separator, timeDirPath, swcName);
                mapForCrossing.put(swcNameWithNoSuffix, new TaskInfo(swcPath, timeDirPath));

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
        try {
            Files.setPosixFilePermissions(Paths.get(swcPath), perms);
        } catch (IOException e) {
            e.printStackTrace();
        }
        r.setCode("200");
        r.setMsg("success!");
        log.info("send file success!");
        return r;
    }


    @PostMapping(value = {"/crossing"})
    public R detectCrossing(@RequestBody CrossingInfo crossingInfo) {
        R r = new R();
        String obj = crossingInfo.getObj();
        List<List<PointInfo>> infos = crossingInfo.getInfos();
        String swcNameWithNoSuffix = crossingInfo.getSwcNameWithNoSuffix();
        String objRelaventPath = crossingInfo.getObjRelaventPath();
        TaskInfo taskInfo;
        if (mapForCrossing.containsKey(swcNameWithNoSuffix)) {
            taskInfo = mapForCrossing.get(swcNameWithNoSuffix);
        } else {
            log.info("taskinfo not exists!");
            r.setMsg("taskinfo not exists!");
            r.setCode("201");
            return r;
        }
        mapForCrossing.remove(swcNameWithNoSuffix);

        File f = new File(taskInfo.getSwcPath());
        if (!f.exists()) {
            log.info("swcfile not exists!");
            r.setMsg("swcfile not exists!");
            r.setCode("201");
            return r;
        }

        JSONArray result = detectService.detectCrossing(taskInfo, obj, objRelaventPath, infos);
        if (result == null) {
            log.info("fail to call the model!");
            r.setMsg("fail to call the model!");
            r.setCode("204");
            return r;
        }

        DetectCrossingResData resData = new DetectCrossingResData();
        List<FiberPredictedResult> fiberPredictedResultList = new ArrayList<>();
        int size = result.size();
        for (int i = 0; i < size; i++) {
            FiberPredictedResult fiberPredictedResult = new FiberPredictedResult();
            JSONObject fiberPredictedResultJO = result.getJSONObject(i);

            Integer y_pred = fiberPredictedResultJO.getInteger("y_pred");
            fiberPredictedResult.setY_pred(y_pred);

            List<FiberCoorInfo> fiberCoorInfoList = new ArrayList<>();
            JSONArray fiberCoorInfoListJA = fiberPredictedResultJO.getJSONArray("fiberCoorInfoList");
            for (int j = 0; j < fiberCoorInfoListJA.size(); j++){
                FiberCoorInfo fiberCoorInfo = new FiberCoorInfo();
                JSONObject fiberCoorInfoJO = result.getJSONObject(i);

                String coor = fiberCoorInfoJO.getString("coor");
                String maxCoor = taskInfo.getCurCoor2MaxCoorMap().get(coor);
                String[] retvals = maxCoor.split("_");
                List<Float> allCoors = Arrays.stream(retvals)
                        .map(Float::valueOf)
                        .collect(Collectors.toList());
                fiberCoorInfo.setCoor(new XYZ(allCoors.get(0), allCoors.get(1), allCoors.get(2)));

                List<XYZ> maxFiberCoorList = new ArrayList<>();
                JSONArray fiberCoorListJA = fiberCoorInfoJO.getJSONArray("fiberCoorList");
                for (int p = 0; p < fiberCoorListJA.size(); p++){
                    String fiberCoor = fiberCoorListJA.getString(p);
                    String maxFiberCoor = taskInfo.getCurCoor2MaxCoorMap().get(fiberCoor);
                    String[] tmpRetvals = maxFiberCoor.split("_");
                    List<Float> tmpAllCoors = Arrays.stream(tmpRetvals)
                            .map(Float::valueOf)
                            .collect(Collectors.toList());
                    maxFiberCoorList.add(new XYZ(tmpAllCoors.get(0), tmpAllCoors.get(1), tmpAllCoors.get(2)));
                }
                fiberCoorInfo.setFiberCoorList(maxFiberCoorList);
                fiberCoorInfoList.add(fiberCoorInfo);
            }
            fiberPredictedResult.setFiberCoorInfoList(fiberCoorInfoList);
            fiberPredictedResultList.add(fiberPredictedResult);
        }

        resData.setFiberPredictedResultList(fiberPredictedResultList);

        r.setCode("200");
        r.setMsg("success!");
        log.info("detect crossing success!");
        r.setData(resData);
        return r;
    }

    @PostMapping(value = {"/missing"})
    public R detectMissing(@RequestBody MissingInfo missingInfo) {
        R r = new R();
        String obj = missingInfo.getObj();
        List<XYZ> coors = missingInfo.getCoors();
        String swcNameWithNoSuffix = missingInfo.getSwcNameWithNoSuffix();
        String objRelaventPath = missingInfo.getObjRelaventPath();
        // swcPathForMissing="C:\\Users\\10422\\Downloads\\01864_P020_T01-S030_ROL_R0613_RJ-20221021_RJ_02.ano.eswc";
        TaskInfo taskInfo;
        if (mapForMissing.containsKey(swcNameWithNoSuffix)) {
            taskInfo = mapForMissing.get(swcNameWithNoSuffix);
        } else {
            log.info("taskinfo not exists!");
            r.setMsg("taskinfo not exists!");
            r.setCode("201");
            return r;
        }
        mapForMissing.remove(swcNameWithNoSuffix);

        File f = new File(taskInfo.getSwcPath());
        if (!f.exists()) {
            log.info("swcfile not exists!");
            r.setMsg("swcfile not exists!");
            r.setCode("201");
            return r;
        }

        JSONArray result = detectService.detectMissing(taskInfo, obj, objRelaventPath, coors);
        if (result == null) {
            log.info("fail to call the model!");
            r.setMsg("fail to call the model!");
            r.setCode("204");
            return r;
        }

        DetectMissingResData detectMissingResData = new DetectMissingResData();
        List<CoorPredictedResult> list = new ArrayList<>();
        int size = result.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> tmpMap = new HashMap<>(4);
            CoorPredictedResult coorPredictedResult = new CoorPredictedResult();

            JSONObject jsonObject = result.getJSONObject(i);
            String name = jsonObject.getString("name");
            String maxCoors = taskInfo.getCurCoor2MaxCoorMap().get(name);
            String[] retvals = maxCoors.split("_");
            List<Float> allCoors = Arrays.stream(retvals)
                    .map(Float::valueOf)
                    .collect(Collectors.toList());
            // for (int j = 0; j < 3; j++) {
            //     rCoors.add((allCoors.get(j) + allCoors.get(j+3)) / 2);
            // }

            coorPredictedResult.setMaxResCoor(new XYZ(allCoors.get(0), allCoors.get(1), allCoors.get(2)));
            coorPredictedResult.setStoreDirName(name);
            coorPredictedResult.setY_pred(jsonObject.getInteger("y_pred"));

            list.add(coorPredictedResult);
        }
        detectMissingResData.setCoorPredictedResultList(list);

        String tipDirPathStr = String.join(File.separator, globalConfigs.getSavePathForPredict(), "tip");
        Path tipDirPath = Paths.get(tipDirPathStr);
        Path timeDirPath = Paths.get(taskInfo.getBaseDirPath());
        String relPathStr = tipDirPath.relativize(timeDirPath).toString();
        detectMissingResData.setRelPath(relPathStr);

        r.setCode("200");
        r.setMsg("success!");
        log.info("detect missing success!");
        r.setData(detectMissingResData);
        return r;
    }

    @PostMapping(value = {"/branching"})
    public R detectBranching(@RequestBody BranchingInfo branchingInfo) {
        R r = new R();
        String obj = branchingInfo.getObj();
        List<XYZ> coors = branchingInfo.getCoors();
        String swcName = branchingInfo.getSwcName();
        String objRelaventPath = branchingInfo.getObjRelaventPath();

        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        String swcNameWithNoSuffix;
        if (swcName != null) {
            swcNameWithNoSuffix = swcName.substring(0, swcName.length() - ".ano.eswc".length());
        } else {
            log.info("swcfile not exists!");
            r.setMsg("swcfile not exists!");
            r.setCode("201");
            return r;
        }
        String baseDir = "";
        baseDir = String.join(File.separator, globalConfigs.getSavePathForPredict(), "branching", swcNameWithNoSuffix);
        File dir = new File(baseDir);
        boolean flag = false;
        if (!dir.exists()) {
            flag = dir.mkdirs();
            try {
                Files.setPosixFilePermissions(Paths.get(String.valueOf(dir)), perms);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!flag) {
                log.info("cannot create dir!");
                r.setMsg("cannot create dir!");
                r.setCode("203");
                return r;
            }
        }

        LocalDateTime currentTime = LocalDateTime.now();
        // 定义日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        // 将当前时间按照指定格式转换为字符串
        String formattedDate = currentTime.format(formatter);
        String timeDirPath = String.join(File.separator, baseDir, formattedDate);
        File timeDir = new File(timeDirPath);
        if (!timeDir.exists()) {
            boolean isSuccess = timeDir.mkdirs();
            if(!isSuccess){
                log.info("fail to mkdirs");
                r.setMsg("fail to mkdirs");
                r.setCode("202");
                return r;
            }
            try {
                Files.setPosixFilePermissions(Paths.get(String.valueOf(timeDir)), perms);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String swcPath = String.join(File.separator, timeDirPath, swcName);
        TaskInfo taskInfo = new TaskInfo(swcPath, timeDirPath);

        JSONArray result = detectService.detectBranching(taskInfo, obj, objRelaventPath, coors);
        if(result ==null)
        {
            log.info("fail to call the model!");
            r.setMsg("fail to call the model!");
            r.setCode("204");
            return r;
        }

        List<XYZ> resList = utils.getResMap().get(obj);
        List<Map<String, Object>> list = new ArrayList<>();
        int size = result.size();
        for(int i = 0; i<size;i++)
        {
            Map<String, Object> tmpMap = new HashMap<>(4);
            JSONObject jsonObject = result.getJSONObject(i);
            String name = jsonObject.getString("name");

            String maxCoors = taskInfo.getCurCoor2MaxCoorMap().get(name);
            String[] retvals = maxCoors.split("_");
            List<Float> allCoors = Arrays.stream(retvals)
                    .map(Float::valueOf)
                    .collect(Collectors.toList());

            tmpMap.put("coors", allCoors);
            tmpMap.put("y_pred", jsonObject.getInteger("y_pred"));
            list.add(tmpMap);
        }
        r.setCode("200");
        r.setMsg("success!");
        log.info("detect branching success!");
        r.setData(list);
        return r;
    }
}

