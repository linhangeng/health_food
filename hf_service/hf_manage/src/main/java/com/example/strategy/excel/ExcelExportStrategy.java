package com.example.strategy.excel;


import com.example.model.vo.SysFileVO;

public interface ExcelExportStrategy<T> {

    void execute(ExcelContext<T> excelContext);


}
