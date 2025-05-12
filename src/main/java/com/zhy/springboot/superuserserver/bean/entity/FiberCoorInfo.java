package com.zhy.springboot.superuserserver.bean.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author zhy
 * @Date 2025/3/14 23:14
 * @Description This is description of class
 * @Since version-1.0
 */
@Data
public class FiberCoorInfo {
    private XYZ coor;
    private List<XYZ> fiberCoorList;
}
