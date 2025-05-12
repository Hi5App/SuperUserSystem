package com.zhy.springboot.superuserserver.bean.entity;

import lombok.Data;

/**
 * @Author zhy
 * @Date 2025/2/24 15:46
 * @Description This is description of class
 * @Since version-1.0
 */
@Data
public class CoorPredictedResult {
    private XYZ maxResCoor;
    private String storeDirName;
    private Integer y_pred;
}
