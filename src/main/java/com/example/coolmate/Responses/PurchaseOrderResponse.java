package com.example.coolmate.Responses;

import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderResponse extends BaseResponse {

    private int id;

    private String code;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private String status;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("role_id")
    private int roleId;

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
                .id(purchaseOrder.getId())
                .code(purchaseOrder.getCode())
                .orderDate(purchaseOrder.getOrderDate())
                .status(purchaseOrder.getStatus())
                .shippingDate(purchaseOrder.getShippingDate())
                .userId(purchaseOrder.getUser().getId())
                .roleId(purchaseOrder.getUser().getRole().getId()) // Assuming you have a method to get role ID
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
