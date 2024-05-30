package com.zhy.springboot.superuserserver.bean;

import com.zhy.springboot.superuserserver.utils.PointInfo;
import com.zhy.springboot.superuserserver.utils.XYZ;
import lombok.Getter;

import java.util.List;

/**
 * @Author zhy
 * @Date 2023/6/27 15:49
 * @Description This is description of class
 * @Since version-1.0
 */
@Getter
public class CrossingInfo {
    // 图像名相对路径
    private String objRelaventPath;
    // 图像名
    private String obj;
    // crossing结构相关信息
    private List<List<PointInfo>> infos;
    // swcNameWithNoSuffix作为标识
    private String swcNameWithNoSuffix;

    public CrossingInfo() {
    }

    public void setObjRelaventPath(String objRelaventPath) {
        this.objRelaventPath = objRelaventPath;
    }

    public CrossingInfo(String obj, List<List<PointInfo>> infos, String swcNameWithNoSuffix, String objRelaventPath) {
        this.obj = obj;
        this.infos = infos;
        this.swcNameWithNoSuffix = swcNameWithNoSuffix;
        this.objRelaventPath = objRelaventPath;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public void setInfos(List<List<PointInfo>> infos) {
        this.infos = infos;
    }

    public void setswcNameWithNoSuffix(String swcNameWithNoSuffix) {
        this.swcNameWithNoSuffix = swcNameWithNoSuffix;
    }

    @Override
    public String toString() {
        return "CrossingInfo{" +
                "obj='" + obj + '\'' +
                ", infos=" + infos +
                ", swcNameWithNoSuffix='" + swcNameWithNoSuffix + '\'' +
                '}';
    }
}
