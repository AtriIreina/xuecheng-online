package com.xuecheng;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages={"com.xuecheng.content.feignclient"})
@EnableSwagger2Doc
@SpringBootApplication //注解默认扫描启动类所在包及其子包
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ContentApplication.class);
//        app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
        app.run(args);
    }

}
