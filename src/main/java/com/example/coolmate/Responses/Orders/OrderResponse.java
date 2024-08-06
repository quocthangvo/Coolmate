package com.example.coolmate.Responses.Orders;

import com.example.coolmate.Models.Order.Order;
import com.example.coolmate.Responses.BaseResponse;
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
    private int id;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private String status;

    private List<OrderDetailResponse> orderDetails;

    public static OrderResponse fromOrder(Order order) {
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                .map(OrderDetailResponse::fromOrderDetail) // Convert each OrderDetail to OrderDetailResponse
                .collect(Collectors.toList());
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .fullName(order.getFullName())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .note(order.getNote())
                .status(order.getStatus().getStatusName())
                .paymentMethod(order.getPaymentMethod())
                .orderDetails(orderDetailResponses)


                .build();
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setUpdatedAt(order.getUpdatedAt());
        return orderResponse;
    }

    public static List<OrderResponse> fromOrderList(List<Order> orders) {
        return orders.stream().map(OrderResponse::fromOrder).collect(Collectors.toList());
    }
}
