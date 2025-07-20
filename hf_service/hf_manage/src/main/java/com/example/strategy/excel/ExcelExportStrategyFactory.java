package com.example.strategy.excel;

import cn.hutool.core.util.ObjectUtil;
import com.example.enums.ExcelExportType;
import com.example.exception.BizEnums;
import com.example.exception.WrappedException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/20 15:11
 */
public class ExcelExportStrategyFactory {


    private static final Map<ExcelExportType, ExcelExportStrategy<?>> strategyMap = new HashMap<>();
    static {
        strategyMap.put(ExcelExportType.SMALL, new SmallExcelExportStrategy<>());
    }



//    @SuppressWarnings("unchecked") //用于抑制编译器产生的 “未经检查的类型转换” 警告。在泛型代码中，当你进行类型转换但编译器无法确认类型安全性时，就会发出这类警告
    public static <T> ExcelExportStrategy<T> getStrategy(Long dataSize) {
        ExcelExportType excelExportType = ExcelExportType.getExcelExportTypeBySize(dataSize);
        return (ExcelExportStrategy<T>) strategyMap.get(excelExportType);
    }

}
