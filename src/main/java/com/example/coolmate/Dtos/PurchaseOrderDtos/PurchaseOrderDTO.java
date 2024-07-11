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
public class PurchaseOrderDTO {


    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("supplier_id")
    private int supplierId;

    private String status;

    private int quantity;

    @JsonProperty("product_detail_id")
    private List<Integer> productDetailId;

    @JsonProperty("version_code")
    private String versionCode;

    private float price;

}
