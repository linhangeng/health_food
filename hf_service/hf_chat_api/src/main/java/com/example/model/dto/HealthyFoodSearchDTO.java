package com.example.model.dto;

import lombok.Data;

/**
 * @ClassName HealthyFoodSearchDTO
 * @Author sheng.lin
 * @Date 2025/7/24
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Data
public class HealthyFoodSearchDTO {

    /**
     * 提问内容
     */
    private String content;

    /**
     * 是否需要视频
     */
    private Boolean isMakeVideo = false;


}
