package com.example.feign;

import com.example.exception.BizEnums;
import com.example.exception.WrappedException;
import com.example.model.dto.SysFileDTO;
import com.example.protocol.ApiServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName VideoFeign
 * @Author sheng.lin
 * @Date 2025/7/4
 * @Version 1.0
 * @修改记录
 **/
@FeignClient(value = "manage-service",
        fallbackFactory = FileFeign.ClientCallBackFactory.class)
public interface FileFeign {


    @PostMapping("/saveSysFile")
    ApiServiceResponse<Boolean> saveSysFile(@RequestBody SysFileDTO sysFileDto);


    @Component
    @Slf4j
    class ClientCallBackFactory implements FallbackFactory<FileFeign> {

        @Override
        public FileFeign create(Throwable cause) {
            log.error("内部调用错误：{}",cause.getMessage(),cause);
            throw new WrappedException(BizEnums.CUSTOM_ERROR, "服务繁忙");
        }
    }
}
