package com.zhy.springboot.superuserserver.bean.dto;

import com.zhy.springboot.superuserserver.bean.entity.XYZ;
import lombok.Getter;

import java.util.List;

/**
 * @Author zhy
 * @Date 2024/2/23 13:49
 * @Description BranchingInfo类
 * @Since version-1.0
 */
@Getter
public class BranchingInfo {
    // 图像名相对路径
    private String objRelaventPath;
    // 图像名
    private String obj;
    // 中心坐标列表
    private List<XYZ> coors;
    //swc文件名
    private String swcName;

    // swc文件
    // private MultipartFile swcFile;

    // public MultipartFile getSwcFile() {
    //     return swcFile;
    // }
    //
    // public void setSwcFile(MultipartFile swcFile) {
    //     this.swcFile = swcFile;
    // }

    public BranchingInfo() {
    }

    public BranchingInfo(String obj, List<XYZ> coors) {
        this.obj = obj;
        this.coors = coors;
    }

    public void setObjRelaventPath(String objRelaventPath) {
        this.objRelaventPath = objRelaventPath;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public void setCoors(List<XYZ> coors) {
        this.coors = coors;
    }

    public void setSwcName(String swcName) {
        this.swcName = swcName;
    }

    @Override
    public String toString() {
        return "MissingInfo{" +
                "obj='" + obj + '\'' +
                ", coors=" + coors +
                '}';
    }
}
