package com.example.feignImpl;

import com.example.feign.FileFeign;
import com.example.protocol.ApiServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/fileFeign")
@Tag(name="fileFeign",description = "file外部调用feign管理")
public class FileFeignImpl implements FileFeign {


    @PostMapping("/search")
    @Operation(summary = "外部查询音频文件接口")
    public ApiServiceResponse<List<String>> search(){
        return new ApiServiceResponse<>();
    }

}
