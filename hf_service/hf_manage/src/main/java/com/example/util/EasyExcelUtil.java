package com.example.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @ClassName EasyExcelUtil
 * @Author sheng.lin
 * @Date 2025/7/18
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
public class EasyExcelUtil {


    /**
     * 小批量导出 小于1w
     *
     * @param response
     * @param data
     * @param clazz
     * @param fileName
     * @param sheetName
     * @Author sheng.lin
     * @Date 2025/7/20
     * @return: void
     * @Version 1.0
     * @修改记录
     */
    public static <T> void exportSmall(HttpServletResponse response, List<T> data, Class<T> clazz, String fileName, String sheetName) {
        try {
            setupResponse(response, fileName);
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .registerWriteHandler(setStyleStrategy())
                    .doWrite(data);
        } catch (IOException e) {
            throw new RuntimeException("Excel 小数据导出失败", e);
        }
    }


    /**
     * 响应头设置
     *
     * @param response
     * @param fileName
     * @Author sheng.lin
     * @Date 2025/7/20
     * @return: void
     * @Version 1.0
     * @修改记录
     */
    private static void setupResponse(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }


    /**
     * 设置样式
     *
     * @param
     * @Author sheng.lin
     * @Date 2025/7/20
     * @return: com.alibaba.excel.write.style.HorizontalCellStyleStrategy
     * @Version 1.0
     * @修改记录
     */
    private static HorizontalCellStyleStrategy setStyleStrategy() {
        // 1. 设置表头样式
        WriteCellStyle headCellStyle = new WriteCellStyle();
        headCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); // 表头背景色
        headCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        WriteFont headFont = new WriteFont();
        headFont.setFontHeightInPoints((short) 12); // 表头字体大小
        headFont.setBold(true); // 表头字体加粗
        headCellStyle.setWriteFont(headFont);
        // 2. 设置内容样式
        WriteCellStyle contentCellStyle = new WriteCellStyle();
        contentCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER); // 内容水平居中
        contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 内容垂直居中
        // 3. 合并样式策略
        HorizontalCellStyleStrategy styleStrategy =
                new HorizontalCellStyleStrategy(headCellStyle, contentCellStyle);
        return styleStrategy;


    }

}
