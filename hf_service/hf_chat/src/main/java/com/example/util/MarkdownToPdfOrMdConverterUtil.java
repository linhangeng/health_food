package com.example.util;

import com.example.exception.BizEnums;
import com.example.exception.WrappedException;
import com.example.service.ChatService;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.kernel.pdf.PdfDocument;

import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class MarkdownToPdfOrMdConverterUtil {


    /**
     * 将 Markdown 文本转换为 PDF 文件。
     *
     * @param markdownContent 待转换的 Markdown 字符串
     * @throws Exception
     */
    public static InputStream convertMarkdownToPdf(String markdownContent) {
        try {
            // 1. 使用 Flexmark 解析 Markdown，启用表格、任务列表、删除线、目录等扩展
            MutableDataSet options = new MutableDataSet();
            options.set(Parser.EXTENSIONS, Arrays.asList(
                    TablesExtension.create(),
                    TocExtension.create(),
                    StrikethroughExtension.create(),
                    TaskListExtension.create()
            ));
            // 可选：将软换行转换为 <br/>
            options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();
            // 解析并渲染 HTML
            String html = renderer.render(parser.parse(markdownContent));
            // 2. 准备 iText pdfHTML 转换
            ConverterProperties props = new ConverterProperties();
            // 嵌入支持中文的字体，例如 Noto Sans CJK
            FontProvider fontProvider = new FontProvider();
            // 根据实际字体文件路径配置，确保中文字符可用:contentReference[oaicite:2]{index=2}:contentReference[oaicite:3]{index=3}
            URL fontUrl = ChatService.class.getClassLoader().getResource("fonts/simhei.ttf");
            if (fontUrl == null) {
                log.error("字体文件不存在：fonts/simhei.ttf，请检查resources目录");
            }
            String fontFilePath = fontUrl.getFile();
            fontProvider.addFont(fontFilePath);
            props.setFontProvider(fontProvider);
            // 3. HTML 转 PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), pdf, props);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception e) {
            log.error("md格式转pdf失败", e);
            throw new WrappedException(BizEnums.CUSTOM_ERROR, "md格式转pdf失败");
        }
    }


    /**
     * 将 Markdown 文本保存为 .md 文件
     * @param markdownText Markdown 文本内容
     * @param outputPath 输出文件路径（例如：output/hello.md）
     * @throws IOException
     */
    public static InputStream markdownToFile(String markdownText) {
        try {
            return new ByteArrayInputStream(markdownText.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("转换 Markdown 为流失败", e);
            throw new RuntimeException("文件处理失败", e);
        }
    }
}
