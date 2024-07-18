package com.example.coolmate.Dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {

    @JsonProperty("product_id")
    private int productId;

    private String productName;

    @JsonProperty("sizes")
    private List<Integer> sizes;


    @JsonProperty("colors")
    private List<Integer> colors;

    @JsonProperty("version_sku")
    private String versionSku;

    @JsonProperty("version_name")
    private String versionName;

    private int quantity;

    private List<PriceDTO> prices;


}
