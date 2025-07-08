package com.example.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @ClassName OpenAPIConfig
 * @Author sheng.lin
 * @Date 2025/7/3
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Configuration
public class OpenAPIConfig {


    /**
     * springDoc 接口文档配置
     * @param
     * @Author sheng.lin
     * @Date 2025/7/3
     * @return: io.swagger.v3.oas.models.OpenAPI
     * @Version  1.0
     * @修改记录
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("hf_manage")
                        .version("1.0.0")
                        .description("hf_manage API 文档")
                        .contact(new Contact().name("技术支持").email("support@example.com"))
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:7090").description("开发环境")
                ));
    }
}
