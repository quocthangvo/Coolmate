//package com.example.coolmate.Responses.Product;
//
//import com.example.coolmate.Models.Product.ProductDetail;
//import com.example.coolmate.Responses.BaseResponse;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.*;
//
//@Builder
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class ProductDetailResponse extends BaseResponse {
//
//
//    @JsonProperty("product_id")
//    private int productId;
//
//    @JsonProperty("product_name")
//    private String productName;
//
//    @JsonProperty("color_id")
//    private int colorId;
//
//    @JsonProperty("color_name")
//    private String colorName;
//
//    @JsonProperty("size_id")
//    private int sizeId;
//
//    @JsonProperty("size_name")
//    private String sizeName;
//
//    public static ProductDetailResponse fromProductDetail(ProductDetail productDetail) {
//        ProductDetailResponse productDetailResponse = ProductDetailResponse.builder()
//                .productId(productDetail.getProduct().getId())
//                .colorId(productDetail.getColor().getId())
//                .sizeId(productDetail.getSize().getId())
//                .productName(productDetail.getProduct().getName())
//                .colorName(productDetail.getColor().getName())
//                .sizeName(productDetail.getSize().getName())
//
//                .build();
//        productDetailResponse.setCreatedAt(productDetail.getCreatedAt());
//        productDetailResponse.setUpdatedAt(productDetail.getUpdatedAt());
//
//        return productDetailResponse;
//    }
//}
