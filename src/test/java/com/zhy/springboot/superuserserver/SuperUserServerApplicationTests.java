package com.zhy.springboot.superuserserver;

import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SuperUserServerApplicationTests {

    @Autowired
    GlobalConfigs globalConfigs;

    @Test
    void contextLoads() {
        System.out.println(globalConfigs);
    }

}
