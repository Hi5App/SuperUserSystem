package com.zhy.springboot.superuserserver.bean.dto;

import com.zhy.springboot.superuserserver.bean.entity.CoorPredictedResult;
import lombok.Data;

import java.util.List;

/**
 * @Author zhy
 * @Date 2025/2/24 15:43
 * @Description This is description of class
 * @Since version-1.0
 */
@Data
public class DetectMissingResData {
    private String relPath;
    private List<CoorPredictedResult> coorPredictedResultList;
}
