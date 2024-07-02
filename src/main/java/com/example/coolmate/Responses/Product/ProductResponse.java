package com.example.coolmate.Responses.Product;

import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse {
    private String name;
    private String image;
    private String description;

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("product_details")
    private List<ProductDetailRespone> productDetails;

    public static ProductResponse fromProduct(Product product) {
        List<ProductDetailRespone> productDetailResponses = product.getProductDetails().stream()
                .map(detail -> ProductDetailRespone.builder()
                        .sizeId(detail.getSize().getId())
                        .sizeName(detail.getSize().getName())
                        .colorId(detail.getColor().getId())
                        .colorName(detail.getColor().getName())
                        .price(detail.getPrices() != null && !detail.getPrices().isEmpty() ? detail.getPrices().get(0).getPrice() : 0)
                        .build())
                .collect(Collectors.toList());

        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .image(product.getImage())
                .description(product.getDescription())
                .categoryId(product.getCategoryId().getId())
                .categoryName(product.getCategoryId().getName())
                .productDetails(productDetailResponses)
                .build();

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }
}
