package com.zhy.springboot.superuserserver.utils;

import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.concurrent.Semaphore;

class XYZ {
    public float x;
    public float y;
    public float z;
}

class BBox {
    public XYZ coord1;
    public XYZ coord2;
    public String obj;
    public String res;
}

/**
 * @Author zhy
 * @Date 2023/5/10 15:01
 * @Description 工具类
 * @Since version-1.0
 */
@Slf4j
public class Utils {
    @Autowired
    private static GlobalConfigs globalConfigs;
    private static Semaphore semaphore = new Semaphore(globalConfigs.getCropprocess());

    public static String consumeInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            System.out.println(s);
            sb.append(s);
        }
        return sb.toString();
    }

    public static String getBBImage(BBox bBox) {
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

    public static void preProcess() {

    }

    public static void postProcess() {

    }
}
