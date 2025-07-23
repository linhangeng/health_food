/*
package com.example.controller;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

*/
/**
 * @ClassName ShardingSphereConfig
 * @Author sheng.lin
 * @Date 2025/7/23
 * @Version 1.0
 * @修改记录
 **//*

*/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 *//*

@Configuration
public class ShardingSphereConfig {

    @Bean
    public DataSource dataSource() throws SQLException {
        // 1. 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 主库配置
        DruidDataSource masterDataSource = new DruidDataSource();
        masterDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        masterDataSource.setUrl(System.getProperty("database.master.url")); // 使用 system property 或者后面再替换
        masterDataSource.setUsername(System.getProperty("database.master.user"));
        masterDataSource.setPassword(System.getProperty("database.master.password"));
        dataSourceMap.put("hf_master", masterDataSource);

        // 从库配置
        DruidDataSource slaveDataSource = new DruidDataSource();
        slaveDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        slaveDataSource.setUrl(System.getProperty("database.slave.url"));
        slaveDataSource.setUsername(System.getProperty("database.slave.user"));
        slaveDataSource.setPassword(System.getProperty("database.slave.password"));
        dataSourceMap.put("hf_slave", slaveDataSource);

        // 2. 配置读写分离数据源规则
        ReadwriteSplittingDataSourceRuleConfiguration readwriteRule = new ReadwriteSplittingDataSourceRuleConfiguration(
                "readwrite_ds",
                "hf_master",
                Arrays.asList("hf_slave"),
                "round_robin"
        );

        // 3. 配置负载均衡算法
        Map<String, ShardingSphereAlgorithmConfiguration> loadBalancers = new HashMap<>();
        loadBalancers.put("round_robin", new ShardingSphereAlgorithmConfiguration("ROUND_ROBIN", new Properties()));

        // 4. 创建读写分离规则配置
        ReadwriteSplittingRuleConfiguration ruleConfig = new ReadwriteSplittingRuleConfiguration(
                Collections.singletonList(readwriteRule),
                loadBalancers
        );

        // 5. 设置属性
        Properties props = new Properties();
        props.setProperty("sql-show", "true");
        props.setProperty("sql-format", "true");

        // 6. 创建 ShardingSphere 数据源
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singletonList(ruleConfig), props);
    }
}*/
