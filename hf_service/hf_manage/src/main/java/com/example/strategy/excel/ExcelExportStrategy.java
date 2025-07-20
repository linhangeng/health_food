package com.example.strategy.excel;




public interface ExcelExportStrategy<T> {

    void execute(ExcelContext excelContext);


}
