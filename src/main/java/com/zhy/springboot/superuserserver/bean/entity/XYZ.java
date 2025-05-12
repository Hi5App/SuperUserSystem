package com.zhy.springboot.superuserserver.bean.entity;

/**
 * @Author zhy
 * @Date 2023/6/27 16:05
 * @Description This is description of class
 * @Since version-1.0
 */
public class XYZ {
    public float x;
    public float y;
    public float z;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public XYZ() {
    }

    public XYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "XYZ{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

}
