package com.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:34
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String orderId;

    private String orderName;
}
