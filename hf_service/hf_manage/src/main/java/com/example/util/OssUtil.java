package com.example.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.config.OssProperties;
import com.example.enums.EnvEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @ClassName OssUtil
 * @Author sheng.lin
 * @Date 2025/7/2
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Component
@Slf4j
public class OssUtil {

    @Resource
    OssProperties ossProperties;

    private OSS ossClient;

    @PostConstruct
    public void init() {
        String endpoint = getEndpointByEnv(ossProperties.getActiveProfile());
        ossClient = new OSSClientBuilder().build(endpoint, ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
    }


    /**
     * 上传文件：音频 视频 文件等等
     * @param file 待上传的文件
     * @param bucketName 存储桶名称
     * @param objectName 对象名称（路径+文件名）
     * @Author sheng.lin
     * @Date 2025/7/3
     * @return: java.lang.String
     * @Version  1.0
     * @修改记录
     */
    public String uploadFileByMul(MultipartFile file, String bucketName, String objectName,boolean isTimeRead) {
        try (InputStream inputStream = file.getInputStream()) {
            ossClient.putObject(bucketName, objectName, inputStream);
            return isTimeRead
                    ? generatePublicUrl(bucketName, objectName)
                    : generatePrivateUrl(bucketName, objectName);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败", e);
        }
    }



    /**
     * 生成公共读Bucket的文件URL
     * @param bucketName
     * @param objectName
     * @Author sheng.lin
     * @Date 2025/7/3
     * @return: java.lang.String
     * @Version  1.0
     * @修改记录
     */
    private String generatePublicUrl(String bucketName, String objectName) {
        // 构造文件的 URL，假设文件的 URL 格式为：<endpoint>/<bucket-name>/<object-name>
        return "https://" + bucketName + "." + ossProperties.getEndpointExtNet() + "/" + objectName;
    }


    /**
     * 生成私有Bucket的临时访问URL
     * @param bucketName
     * @param objectName
     * @Author sheng.lin
     * @Date 2025/7/3
     * @return: java.lang.String
     * @Version  1.0
     * @修改记录
     */
    private String generatePrivateUrl(String bucketName, String objectName) {
        // 假设生成 URL 的时间为T，则失效时间为T + 1小时。例如
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
        return url.toString();
    }


    /**
     * 根据不同环境选择不同网络
     * @param env
     * @Author sheng.lin
     * @Date 2025/7/2
     * @return: java.lang.String
     * @Version  1.0
     * @修改记录
     */
    private String getEndpointByEnv(String env) {
        if (EnvEnum.DEV_ENV.getCode().equals(env)) {
            // 生产环境用私网
            return ossProperties.getEndpointExtNet();
        } else {
            // 开发/测试用公网
            return ossProperties.getEndpointInternal();
        }
    }
}
