package com.demo.sys.config.web;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tokenPars = new ParameterBuilder();
        tokenPars.name("Authorization")
                .description("用户登录认证后的token令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build(); //header中的ticket参数非必填，传空也可以
        ParameterBuilder serviceNamePars = new ParameterBuilder();
        serviceNamePars.name("serviceName")
                .description("服务名称")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        ParameterBuilder methodNamePars = new ParameterBuilder();
        methodNamePars.name("methodName")
                .description("服务的地址")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(tokenPars.build());
        pars.add(serviceNamePars.build());
        pars.add(methodNamePars.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo.sys.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo())
                .globalOperationParameters(pars)
                .enable(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("翊廷平台系统- API")
                .description("翊廷平台系统-API接口服务")
                .version("1.0.0")
                .contact(new Contact("西安研发中心", "http://www.demo.cn", "shyt@demo.cn"))
                .license("The License Of demo Internet Business Department")
                .licenseUrl("http://www.demo.cn")
                .build();
    }
}