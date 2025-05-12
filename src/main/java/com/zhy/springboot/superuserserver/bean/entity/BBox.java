package com.zhy.springboot.superuserserver.bean.entity;

/**
 * @Author zhy
 * @Date 2023/6/27 16:04
 * @Description This is description of class
 * @Since version-1.0
 */

public class BBox {
    public XYZ coord1;
    public XYZ coord2;
    public String obj;
    public String res;

    @Override
    public String toString() {
        return "BBox{" +
                "coord1=" + coord1 +
                ", coord2=" + coord2 +
                ", obj='" + obj + '\'' +
                ", res='" + res + '\'' +
                '}';
    }
}
