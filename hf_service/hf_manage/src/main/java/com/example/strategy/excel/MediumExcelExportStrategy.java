package com.example.strategy.excel;

import com.example.model.vo.SysFileVO;
import com.example.util.EasyExcelUtil;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/20 18:05
 */
public class MediumExcelExportStrategy<T> implements ExcelExportStrategy<T> {



    /**
     * 中数据量导出
     * @param excelContext
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void execute(ExcelContext<T> excelContext) {
        EasyExcelUtil.exportMedium(
                excelContext.getHttpServletResponse(),
                excelContext.getFileName(),
                excelContext.getClazz(),
                excelContext.getSheetName(),
                excelContext.getPageSize(),
                excelContext.getFetchFunc()
        );
    }
}
