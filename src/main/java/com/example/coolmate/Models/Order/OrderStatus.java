package com.example.coolmate.Models.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

@AllArgsConstructor

public enum OrderStatus {
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    SHIPPING("SHIPPING"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED");

    private final String statusName;
}
