package com.zhy.springboot.superuserserver.bean;

import com.zhy.springboot.superuserserver.utils.XYZ;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author zhy
 * @Date 2023/6/27 15:49
 * @Description This is description of class
 * @Since version-1.0
 */
public class MissingInfo {
    // 图像名
    private String obj;
    // 图像最高分辨率
    private String res;
    // 中心坐标列表
    private List<XYZ> coors;
    // swc文件
    // private MultipartFile swcFile;

    // public MultipartFile getSwcFile() {
    //     return swcFile;
    // }
    //
    // public void setSwcFile(MultipartFile swcFile) {
    //     this.swcFile = swcFile;
    // }

    public MissingInfo() {
    }

    public MissingInfo(String obj, List<XYZ> coors, String res) {
        this.obj = obj;
        this.res = res;
        this.coors = coors;
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

    public List<XYZ> getCoors() {
        return coors;
    }

    public void setCoors(List<XYZ> coors) {
        this.coors = coors;
    }

    @Override
    public String toString() {
        return "MissingInfo{" +
                "obj='" + obj + '\'' +
                ", coors=" + coors +
                ", res=" + res +
                '}';
    }
}
