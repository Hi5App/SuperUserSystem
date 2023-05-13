package com.zhy.springboot.superuserserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhy.springboot.superuserserver.service.DetectService;
import com.zhy.springboot.superuserserver.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zhy
 * @Date 2023/5/10 17:11
 * @Description DetectController
 * @Since version-1.0
 */
@Slf4j
@RestController
@RequestMapping("/detect")
public class DetectController {
    @Autowired
    DetectService detectService;

    @PostMapping(value = {"/crossing"})
    public R detectCrossing(@RequestBody JSONObject jsonParam) {
        detectService.detectCrossing();

        R r=new R();
        return r;
     }

    @PostMapping(value = {"/missing"})
    public R detectMissing(@RequestBody JSONObject jsonParam) {
        detectService.detectMissing();

        R r=new R();
        return r;
    }
}
