package com.example.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/20 17:17
 */
@Data
public class SysFileVO {

//    @ExcelIgnore
    @ExcelProperty("id")
    private Integer recId;

    @ExcelProperty("上传位置")
    private String uploadSource;

    @ExcelProperty("远程名称")
    private String uploadName;

    @ExcelProperty("桶名")
    private String bucketName;

    @ExcelProperty("文件状态")
    private String fileStatus;

    @ExcelProperty("文件名")
    private String fileName;

    @ExcelProperty("文件大小")
    private Integer fileSize;

    @ExcelProperty("文件路径")
    private String fileUrl;

    @ExcelProperty("创建人")
    private Integer creatorId;

    @ExcelProperty("创建时间")
    private String createTime;
}
