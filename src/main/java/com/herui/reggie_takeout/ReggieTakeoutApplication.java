package com.herui.reggie_takeout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieTakeoutApplication {

    public static void main(String[] args) {

        SpringApplication.run(ReggieTakeoutApplication.class, args);
        log.info("The project of ReggieTakeout run successfully!");

    }
}
