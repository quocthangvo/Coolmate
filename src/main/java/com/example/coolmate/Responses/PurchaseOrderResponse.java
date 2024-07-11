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


    @JsonProperty("supplier_name")
    private String supplierName;

    @JsonProperty("version_code")
    private String versionCode;

    @JsonProperty("product_detail_id")
    private int productDetailId;

    private float price;

    private int quantity;

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
                .supplierName(purchaseOrder.getSupplier().getName())
                .versionCode(purchaseOrder.getVersionCode())
                
                .build();
        purchaseOrderResponse.setCreatedAt(purchaseOrder.getCreatedAt());
        purchaseOrderResponse.setUpdatedAt(purchaseOrder.getUpdatedAt());
        return purchaseOrderResponse;
    }
}
