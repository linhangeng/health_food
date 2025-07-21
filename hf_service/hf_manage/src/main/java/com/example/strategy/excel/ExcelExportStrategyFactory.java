package com.example.strategy.excel;

import cn.hutool.core.util.ObjectUtil;
import com.example.enums.ExcelExportType;
import com.example.exception.BizEnums;
import com.example.exception.WrappedException;
import jakarta.annotation.PostConstruct;
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

//    @PostConstruct
//    public void init() {
//        strategyMap.put(ExcelExportType.SMALL, new SmallExcelExportStrategy<>());
//        strategyMap.put(ExcelExportType.MEDIUM, new MediumExcelExportStrategy<>());
//        strategyMap.put(ExcelExportType.LARGE, new LargeExcelExportStrategy<>());
//    }
    static {
        strategyMap.put(ExcelExportType.SMALL, new SmallExcelExportStrategy<>());
        strategyMap.put(ExcelExportType.MEDIUM, new MediumExcelExportStrategy<>());
        strategyMap.put(ExcelExportType.LARGE, new LargeExcelExportStrategy<>());
    }


    /**
     * 获取具体的工厂
     * @param dataSize
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: com.example.strategy.excel.ExcelExportStrategy<T>
     * @Version  1.0
     * @修改记录
     */
    //    @SuppressWarnings("unchecked") //用于抑制编译器产生的 “未经检查的类型转换” 警告。在泛型代码中，当你进行类型转换但编译器无法确认类型安全性时，就会发出这类警告
    public static <T> ExcelExportStrategy<T> getStrategy(Long dataSize) {
        ExcelExportType excelExportType = ExcelExportType.getExcelExportTypeBySize(dataSize);
        return (ExcelExportStrategy<T>) strategyMap.get(excelExportType);
    }

}
