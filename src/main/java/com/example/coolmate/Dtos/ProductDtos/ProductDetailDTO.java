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

<<<<<<< HEAD
    @JsonProperty("colors")
    private List<Integer> colors;

    @JsonProperty("price")
    private PriceDTO price; // thông tin về giá
=======
    @JsonProperty("size_name")
    private String sizeName;

    @JsonProperty("colors")
    private List<Integer> colors;

    @JsonProperty("color_name")
    private String colorName;

>>>>>>> 4b960ba16cc7910a535a6bf521d064262714b715
}
