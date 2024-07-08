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

    @JsonProperty("purchase_order_id")
    private int purchaseOrderId;

    @JsonProperty("product_details")
    private List<ProductDetailOrder> productDetails;

    private boolean active;

    private float price;

    private int quantity;

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDetailOrder {
        @JsonProperty("product_detail_id")
        private Integer productDetailId;

        private Integer quantity;

        private Float price;
    }

}
