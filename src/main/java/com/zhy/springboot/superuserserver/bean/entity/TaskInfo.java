package com.zhy.springboot.superuserserver.bean.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhy
 * @Date 2024/1/5 14:54
 * @Description 用于保存每次task所需记录的一些必要信息
 * @Since version-1.0
 */
@Getter
public class TaskInfo {
    private String swcPath;
    private String baseDirPath;
    private final Map<String,String> curCoor2MaxCoorMap = new HashMap<>();

    public TaskInfo(String swcPath, String baseDirPath){
        this.swcPath = swcPath;
        this.baseDirPath = baseDirPath;
    }
    public void setSwcPath(String swcPath) {
        this.swcPath = swcPath;
    }

    public void setBaseDirPath(String baseDirPath) {
        this.baseDirPath = baseDirPath;
    }

}
