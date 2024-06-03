package com.example.coolmate.Dtos.PurchaseOrderDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDetailDTO {

    private String unit;

    private int quantity;

    private float price;

    private boolean active;

    @JsonProperty("product_detail_id")
    private int productDetailId;

    @JsonProperty("purchase_order_id")
    private int purchaseOrderId;


}
