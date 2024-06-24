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


    @JsonProperty("sizes")
    private List<Integer> sizes;


    @JsonProperty("colors")
    private List<Integer> colors;


}
