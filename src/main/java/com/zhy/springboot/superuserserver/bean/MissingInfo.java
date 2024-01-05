package com.zhy.springboot.superuserserver.bean;

import com.zhy.springboot.superuserserver.utils.XYZ;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author zhy
 * @Date 2023/6/27 15:49
 * @Description This is description of class
 * @Since version-1.0
 */
@Getter
public class MissingInfo {
    // 图像名
    private String obj;
    // 中心坐标列表
    private List<XYZ> coors;
    // swcNameWithNoSuffix 作为标识
    private String swcNameWithNoSuffix;
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

    public MissingInfo(String obj, List<XYZ> coors, String swcNameWithNoSuffix) {
        this.obj = obj;
        this.coors = coors;
        this.swcNameWithNoSuffix = swcNameWithNoSuffix;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public void setCoors(List<XYZ> coors) {
        this.coors = coors;
    }

    public void setswcNameWithNoSuffix(String swcNameWithNoSuffix) {
        this.swcNameWithNoSuffix = swcNameWithNoSuffix;
    }

    @Override
    public String toString() {
        return "MissingInfo{" +
                "obj='" + obj + '\'' +
                ", coors=" + coors +
                ", swcNameWithNoSuffix='" + swcNameWithNoSuffix + '\'' +
                '}';
    }
}
