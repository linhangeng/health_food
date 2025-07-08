package com.example.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.enums.EnvEnum;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName OssConfig
 * @Author sheng.lin
 * @Date 2025/7/2
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssProperties {

    /**
     * 外网访问
     */
    private String endpointExtNet;

    /**
     * 内网访问
     */
    private String endpointInternal;

    /**
     * keyId
     */
    private String accessKeyId;

    /**
     * keySecret
     */
    private String accessKeySecret;

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 环境env
     */
    private String activeProfile;

}
