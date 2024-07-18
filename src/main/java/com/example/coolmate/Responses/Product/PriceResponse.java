package com.example.coolmate.Responses.Product;

import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.ProductDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceResponse {

    @JsonProperty("price_id")
    private int priceId;

    @JsonProperty("price_selling")
    private float priceSelling;

    @JsonProperty("promotion_price")
    private float promotionPrice;

    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @JsonProperty("end_date")
    private LocalDateTime endDate;

    @JsonProperty("product_detail")
    private ProductDetail productDetail;

    // Chuyển đổi danh sách giá từ List<Price> sang PriceResponse
    public static PriceResponse fromPriceList(List<Price> prices) {
        // Lọc và chỉ lấy giá cuối cùng (có startDate lớn nhất)
        Price latestPrice = prices.stream()
                .max(Comparator.comparing(Price::getStartDate))
                .orElse(null);

        if (latestPrice != null) {
            return PriceResponse.builder()
                    .priceId(latestPrice.getId())
                    .productDetail(latestPrice.getProductDetail())
                    .priceSelling(latestPrice.getPriceSelling())
                    .promotionPrice(latestPrice.getPromotionPrice())
                    .startDate(latestPrice.getStartDate())
                    .endDate(latestPrice.getEndDate())
                    .build();
        } else {
            return null; // Hoặc xử lý trường hợp khác tùy vào logic của bạn
        }
    }
}
