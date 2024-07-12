package com.example.coolmate.Models.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderStatus {
    public static final String PENDING = "Chờ xủ lý";

    public static final String PROCESSING = "Xác nhận";

    public static final String SHIPPING = "Đang giao hàng";

    public static final String DELIVERED = "Đã giao hàng";

    public static final String CANCELLED = "Hủy";
}
