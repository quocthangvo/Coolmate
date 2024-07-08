package com.example.coolmate.Responses.Product;

import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse {
    private String name;
    private float price;
    private String image;
    private String description;

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("category_name")
    private String categoryName;


    public static ProductResponse fromProduct(Product product) {

        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategoryId().getId())
                .categoryName(product.getCategoryId().getName())

                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }
}
