package com.example.enums;

import lombok.Getter;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/20 15:49
 */
@Getter
public enum ExcelExportType {

    SMALL("SMALL", 10000L),
    MEDIUM("MEDIUM", 500000L),
    LARGE("LARGE", 1000000L),


    ;
    String code;
    Long size;

    ExcelExportType(String code, Long size) {
        this.code = code;
        this.size = size;
    }


    /**
     * 获取类型
     * @param dataSize
     * @Author sheng.lin
     * @Date 2025/7/20
     * @return: com.example.strategy.excel.ExcelExportStrategyFactory.ExcelExportType
     * @Version  1.0
     * @修改记录
     */
    public static ExcelExportType getExcelExportTypeBySize(Long dataSize) {
        if (dataSize == null || dataSize <= 0) {
            return SMALL;
        }
        // 按阈值从大到小排序，优先匹配最大阈值
        for (ExcelExportType type : ExcelExportType.values()) {
            if (dataSize <= type.getSize()) {
                return type;
            }
        }
        return LARGE;
    }
}
