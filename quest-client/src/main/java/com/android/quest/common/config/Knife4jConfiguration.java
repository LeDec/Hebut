package com.android.quest.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Lidong
 * 2021-10-25
 */
@Configuration

public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("")
                        .description("# 学生成绩信息管理系统")
                        .termsOfServiceUrl("http://127.0.0.1:8081")
                        .contact(new Contact("Hebut", "", "99@.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("测试")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.android.quest.project"))
                .paths(PathSelectors.any())
                .build();
    }
}
