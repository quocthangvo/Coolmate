package com.example.coolmate.Responses.Product;

import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceReponse {
    private int id;
    private float price;
    private float priceSelling;
    private Float promotionPrice;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("product_detail")
    private int productDetailId;
}
