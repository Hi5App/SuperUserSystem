package com.zhy.springboot.superuserserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zhy
 * @Date 2023/5/10 15:01
 * @Description 工具类
 * @Since version-1.0
 */
@Slf4j
@Component
public class Utils {
    @Autowired
    private GlobalConfigs globalConfigs;
    private Semaphore semaphore = null;
    @Resource
    private OkHttpUtil okHttpUtil;

    private Map<String, List<XYZ>> resMap = new HashMap<>();

    public Map<String, List<XYZ>> getResMap() {
        return resMap;
    }

    public void setSemaphore() {
        if (semaphore == null) {
            semaphore = new Semaphore(globalConfigs.getCropprocess());
        }
    }

    public String consumeInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            System.out.println(s);
            sb.append(s);
        }
        return sb.toString();
    }

    public String getBBImage(BBox bBox) {
        setSemaphore();
        // Semaphore semaphore = new Semaphore(globalConfigs.getCropprocess());
        String savefilePath = "";
        BufferedReader br = null;
        try {
            semaphore.acquire();
            savefilePath = String.format("%s/%s_%d_%d_%d_%d_%d_%d_%d.v3dpbd", globalConfigs.getTmpdir(),
                    bBox.obj, (int) bBox.coord1.x, (int) bBox.coord1.y, (int) bBox.coord1.z, (int) bBox.coord2.x,
                    (int) bBox.coord2.y, (int) bBox.coord2.z, System.currentTimeMillis());
            String cmd = String.format("%s %s/%s/%s %s %s %s %s %s %s %s", globalConfigs.getCropImageBin(),
                    globalConfigs.getImageDir(), bBox.obj, bBox.res, savefilePath, String.valueOf((int) bBox.coord1.x),
                    String.valueOf((int) bBox.coord1.y), String.valueOf((int) bBox.coord1.z), String.valueOf((int) bBox.coord2.x),
                    String.valueOf((int) bBox.coord2.y), String.valueOf((int) bBox.coord2.z));
            log.info("getBBImage_cmd: " + cmd);
            // Runtime runtime = Runtime.getRuntime();  //获取Runtime实例
            // Process process = runtime.exec(cmd);
            File tmpFile = new File("./temp.tmp");//新建一个用来存储结果的缓存文件
            if (!tmpFile.exists()) {
                tmpFile.createNewFile();
            }

            ProcessBuilder pb = new ProcessBuilder().command(cmd).inheritIO();
            pb.redirectErrorStream(true);//合并输出流和错误流
            pb.redirectOutput(tmpFile);//把执行结果输出。
            pb.start().waitFor();//等待语句执行完成，否则可能会读不到结果。
            InputStream in = new FileInputStream(tmpFile);
            br = new BufferedReader(new InputStreamReader(in));
            log.info("getBBImage_cmd输出流和错误流:");
            String line = null;
            while ((line = br.readLine()) != null) {
                log.info(line);
            }
            br.close();
            tmpFile.delete();
            log.info("getBBImage_cmd执行结束");

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        return savefilePath;
    }

    public void generateJsonFile(String jsonString, String filePath) {
        Path path = Paths.get(filePath);

        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copySwcFile2AnotherPath(String obj, String swcPath, String swcName){
        //生成Image文件夹
        String newSwcDir = String.join(File.separator,"/TeraConvertedBrain/data/arbor/test", obj);
        Path newSwcDirPath = Paths.get(newSwcDir);
        if(!Files.exists(newSwcDirPath)){
            try {
                Files.createDirectories(newSwcDirPath);
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
                Files.setPosixFilePermissions(newSwcDirPath, perms);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("创建目录失败!");
            }
        }

        //复制一份swc文件
        String newSwcPath=String.join(File.separator,"/TeraConvertedBrain/data/arbor/test", obj, swcName);
        Path sourcePath = Paths.get(swcPath);
        Path targetPath = Paths.get(newSwcPath);
        try {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("复制文件失败!");
        }
    }

    public void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            log.info("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        if(files != null) {
            for (File f : files) {
                //判断子目录是否存在子目录,如果是文件则删除
                if (f.isDirectory()) {
                    deleteFile(f);
                } else {
                    boolean ok = f.delete();
                    if(!ok){
                        log.info("删除文件失败!");
                    }
                }
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        boolean ok = file.delete();
        if(!ok){
            log.info("删除文件夹失败");
        }
    }

    public List<String> getMaxResAndSubMaxRes(String obj, String username, String password) {
        log.info("get res");

        Map<String, Object> imageMap = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", username);
        userMap.put("passwd", password);
        Map<String, Object> imageInfoMap = new HashMap<>();
        imageInfoMap.put("name", obj);
        imageInfoMap.put("detail", "");
        imageMap.put("user", userMap);
        imageMap.put("Image", imageInfoMap);

        String result = okHttpUtil.postForJsonString(globalConfigs.getUrlForGetImageList(), JSON.toJSONString(imageMap));
        if (result == null) {
            log.info("getImageRes error!");
            return null;
        }
        // System.out.println(result);
        JSONArray jsonArrayTemp = (JSONArray) JSONArray.parse(result);
        JSONArray jsonResultArrayTemp = null;
        for (Object o : jsonArrayTemp) {
            JSONObject jsonObject = JSONArray.parseObject(o.toString());
            String name = jsonObject.getString("name");
            if (Objects.equals(name, obj)) {
                jsonResultArrayTemp = (JSONArray) JSONArray.parse(jsonObject.get("detail").toString());
                break;
            }
        }
        if (jsonResultArrayTemp == null) {
            log.info("getImageRes error!");
            return null;
        }
        String subMaxRes = jsonResultArrayTemp.getString(1);
        String maxRes = jsonResultArrayTemp.getString(0);

        List<String> resStrings = new ArrayList<>();
        resStrings.add(maxRes);
        resStrings.add(subMaxRes);

        return resStrings;
    }

    public List<XYZ> transResString2XYZ(List<String> resStrings) {
        String patternString = "RES\\((\\d+)x(\\d+)x(\\d+)\\)";
        XYZ ImageMaxRes = new XYZ();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher1 = pattern.matcher(resStrings.get(0));
        while (matcher1.find()) {
            ImageMaxRes.y = Integer.parseInt(matcher1.group(1));
            ImageMaxRes.x = Integer.parseInt(matcher1.group(2));
            ImageMaxRes.z = Integer.parseInt(matcher1.group(3));
            System.out.println(ImageMaxRes.x + " " + ImageMaxRes.y + " " + ImageMaxRes.z);
        }

        XYZ ImageCurRes = new XYZ();
        Matcher matcher2 = pattern.matcher(resStrings.get(1));
        while (matcher2.find()) {
            ImageCurRes.y = Integer.parseInt(matcher2.group(1));
            ImageCurRes.x = Integer.parseInt(matcher2.group(2));
            ImageCurRes.z = Integer.parseInt(matcher2.group(3));
            System.out.println(ImageCurRes.x + " " + ImageCurRes.y + " " + ImageCurRes.z);
        }

        List<XYZ> resList = new ArrayList<>();
        resList.add(ImageMaxRes);
        resList.add(ImageCurRes);

        return resList;
    }

    public String transResXYZ2String(XYZ resXYZ) {
        return String.format("RES(%dx%dx%d)", (int)resXYZ.y, (int)resXYZ.x, (int)resXYZ.z);
    }

    public void getCroppedImage(XYZ pa1, XYZ pa2, String dirPath, String obj, XYZ res, String username, String password){
        String resString = transResXYZ2String(res);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", username);
        userMap.put("passwd", password);

        Map<String, Object> bbMap = new HashMap<>();
        Map<String, Object> bBoxMap = new HashMap<>();
        bbMap.put("obj", obj);
        bbMap.put("res", resString);
        bbMap.put("pa1", pa1);
        bbMap.put("pa2", pa2);
        bBoxMap.put("user", userMap);
        bBoxMap.put("bb", bbMap);
        JSONObject bBox = new JSONObject(bBoxMap);

        byte[] bytes = okHttpUtil.postForFile(globalConfigs.getUrlForGetBBImage(), JSON.toJSONString(bBox));
        if (bytes == null) {
            log.info("get cropimage error!");
            return ;
        }
        String tifName = "optical.v3dpbd";
        String tifPath = String.join(File.separator,dirPath,tifName);
        FileOutputStream oStream = null;
        try {
            oStream = new FileOutputStream(tifPath);
            oStream.write(bytes);
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCroppedSwc(XYZ pa1, XYZ pa2, String swcName, String resForCropSwc, String username, String password, String dirPath){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", username);
        userMap.put("passwd", password);

        Map<String, Object> bbMap = new HashMap<>();
        Map<String, Object> bBoxMap = new HashMap<>();
        bbMap.put("obj", swcName);
        bbMap.put("res", resForCropSwc);
        bbMap.put("pa1", pa1);
        bbMap.put("pa2", pa2);
        bBoxMap.put("user", userMap);
        bBoxMap.put("bb", bbMap);
        JSONObject bBox = new JSONObject(bBoxMap);
        byte[] bytes = okHttpUtil.postForFile(globalConfigs.getUrlForGetBBSwc(), JSON.toJSONString(bBox));
        if (bytes == null) {
            log.info("get cropswc error!");
            return;
        }
        String cropSwcName = "optical.eswc";
        String cropSwcPath = String.join(File.separator, dirPath, cropSwcName);
        FileOutputStream oStream2 = null;
        try {
            oStream2 = new FileOutputStream(cropSwcPath);
            oStream2.write(bytes);
            oStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //处理optical.eswc中的坐标
        handleCoorsInCropSwc(cropSwcPath, pa1);
    }

    public void handleCoorsInCropSwc(String swcPath, XYZ pa1) {
        List<String> lines = null;
        Path path = null;
        try {
            path = Paths.get(swcPath);
            lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                if (!lines.get(i).contains("#")) {
                    List<String> items = Arrays.asList(lines.get(i).split(" "));
                    float x = Float.parseFloat(items.get(2));
                    x -= pa1.x;
                    float y = Float.parseFloat(items.get(3));
                    y -= pa1.y;
                    float z = Float.parseFloat(items.get(4));
                    z -= pa1.z;
                    // 使用 DecimalFormat 格式化结果并保留三位小数
                    DecimalFormat decimalFormat = new DecimalFormat("#.###");
                    items.set(2, decimalFormat.format(x));
                    items.set(3, decimalFormat.format(y));
                    items.set(4, decimalFormat.format(z));
                    String newLine = String.join(" ", items);
                    lines.set(i, newLine);
                }
            }
            Files.write(path, lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public XYZ convertMaxRes2CurrResCoords(XYZ ImageMaxRes, XYZ ImageCurRes, float x, float y, float z) {
        x /= (ImageMaxRes.x / ImageCurRes.x);
        y /= (ImageMaxRes.y / ImageCurRes.y);
        z /= (ImageMaxRes.z / ImageCurRes.z);
        return new XYZ(x, y, z);
    }

    public void convertCoorsInSwc(String swcPath, XYZ ImageMaxRes, XYZ ImageCurRes) {
        List<String> lines = null;
        Path path = null;
        try {
            path = Paths.get(swcPath);
            lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                if (!lines.get(i).contains("#")) {
                    List<String> items = Arrays.asList(lines.get(i).split(" "));
                    float x = Float.parseFloat(items.get(2));
                    x /= (ImageMaxRes.x / ImageCurRes.x);
                    float y = Float.parseFloat(items.get(3));
                    y /= (ImageMaxRes.y / ImageCurRes.y);
                    float z = Float.parseFloat(items.get(4));
                    z /= (ImageMaxRes.z / ImageCurRes.z);
                    // 使用 DecimalFormat 格式化结果并保留三位小数
                    DecimalFormat decimalFormat = new DecimalFormat("#.###");
                    items.set(2, decimalFormat.format(x));
                    items.set(3, decimalFormat.format(y));
                    items.set(4, decimalFormat.format(z));
                    String newLine = String.join(" ", items);
                    lines.set(i, newLine);
                }
            }
            Files.write(path, lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public XYZ convertCurRes2MaxResCoords(XYZ ImageMaxRes, XYZ ImageCurRes, float x, float y, float z) {
        x *= (ImageMaxRes.x / ImageCurRes.x);
        y *= (ImageMaxRes.y / ImageCurRes.y);
        z *= (ImageMaxRes.z / ImageCurRes.z);
        return new XYZ(x, y, z);
    }


}
