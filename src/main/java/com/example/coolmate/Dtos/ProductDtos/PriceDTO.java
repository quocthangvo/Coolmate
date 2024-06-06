package com.example.coolmate.Dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceDTO {

    @Min(value = 0, message = "Giá từ nhà cung cấp không được là 0")
    @Max(value = 10000000, message = "price có giá trị lớn nhất là 10,000,000")
    private float price;

    @Min(value = 0,message = "Giá bán không được để trống")
    @Max(value = 10000000,message = "Giá bán có giá trị lớn nhất là 10,000,000")
    @JsonProperty("price_selling")
    private float priceSelling;

    @JsonProperty("promotion_price")
    private Float promotionPrice;

    @JsonProperty("product_details_id")
    private int productDetailID;
}
