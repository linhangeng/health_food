package com.example.Properties;

import lombok.Data;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:14
 */
@Data
public class QueueConfig {
    private String name;
    private int concurrency = 1;
    private int maxConcurrency = 1;
}
