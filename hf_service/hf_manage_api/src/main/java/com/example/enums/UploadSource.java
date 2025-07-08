package com.example.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @ClassName UploadSource
 * @Author sheng.lin
 * @Date 2025/7/4
 * @Version 1.0
 * @修改记录
 **/
@Getter
public enum UploadSource {

    ALIBABA_OSS("OSS","阿里云OSS")

    ;

    String code;
    String description;

    UploadSource(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
