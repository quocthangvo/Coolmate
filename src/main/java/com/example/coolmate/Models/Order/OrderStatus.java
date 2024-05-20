package com.example.coolmate.Models.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderStatus {
    public static final String PENDING = "pending";

    public static final String PROCESSING = "processing";

    public static final String SHIPPER = "shipper";

    public static final String DELIVERED = "delivered";

    public static final String CANCELLED = "cancelled";
}
