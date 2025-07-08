package com.example.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName SysFileVO
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
public class SysFileVO {


    @TableId(type = IdType.AUTO)
    private Integer recId;

    @Schema(description = "上传位置")
    private String uploadSource;

    @Schema(description = "桶名")
    private String bucketName;

    @Schema(description = "文件状态")
    private String fileStatus;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件大小")
    private Integer fileSize;

    @Schema(description = "文件路径")
    private String fileUrl;

    @Schema(description = "创建人")
    private Integer creatorId;

    @Schema(description = "创建时间")
    private Long createTime;
}
