package com.example.enums;

import lombok.Getter;

/**
 * @ClassName SysFileEnum
 * @Author sheng.lin
 * @Date 2025/7/4
 * @Version 1.0
 * @修改记录
 **/
@Getter
public enum SysFileStatusEnum {

    UPLOADED("UPLOADED","上传成功"),
    FAILED("FAILED","上传失败"),
    DELETED("DELETED","删除"),
    ;


    String code;
    String description;

    SysFileStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
