package com.example.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @ClassName EasyExcelImportUtil
 * @Author sheng.lin
 * @Date 2025/8/5
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Slf4j
public class EasyExcelImportUtil {


    // 线程池（用于大批量异步处理）
    private static final ExecutorService BATCH_EXECUTOR = new ThreadPoolExecutor(
            4, // 核心线程数（根据CPU核心数调整）
            8, // 最大线程数
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactory() {
                private int count = 0;
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("excel-import-thread-" + count++);
                    thread.setDaemon(true); // 守护线程，避免阻塞JVM退出
                    return thread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // 任务满时，让提交者线程执行（避免任务丢失）
    );


    // ------------------------------ 小批量导入（几万行） ------------------------------
    /**
     * 小批量导入（同步处理，适合≤5万行）
     * @param file
     * @param head
     * @param dataConsumer
     * @Author sheng.lin
     * @Date 2025/8/5
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public static <T> void importSmallBatch(MultipartFile file, Class<T> head, Consumer<List<T>> dataConsumer) throws IOException {
        importSmallBatch(file.getInputStream(), head, dataConsumer);
    }



    /**
     * 小批量导入（同步处理，适合≤5万行）
     * @param filePath
     * @param head
     * @param dataConsumer
     * @Author sheng.lin
     * @Date 2025/8/5
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public static <T> void importSmallBatch(String filePath, Class<T> head, Consumer<List<T>> dataConsumer) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在：" + filePath);
        }
        List<T> allData = EasyExcel.read(file).head(head).sheet().doReadSync();
        dataConsumer.accept(allData); // 回调处理全部数据
    }



    /**
     * 导入
     * @param is
     * @param head
     * @param dataConsumer
     * @Author sheng.lin
     * @Date 2025/8/5
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public static <T> void importSmallBatch(InputStream is, Class<T> head, Consumer<List<T>> dataConsumer) {
        // 同步读取所有数据（适合小批量，数据量过大会OOM）
        List<T> allData = EasyExcel.read(is).head(head).sheet().doReadSync();
        dataConsumer.accept(allData); // 回调处理全部数据
    }


    // ------------------------------ 大批量导入（几百万行） ------------------------------

    /**
     * 大批量导入（流式处理+异步批处理，适合≥10万行）
     * 特点：分批次读取，异步处理，内存可控
     * @param batchSize 批处理大小（建议5000-10000行/批，根据内存调整）
     * @param dataHandler 批数据处理器（如批量入库）
     * @param finishCallback 全部完成后的回调（如更新导入状态）
     */
    public static <T> void importLargeBatch(MultipartFile file, Class<T> head, int batchSize,
                                            Consumer<List<T>> dataHandler, Runnable finishCallback) throws IOException {
        importLargeBatch(file.getInputStream(), head, batchSize, dataHandler, finishCallback);
    }

    public static <T> void importLargeBatch(String filePath, Class<T> head, int batchSize,
                                            Consumer<List<T>> dataHandler, Runnable finishCallback) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在：" + filePath);
        }
        readLargeExcel(file, head, batchSize, dataHandler, finishCallback);
    }

    public static <T> void importLargeBatch(InputStream is, Class<T> head, int batchSize,
                                            Consumer<List<T>> dataHandler, Runnable finishCallback) {
        readLargeExcel(is, head, batchSize, dataHandler, finishCallback);
    }

    /**
     * 大批量Excel读取核心逻辑
     */
    private static <T> void readLargeExcel(Object input, Class<T> head, int batchSize,
                                           Consumer<List<T>> dataHandler, Runnable finishCallback) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("批处理大小必须>0，建议5000-10000");
        }

        // 自定义监听器：流式读取+异步批处理
        ReadListener<T> listener = new ReadListener<T>() {
            private List<T> batchData = ListUtils.newArrayListWithExpectedSize(batchSize); // 预分配容量

            @Override
            public void invoke(T data, com.alibaba.excel.context.AnalysisContext context) {
                batchData.add(data);
                // 达到批处理大小，异步处理
                if (batchData.size() >= batchSize) {
                    processBatch(batchData);
                    batchData = ListUtils.newArrayListWithExpectedSize(batchSize); // 重置批数据
                }
            }

            @Override
            public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {
                // 处理剩余数据
                if (!batchData.isEmpty()) {
                    processBatch(batchData);
                }
                // 所有批次处理完成后执行回调（提交者线程执行）
                BATCH_EXECUTOR.execute(finishCallback);
                log.info("所有数据处理完成");
            }

            // 异步处理单批数据
            private void processBatch(List<T> data) {
                // 提交到线程池异步处理（避免阻塞Excel读取线程）
                BATCH_EXECUTOR.submit(() -> {
                    try {
                        dataHandler.accept(data); // 调用业务处理（如批量入库）
                        log.info("处理完成一批数据，大小：{}", data.size());
                    } catch (Exception e) {
                        log.error("批处理数据失败", e);
                        throw new RuntimeException("批处理失败：" + e.getMessage()); // 抛出异常，可根据业务处理
                    }
                });
            }

            @Override
            public boolean hasNext(com.alibaba.excel.context.AnalysisContext context) {
                return true;
            }
        };

        // 构建Excel读取器
        ExcelReaderBuilder readerBuilder;
        if (input instanceof File) {
            readerBuilder = EasyExcel.read((File) input, head, listener);
        } else if (input instanceof InputStream) {
            readerBuilder = EasyExcel.read((InputStream) input, head, listener);
        } else {
            throw new IllegalArgumentException("不支持的输入类型");
        }
        // 大批量读取优化：关闭自动关闭输入流（由框架统一管理）
        readerBuilder.autoCloseStream(false);
        // 读取Excel（默认读取第一个sheet）
        readerBuilder.sheet().doRead();
    }
}
