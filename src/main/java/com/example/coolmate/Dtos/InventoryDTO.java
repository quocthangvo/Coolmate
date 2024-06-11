package com.example.coolmate.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    @JsonProperty("purchase_order_id")
    private int purchaseOrderId;

//    @JsonProperty("product_detail_id")
//    private int productDetailId;

    private int quantity;
}
