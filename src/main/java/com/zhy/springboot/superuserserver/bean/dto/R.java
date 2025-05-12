package com.zhy.springboot.superuserserver.bean.dto;

/**
 * @Author zhy
 * @Date 2023/5/10 17:53
 * @Description This is description of class
 * @Since version-1.0
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class R {
    private String code;
    private Object data;
    private String msg;
}
