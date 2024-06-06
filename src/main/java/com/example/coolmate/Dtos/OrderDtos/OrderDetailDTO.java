package com.example.coolmate.Dtos.OrderDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "order id must be >= 0")
    private int orderId;

    @JsonProperty("product_detail_id")
    @Min(value = 1, message = "product id must be >= 0")
    private int productDetail;

    @Min(value = 0, message = "order id must be >= 0")
    private float price;

    @JsonProperty("quantity")
    @Min(value = 1, message = "number of product id must be >= 0")
    private int quantity;

    @JsonProperty("total_money")
    @Min(value = 0, message = "total number id must be >= 0")
    private float totalMoney;
}
