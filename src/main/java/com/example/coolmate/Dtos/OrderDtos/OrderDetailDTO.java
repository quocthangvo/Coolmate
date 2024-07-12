package com.example.coolmate.Dtos.OrderDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("product_detail_id")
    private int productDetailId;

    private float price;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total_money")
    private float totalMoney;

    @JsonProperty("version_order_code")
    private String versionOrderCode;
}
