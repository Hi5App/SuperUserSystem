package com.zhy.springboot.superuserserver.bean.dto;

import com.zhy.springboot.superuserserver.bean.entity.FiberPredictedResult;
import lombok.Data;

import java.util.List;

/**
 * @Author zhy
 * @Date 2025/3/14 23:06
 * @Description This is description of class
 * @Since version-1.0
 */
@Data
public class DetectCrossingResData {
    private List<FiberPredictedResult> fiberPredictedResultList;
}
