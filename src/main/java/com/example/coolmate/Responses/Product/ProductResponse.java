package com.example.coolmate.Responses.Product;

import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductImage;
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
    private float price;

    private String description;

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("product_images")
    private List<ProductImage> productImages;

    private String sku;

    @JsonProperty("product_details")
    private List<ProductDetailResponse> productDetails;

    public static ProductResponse fromProduct(Product product) {
        List<ProductDetailResponse> productDetailResponses = product.getProductDetails().stream()
                .map(ProductDetailResponse::fromProductDetail)
                .collect(Collectors.toList());

        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategoryId().getId())
                .categoryName(product.getCategoryId().getName())
                .productImages(product.getProductImages())
                .sku(product.getSku())
                .productDetails(productDetailResponses) // Chuyển đổi danh sách ProductDetail thành ProductDetailResponse
                .build();
    }
}
