package com.example.enums;

import lombok.Getter;

/**
 * @ClassName EnvEnum
 * @Author sheng.lin
 * @Date 2025/7/2
 * @Version 1.0
 * @修改记录
 **/
@Getter
public enum EnvEnum {

    DEV_ENV("dev","开发/测试环境"),
    PRO_ENV("pro","生产环境")



    ;
    private String code;
    private String description;

    EnvEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
