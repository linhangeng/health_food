package com.example.exception;

import lombok.Data;

/**
 * @ClassName WrappedException
 * @Author sheng.lin
 * @Date 2025/7/3
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Data
public class WrappedException extends RuntimeException {
    private ExceptionEnums errEnum;
    private String errMsg;
    private Integer errCode;

    public WrappedException(ExceptionEnums openServiceErrEnum) {
        super(openServiceErrEnum.getMessage() + "[" + openServiceErrEnum.getCode() + "]");
        this.errEnum = openServiceErrEnum;
        this.errMsg = openServiceErrEnum.getMessage();
        this.errCode = openServiceErrEnum.getCode();
    }

    public WrappedException(ExceptionEnums openServiceErrEnum, String errMsg) {
        super(errMsg + "[" + openServiceErrEnum.getCode() + "]");
        this.errEnum = openServiceErrEnum;
        this.errMsg = errMsg;
        this.errCode = openServiceErrEnum.getCode();
    }

    public WrappedException(Integer errCode, String errMsg) {
        super(errMsg + "[" + errCode + "]");
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

}
