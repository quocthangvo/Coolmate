package com.example.coolmate.Responses.Product;

import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.Product.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private int id;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("product_name")
    private String productName;

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
    private PriceResponse latestPrice;

    @JsonProperty("images")
    private List<String> images; // Thêm thuộc tính danh sách URL hình ảnh

    @JsonProperty("version_sku")
    private String versionSku;


    public static ProductDetailResponse fromProductDetail(ProductDetail productDetail) {

        String colorName = productDetail.getColor().getName();
        String sizeName = productDetail.getSize().getName();

        // Lấy danh sách URL hình ảnh từ Product
        List<String> images = productDetail.getProduct().getProductImages().stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());

        PriceResponse latestPrice = PriceResponse.fromPriceList(productDetail.getPrices());

        return ProductDetailResponse.builder()
                .id(productDetail.getId())
                .productId(productDetail.getProduct().getId())
                .productName(productDetail.getProduct().getName())
                .versionName(productDetail.getVersionName())
                .colorId(productDetail.getColor().getId())
                .colorName(colorName)
                .sizeId(productDetail.getSize().getId())
                .sizeName(sizeName)
                .latestPrice(latestPrice)
                .images(images) // Thêm danh sách URL hình ảnh vào response
                .versionSku(productDetail.getVersionSku())
                .build();
    }
}
