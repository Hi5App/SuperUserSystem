package com.zhy.springboot.superuserserver;

import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import com.zhy.springboot.superuserserver.utils.Utils;
import com.zhy.springboot.superuserserver.utils.XYZ;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SuperUserServerApplicationTests {

    @Autowired
    GlobalConfigs globalConfigs;

    @Autowired
    Utils utils;

    @Test
    void contextLoads() {
        System.out.println(globalConfigs);
        System.out.println(utils);
    }

    @Test
    void testCropImage(){

    }

    @Test
    void testResXYZ2String(){
        XYZ res=new XYZ(43234.0f, 12942.0f, 13435.0f);
        System.out.println(utils.transResXYZ2String(res));
    }
}
