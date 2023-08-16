package com.zhy.springboot.superuserserver;

import com.zhy.springboot.superuserserver.config.GlobalConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan("com.zhy")
public class SuperUserServerApplication {

    public static void main(String[] args) {
        final TimeZone timeZone=TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(timeZone);
        SpringApplication.run(SuperUserServerApplication.class, args);
    }

}
