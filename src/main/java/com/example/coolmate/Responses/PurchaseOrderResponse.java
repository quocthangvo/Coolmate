package com.example.coolmate.Responses;

import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderResponse extends BaseResponse {

    private String code;

    @Column(name = "order_date")
    private Date orderDate;

    private String status;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("supplier_name")
    private String supplierName;

    public static PurchaseOrderResponse fromPurchase(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderResponse.builder()
                .code(purchaseOrder.getCode())
                .orderDate(purchaseOrder.getOrderDate())
                .status(purchaseOrder.getStatus())
                .shippingDate(purchaseOrder.getShippingDate())
                .userName(purchaseOrder.getUser().getFullName())
                .phoneNumber(purchaseOrder.getUser().getPhoneNumber())
//                .address(purchaseOrder.getUser().getAddress())
                .supplierName(purchaseOrder.getSupplier().getName())
                .build();
        purchaseOrderResponse.setCreatedAt(purchaseOrder.getCreatedAt());
        purchaseOrderResponse.setUpdatedAt(purchaseOrder.getUpdatedAt());
        return purchaseOrderResponse;
    }
}
