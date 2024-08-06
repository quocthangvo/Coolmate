package com.example.coolmate.Responses.Orders;

import com.example.coolmate.Models.Order.OrderDetail;
import com.example.coolmate.Responses.Product.ProductDetailResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class OrderDetailResponse {
    private int id;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("product_detail_id")
    private ProductDetailResponse productDetailId;

    private float price;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total_money")
    private float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        return OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productDetailId(ProductDetailResponse.fromProductDetail(orderDetail.getProductDetail()))
                .quantity(orderDetail.getQuantity())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}
