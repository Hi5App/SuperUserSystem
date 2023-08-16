package com.zhy.springboot.superuserserver.bean;

import com.zhy.springboot.superuserserver.utils.PointInfo;
import com.zhy.springboot.superuserserver.utils.XYZ;

import java.util.List;

/**
 * @Author zhy
 * @Date 2023/6/27 15:49
 * @Description This is description of class
 * @Since version-1.0
 */
public class CrossingInfo {
    // 图像名
    private String obj;
    // 图像最高分辨率
    private String res;
    // crossing结构相关信息
    private List<List<PointInfo>> infos;

    public CrossingInfo() {
    }

    public CrossingInfo(String obj, String res, List<List<PointInfo>> infos) {
        this.obj = obj;
        this.res = res;
        this.infos = infos;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<List<PointInfo>> getInfos() {
        return infos;
    }

    public void setInfos(List<List<PointInfo>> infos) {
        this.infos = infos;
    }

    @Override
    public String toString() {
        return "CrossingInfo{" +
                "obj='" + obj + '\'' +
                ", res='" + res + '\'' +
                ", infos=" + infos +
                '}';
    }
}
