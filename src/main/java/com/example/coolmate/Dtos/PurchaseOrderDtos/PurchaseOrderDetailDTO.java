package com.example.coolmate.Dtos.PurchaseOrderDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

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

    @JsonProperty("product_details")
    private List<Integer> productDetailId;

    @JsonProperty("purchase_order_id")
    private int purchaseOrderId;

}
