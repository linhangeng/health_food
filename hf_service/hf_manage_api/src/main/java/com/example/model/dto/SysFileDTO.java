package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName SysFileDTO
 * @Author sheng.lin
 * @Date 2025/7/4
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Data
public class SysFileDTO {


    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "上传位置")
    private String uploadSource;

    @Schema(description = "桶名")
    private String bucketName;

    @Schema(description = "文件状态")
    private String fileStatus;


}
