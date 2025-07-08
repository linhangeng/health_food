package com.example.exception;

/**
 * @ClassName BizEnums
 * @Author sheng.lin
 * @Date 2025/7/3
 * @Version 1.0
 * @修改记录
 **/
public enum BizEnums implements ExceptionEnums {

    /**
     *
     */

    CUSTOM_ERROR(10000001, "", ""),
    ;

    public int code;
    public String message;
    public String enMsg;

    BizEnums(int code, String message, String enMsg) {
        this.code = code;
        this.message = message;
        this.enMsg = enMsg;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
