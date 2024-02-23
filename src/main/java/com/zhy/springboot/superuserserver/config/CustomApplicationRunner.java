package com.zhy.springboot.superuserserver.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Set;

/**
 * 自定义{@link ApplicationRunner}实现
 *
 * @author zhy
 */
@Slf4j
@Component
public class CustomApplicationRunner implements ApplicationRunner {

    @Autowired
    GlobalConfigs globalConfigs;
    /**
     * 在SpringApplication启动完成后,该方法会被回调,可以在这里做一些操作
     *
     * @param args 启动Java Application时设置的程序参数
     * @throws Exception 异常
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("enter CustomApplicationRunner");

        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        String tipDir = String.join(File.separator, globalConfigs.getSavePathForPredict(), "tip");
        String crossingDir = String.join(File.separator, globalConfigs.getSavePathForPredict(), "crossing");
        String branchingDir = String.join(File.separator, globalConfigs.getSavePathForPredict(), "branching");
        File dir = new File(tipDir);
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
            }
        }
       dir = new File(crossingDir);
        flag = false;
        if (!dir.exists()) {
            flag = dir.mkdirs();
            try {
                Files.setPosixFilePermissions(Paths.get(String.valueOf(dir)), perms);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!flag) {
                log.info("cannot create dir!");
            }
        }
        dir = new File(branchingDir);
        flag = false;
        if (!dir.exists()) {
            flag = dir.mkdirs();
            try {
                Files.setPosixFilePermissions(Paths.get(String.valueOf(dir)), perms);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!flag) {
                log.info("cannot create dir!");
            }
        }
    }

}
