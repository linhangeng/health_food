package com.example.handler;

import com.example.exception.WrappedException;
import com.example.protocol.ApiServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 统一异常处理
 *
 * @author huangfl
 * @date 2021-11-15
 */
/**
 * 全局统一异常处理器
 * 捕获所有控制器层抛出的异常，统一转换为 ApiServiceResponse 格式响应
 */
@Slf4j
@RestControllerAdvice // 作用于所有 @RestController 控制器
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常 WrappedException
     */
    @ExceptionHandler(WrappedException.class)
    public ApiServiceResponse<Void> handleWrappedException(WrappedException e) {
        // 记录业务异常日志（级别为 info，非错误）
        log.info("业务异常: {}", e.getMessage());
        // 构建自定义响应
        ApiServiceResponse<Void> response = new ApiServiceResponse<>();
        response.setCode(e.getErrCode());
        response.setMessage(e.getMessage());
        return response;
    }

    /**
     * 处理参数校验异常（如 @Valid 注解的参数校验失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiServiceResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 提取参数校验错误信息（如字段不能为空、格式错误等）
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", errorMsg);
        return ApiServiceResponse.fail(
                ApiServiceResponse.DEFAULT_ERR_CODE,
                "参数错误: " + errorMsg
        );
    }

    /**
     * 处理文件上传过大异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiServiceResponse<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("文件上传过大: {}", e.getMessage());
        return ApiServiceResponse.fail(
                ApiServiceResponse.DEFAULT_ERR_CODE,
                "文件大小超过限制，请上传更小的文件"
        );
    }

    /**
     * 处理 404 异常（接口不存在）
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiServiceResponse<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("接口不存在: {}", e.getRequestURL());
        return ApiServiceResponse.fail(
                404,
                "接口不存在: " + e.getRequestURL()
        );
    }

    /**
     * 处理所有未捕获的异常（兜底处理）
     */
    @ExceptionHandler(Exception.class)
    public ApiServiceResponse<Void> handleOtherExceptions(Exception e) {
        // 记录完整异常堆栈（错误级别，用于排查问题）
        log.error("未处理的异常", e);
        // 构建通用错误响应（避免暴露敏感信息）
        return ApiServiceResponse.fail(
                ApiServiceResponse.DEFAULT_ERR_CODE,
                ApiServiceResponse.DEFAULT_ERR_MSG // 使用默认消息："服务繁忙"
        );
    }

}
