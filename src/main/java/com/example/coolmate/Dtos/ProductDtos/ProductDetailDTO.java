package com.example.coolmate.Dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("size_id")
    private int sizeId;

    @JsonProperty("size_name")
    private String sizeName;

    @JsonProperty("color_id")
    private int colorId;

    @JsonProperty("color_name")
    private String colorName;
}
