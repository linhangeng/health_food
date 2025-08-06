package com.example.strategy.excel;

import com.example.util.EasyExcelExportUtil;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/20 15:00
 */
public class SmallExcelExportStrategy<T> implements ExcelExportStrategy<T> {


    /**
     * 具体实现
     * @param
     * @Author sheng.lin
     * @Date 2025/7/20
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void execute(ExcelContext<T> excelContext) {
        EasyExcelExportUtil.exportSmall(
                excelContext.getHttpServletResponse(),
                excelContext.getDataList(),
                excelContext.getClazz(),
                excelContext.getFileName(),
                excelContext.getSheetName()
        );
    }
}
