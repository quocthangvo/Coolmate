package com.example.coolmate.Models.PurchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseOrderStatus {
    public static final String PENDING = "Chờ xử lý";

    public static final String DELIVERED = "Đã giao hàng";

    public static final String CANCELLED = "Hủy";
}
