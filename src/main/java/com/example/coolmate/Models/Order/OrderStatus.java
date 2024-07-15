package com.example.coolmate.Models.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

@AllArgsConstructor

public enum OrderStatus {
    PENDING("Chờ xử lý"),
    PROCESSING("Xác nhận"),
    SHIPPING("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Hủy");

    private final String statusName;
}
