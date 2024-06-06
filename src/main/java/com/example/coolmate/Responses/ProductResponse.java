package com.example.coolmate.Responses;

import com.example.coolmate.Models.Product.Product;
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

    @JsonProperty("brand_id")
    private int brandId;

    @JsonProperty("category_id")
    private int categoryId;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .image(product.getImage())
                .description(product.getDescription())
                .build();

        if (product.getBrand() != null) {
            productResponse.setBrandId(product.getBrand().getId());
        }

        if (product.getCategory() != null) {
            productResponse.setCategoryId(product.getCategory().getId());
        }

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }


}
