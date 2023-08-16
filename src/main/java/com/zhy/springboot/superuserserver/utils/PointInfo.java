package com.zhy.springboot.superuserserver.utils;

import java.util.List;

/**
 * @Author zhy
 * @Date 2023/7/19 15:05
 * @Description This is description of class
 * @Since version-1.0
 */
public class PointInfo {
    public float x;
    public float y;
    public float z;
    public List<XYZ> parentsCoors;
    public List<XYZ> offspringsCoors;

    public PointInfo() {
    }

    public PointInfo(float x, float y, float z, List<XYZ> parentsCoors, List<XYZ> offspringsCoors) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parentsCoors = parentsCoors;
        this.offspringsCoors = offspringsCoors;
    }

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

    public List<XYZ> getParentsCoors() {
        return parentsCoors;
    }

    public void setParentsCoors(List<XYZ> parentsCoors) {
        this.parentsCoors = parentsCoors;
    }

    public List<XYZ> getOffspringsCoors() {
        return offspringsCoors;
    }

    public void setOffspringsCoors(List<XYZ> offspringsCoors) {
        this.offspringsCoors = offspringsCoors;
    }

    @Override
    public String toString() {
        return "PointInfo{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", parentsCoors=" + parentsCoors +
                ", offspringsCoors=" + offspringsCoors +
                '}';
    }
}
