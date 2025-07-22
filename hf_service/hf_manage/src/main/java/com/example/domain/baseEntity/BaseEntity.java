package com.example.domain.baseEntity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName BaseEntity
 * @Author sheng.lin
 * @Date 2025/7/22
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Data
public abstract class BaseEntity {


    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer creatorId;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}

