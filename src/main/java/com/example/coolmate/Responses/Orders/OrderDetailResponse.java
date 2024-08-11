package com.example.coolmate.Responses.Orders;

import com.example.coolmate.Models.Order.OrderDetail;
import com.example.coolmate.Responses.Product.PriceResponse;
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

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total_money")
    private float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        // Calculate totalMoney based on promotional price or selling price
        float totalMoney = calculateTotalMoney(orderDetail);

        return OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productDetailId(ProductDetailResponse.fromProductDetail(orderDetail.getProductDetail()))
                .quantity(orderDetail.getQuantity())
                .totalMoney(totalMoney)
                .build();
    }

    private static float calculateTotalMoney(OrderDetail orderDetail) {
        // Get the latest price from the product detail
        PriceResponse latestPrice = ProductDetailResponse.fromProductDetail(orderDetail.getProductDetail()).getLatestPrice();
        float price = latestPrice.getPriceSelling(); // Default to selling price

        if (latestPrice.getPromotionPrice() > 0) {
            price = latestPrice.getPromotionPrice(); // Use promotional price if available
        }

        return price * orderDetail.getQuantity(); // Calculate total money
    }
}
