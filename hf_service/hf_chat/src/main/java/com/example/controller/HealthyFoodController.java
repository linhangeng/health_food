package com.example.controller;

import com.example.model.dto.HealthyFoodSearchDTO;
import com.example.protocol.ApiServiceResponse;
import com.example.service.HealthyFoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HealthyFoodController
 * @Author sheng.lin
 * @Date 2025/7/24
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@RestController
@RequestMapping("/healthyFood")
@Tag(name = "/healthyFood", description = "openApi聊天接口管理")
public class HealthyFoodController {

    @Resource
    HealthyFoodService healthyFoodService;

    @PostMapping("/search")
    @Operation(summary = "获取美食制作方法")
    public ApiServiceResponse<String> search(@RequestBody HealthyFoodSearchDTO healthyFoodSearchDto){
        return new ApiServiceResponse<>(healthyFoodService.search(healthyFoodSearchDto));
    }
}
