package com.example.feignImpl;

import com.example.feign.FileFeign;
import com.example.model.dto.SysFileDTO;
import com.example.protocol.ApiServiceResponse;
import com.example.service.SysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName VideoFeignImpl
 * @Author sheng.lin
 * @Date 2025/7/4
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@RestController
@Tag(name="fileFeign",description = "file外部调用feign管理")
public class FileFeignImpl implements FileFeign {

    @Resource
    SysFileService sysFileService;

    @PostMapping("/saveSysFile")
    @Operation(summary = "外部查询文件存储接口")
    public ApiServiceResponse<Boolean> saveSysFile(@RequestBody SysFileDTO sysFileDto) {
        return new ApiServiceResponse<>(sysFileService.saveSysFile(sysFileDto));
    }
}
