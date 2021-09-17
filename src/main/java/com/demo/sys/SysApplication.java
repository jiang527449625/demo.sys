package com.demo.sys;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients
@EnableScheduling
@MapperScan("com.demo.sys.dao")
@Slf4j
public class SysApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysApplication.class, args);
        log.info("服务启动完成！");
    }

}
