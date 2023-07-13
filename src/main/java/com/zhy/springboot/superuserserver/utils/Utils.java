package com.zhy.springboot.superuserserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

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

    public void setSemaphore() {
        if(semaphore==null){
            semaphore=new Semaphore(globalConfigs.getCropprocess());
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

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        return savefilePath;
    }

    public void preProcess(String baseDir, String obj, String res, List<XYZ> coors) {
        log.info("enter preProcess...");
        for (XYZ coor : coors) {
            int[] patchSize = globalConfigs.getPatchSize();
            String xmin = String.format("%06d", (int) coor.x - patchSize[0] / 2);
            String ymin = String.format("%06d", (int) coor.y - patchSize[0] / 2);
            String zmin = String.format("%06d", (int) coor.z - patchSize[0] / 2);
            String xmax = String.format("%06d", (int) coor.x + patchSize[0] / 2);
            String ymax = String.format("%06d", (int) coor.y + patchSize[0] / 2);
            String zmax = String.format("%06d", (int) coor.z + patchSize[0] / 2);
            String fileName = xmin + "_" + ymin + "_" + zmin + "_" + xmax + "_" + ymax + "_" + zmax;
            String dirPath = String.join(File.separator, baseDir, fileName);
            File eachDir = new File(dirPath);
            if(!eachDir.isDirectory()){
                eachDir.mkdirs();
            }

            XYZ pa1=new XYZ(coor.x - patchSize[0] / 2, coor.y - patchSize[0] / 2, coor.z - patchSize[0] / 2 );
            XYZ pa2=new XYZ(coor.x + patchSize[0] / 2, coor.y + patchSize[0] / 2, coor.z + patchSize[0] / 2 );
            Map<String, Object> bbMap = new HashMap<>();
            Map<String, Object> userMap= new HashMap<>();
            Map<String, Object> bBoxMap=new HashMap<>();
            bbMap.put("obj", obj);
            bbMap.put("res", res);
            bbMap.put("pa1", pa1);
            bbMap.put("pa2", pa2);
            userMap.put("name", "zackzhy");
            userMap.put("passwd", "123456");
            bBoxMap.put("user", userMap);
            bBoxMap.put("bb", bbMap);
            JSONObject bBox = new JSONObject(bBoxMap);

            byte[] bytes = okHttpUtil.postForFile(globalConfigs.getUrlForGetBBimage(), JSON.toJSONString(bBox));
            if (bytes == null) {
                log.info("preProcess error!");
                return;
            }
            String tifName = "optical.tif";
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
    }

    public void postProcess(String swcPath, String baseDir) {
        log.info("enter postProcess...");
        File swcFile=new File(swcPath);
        swcFile.delete();
        File dirPath=new File(baseDir);
        deleteFile(dirPath);
     }

    public void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            log.info("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
}
