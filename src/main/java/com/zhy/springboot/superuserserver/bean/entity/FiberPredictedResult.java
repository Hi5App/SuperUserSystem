package com.zhy.springboot.superuserserver.bean.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author zhy
 * @Date 2025/3/14 23:05
 * @Description This is description of class
 * @Since version-1.0
 */
@Data
public class FiberPredictedResult {
    private List<FiberCoorInfo> fiberCoorInfoList;
    private Integer y_pred;
}
