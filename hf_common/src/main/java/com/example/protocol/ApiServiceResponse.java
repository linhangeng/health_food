package com.example.protocol;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName ApiServiceResponse
 * @Author sheng.lin
 * @Date 2025/7/2
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Data
public class ApiServiceResponse<T> {

    /**
     * 返回范型
     */
    T response;

    /**
     * 返回码
     */
    int code = 0;

    /**
     * 返回消息
     */
    String message = "SUCESS";

    public static final int DEFAULT_ERR_CODE = 500;
    public static final String DEFAULT_ERR_MSG = "服务繁忙";

    public ApiServiceResponse() {
    }

    public ApiServiceResponse(T response) {
        this.response = response;
    }

    public ApiServiceResponse(T response, Map<String, Boolean> permission) {
        this.response = response;
    }


    public static ApiServiceResponse getCallBackServiceResponse() {
        ApiServiceResponse serviceResponse = new ApiServiceResponse();
        serviceResponse.setCode(DEFAULT_ERR_CODE);
        serviceResponse.setMessage(DEFAULT_ERR_MSG);
        return serviceResponse;
    }


    public static ApiServiceResponse success(Object data) {
        ApiServiceResponse result = new ApiServiceResponse();
        result.setResponse(data);
        return result;
    }


    public static ApiServiceResponse success() {
        ApiServiceResponse result = new ApiServiceResponse();
        return result;
    }


    public static ApiServiceResponse fail(Integer code, String message) {
        ApiServiceResponse result = new ApiServiceResponse();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

}
