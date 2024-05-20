package com.example.coolmate.Responses;

import com.example.coolmate.Models.Order.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse extends BaseResponse {
    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("total_money")
    private float totalMoney;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("status")
    private String status;

    public static OrderResponse fromOrder(Order order) {
        OrderResponse orderResponse = OrderResponse.builder()
                .fullname(order.getFullName())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .note(order.getNote())
                .status(order.getStatus())
                .totalMoney(order.getTotalMoney())
                .paymentMethod(order.getPaymentMethod())
                .build();
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setUpdatedAt(order.getUpdatedAt());
        return orderResponse;
    }

    public static List<OrderResponse> fromOrderList(List<Order> orders) {
        return orders.stream().map(OrderResponse::fromOrder).collect(Collectors.toList());
    }
}
