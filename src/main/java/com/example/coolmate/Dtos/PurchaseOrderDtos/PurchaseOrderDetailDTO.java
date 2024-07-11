package com.example.coolmate.Dtos.PurchaseOrderDtos;

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
public class PurchaseOrderDetailDTO {

    @JsonProperty("purchase_order_id")
    private int purchaseOrderId;

    @JsonProperty("product_details")
    private List<ProductDetail> productDetails;

    private float price;

    private int quantity;


}

