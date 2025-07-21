package com.example.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.function.BiFunction;

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
@Slf4j
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
    public static <T> void exportSmall(HttpServletResponse response,
                                       List<T> data,
                                       Class<T> clazz,
                                       String fileName,
                                       String sheetName) {
        try {
            setupResponse(response, fileName);
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .registerWriteHandler(setStyleStrategy()) // 注册样式策略
                    .registerWriteHandler(new SheetWriteHandler() {
                        @Override
                        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                            setColumnWidth(writeSheetHolder); // 设置列宽
                        }
                    })
                    .doWrite(data);
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            throw new RuntimeException("Excel 小数据导出失败", e);
        }
    }


    /**
     * 分批导出大量数据到 Excel（写入同一个 Sheet）
     *
     * @param response  HttpServletResponse
     * @param fileName  导出的文件名
     * @param clazz     导出数据类型的 class
     * @param sheetName sheet 名称
     * @param pageSize  每页查询条数
     * @param fetchFunc 分页查询函数：传入 pageNum 和 pageSize，返回数据列表
     * @param <T>       导出数据类型
     */
    public static <T> void exportMedium(
            HttpServletResponse response,
            String fileName,
            Class<T> clazz,
            String sheetName,
            int pageSize,
            BiFunction<Integer, Integer, List<T>> fetchFunc) {
        try {
            // 设置响应头
            setupResponse(response,fileName);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(setStyleStrategy()) // 注册样式策略
                    .registerWriteHandler(new SheetWriteHandler() {
                        @Override
                        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                            setColumnWidth(writeSheetHolder); // 设置列宽
                        }
                    })
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            int pageNum = 1;
            while (true) {
                List<T> dataList = fetchFunc.apply(pageNum, pageSize);
                if (dataList == null || dataList.isEmpty()) {
                    break;
                }
                excelWriter.write(dataList, writeSheet);
                pageNum++;
            }
            excelWriter.finish();
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            throw new RuntimeException("Excel 中数据导出失败", e);
        }
    }


    /**
     * 分批导出大量数据到 Excel（写入不一个 Sheet）
     * @param response
     * @param fileName
     * @param clazz
     * @param baseSheetName
     * @param pageSize
     * @param fetchFunc
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public static <T> void exportMediumToMultipleSheets(
            HttpServletResponse response,
            String fileName,
            Class<T> clazz,
            String baseSheetName,
            int pageSize,
            BiFunction<Integer, Integer, List<T>> fetchFunc) {
        try {
            // 设置响应头
            setupResponse(response, fileName);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz).build();
            int pageNum = 1;

            while (true) {
                List<T> dataList = fetchFunc.apply(pageNum, pageSize);
                if (dataList == null || dataList.isEmpty()) {
                    break;
                }
                // 每页一个 sheet，名称如：Sheet1，Sheet2 ...
                WriteSheet writeSheet = EasyExcel.writerSheet("Sheet" + pageNum).build();
                excelWriter.write(dataList, writeSheet);
                pageNum++;
            }
            excelWriter.finish();
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            throw new RuntimeException("Excel 中数据导出失败", e);
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
        headCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); // 浅蓝色表头
        headCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        WriteFont headFont = new WriteFont();
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBold(true);
        headCellStyle.setWriteFont(headFont);

        // 2. 设置内容样式
        WriteCellStyle contentCellStyle = new WriteCellStyle();
        contentCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER); // 水平居中
        contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);     // 垂直居中

        // 3. 合并样式策略
        return new HorizontalCellStyleStrategy(headCellStyle, contentCellStyle);
    }


    /**
     * 设置列宽为20
     * @param writeSheetHolder
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    private static void setColumnWidth(WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        int columnCount = 20; // 假设最多20列，可根据实际情况调整
        for (int i = 0; i < columnCount; i++) {
            sheet.setColumnWidth(i, 20 * 256); // Excel列宽单位是1/256个字符宽度
        }
    }

}
