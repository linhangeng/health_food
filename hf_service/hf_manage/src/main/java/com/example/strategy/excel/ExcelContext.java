package com.example.strategy.excel;

import com.example.service.SysFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/20 14:55
 */
@Data
public class ExcelContext {

    private SysFileService sysFileService;

    private HttpServletResponse httpServletResponse;

}