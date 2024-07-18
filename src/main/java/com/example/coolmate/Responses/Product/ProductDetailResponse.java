package com.example.coolmate.Responses.Product;

import com.example.coolmate.Models.Product.ProductDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("version_name")
    private String versionName;

    @JsonProperty("color_id")
    private int colorId;

    @JsonProperty("color_name")
    private String colorName;

    @JsonProperty("size_id")
    private int sizeId;

    @JsonProperty("size_name")
    private String sizeName;

    @JsonProperty("price")
    private PriceResponse latestPrice; // Thay đổi thành PriceResponse

    public static ProductDetailResponse fromProductDetail(ProductDetail productDetail) {

        String colorName = productDetail.getColor().getName();
        String sizeName = productDetail.getSize().getName();


        PriceResponse latestPrice = PriceResponse.fromPriceList(productDetail.getPrices());

        return ProductDetailResponse.builder()
                .productId(productDetail.getProduct().getId())
                .versionName(productDetail.getVersionName())
                .colorId(productDetail.getColor().getId())
                .colorName(colorName)
                .sizeId(productDetail.getSize().getId())
                .sizeName(sizeName)
                .latestPrice(latestPrice)
                .build();
    }

}
