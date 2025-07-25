package com.example.util;

import com.convertapi.client.Config;
import com.convertapi.client.ConversionResult;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;
import com.example.exception.BizEnums;
import com.example.exception.WrappedException;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName ConvertApiUtil
 * @Author sheng.lin
 * @Date 2025/7/24
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Slf4j
public class ConvertAPIUtil {

    // 生产：Jj9AwYbZemPx1PUOFQyw17szgGV6zKG6
    // 沙盒：01ZxeoVIqejIYeb3ZAIr99npeC914p0T
    private static final String CONVERT_API_SECRET = "CWSNdSAsXKKghlpG4zJDxzRJUx5p8zKc";

    public static String convertToPdf(String mdUrl, String fileName) {
        String pdfUrl = "";
        try {
            Config.setDefaultSecret(CONVERT_API_SECRET);
            // 执行转换（md -> pdf）
            CompletableFuture<ConversionResult> future = ConvertApi.convert("md", "pdf",
                    new Param("File", mdUrl),
                    new Param("FileName", fileName),
                    new Param("StoreFile", "true")
            );
            // 获取下载链接
            // 等待异步任务完成
            ConversionResult result = future.get();
            pdfUrl = result.getFile(0).getUrl();
        } catch (Exception e) {
            log.error("md转pdf错误！", e);
            throw new WrappedException(BizEnums.CUSTOM_ERROR, "md转pdf错误");
        }
        return pdfUrl;
    }

    public static void main(String[] args) {
        String markdown = "# Hello 世界\n\n这是一个中文测试。";
        System.out.println(convertToPdf(markdown, null));
    }
}
