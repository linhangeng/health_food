package com.example.strategy.excel;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/20 14:55
 */
@Data
public class ExcelContext<T> {

    private HttpServletResponse httpServletResponse;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * sheet名
     */
    private String sheetName;

    /**
     * 数据
     */
    private List<T> dataList;

    /**
     * 表头字段
     */
    private Class<T> clazz;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 分批函数
     */
    private BiFunction<Integer, Integer, List<T>> fetchFunc;

}