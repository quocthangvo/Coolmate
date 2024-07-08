package com.example.coolmate.Dtos;

import com.example.coolmate.Models.Product.ProductDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {

    @JsonProperty("product_details")
    private List<ProductDetail> productDetails;

    private int quantity;

    private float price;

    @JsonProperty("purchase_order_id")
    private int purchaseOrderId;
}
