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


//    @JsonProperty("product_details")
//    private List<ProductDetail> productDetails;

    @JsonProperty("product_detail_id")
    private int productDetailId;

    private float price;

    private int quantity;


}

