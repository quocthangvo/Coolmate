package com.example.coolmate.Models.PurchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseOrderStatus {
    public static final String PENDING = "pending";

    public static final String PROCESSING = "processing";

    public static final String SHIPPING = "shipping";

    public static final String DELIVERED = "delivered";

    public static final String CANCELLED = "cancelled";
}
