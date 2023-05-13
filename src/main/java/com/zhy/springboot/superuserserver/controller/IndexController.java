package com.zhy.springboot.superuserserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhy
 * @Date 2023/5/10 11:54
 * @Description IndexController
 * @Since version-1.0
 */
@Slf4j
@RestController
public class IndexController {
    @GetMapping(value = {"/"})
    public String helloPage() {
        log.info("hello");
        return "hello";
    }
}
