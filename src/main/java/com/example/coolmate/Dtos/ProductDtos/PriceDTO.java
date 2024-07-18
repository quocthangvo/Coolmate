package com.example.coolmate.Dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceDTO {

    @Min(value = 0, message = "Giá bán không được để trống")
    @Max(value = 10000000, message = "Giá bán có giá trị lớn nhất là 10,000,000")
    @JsonProperty("price_selling")
    private float priceSelling;

    @JsonProperty("promotion_price")
    private float promotionPrice;

    @JsonProperty("product_detail_id")
    private int productDetailId;

    @JsonProperty("start_date")
    private LocalDateTime startDate; // Chuỗi ngày bắt đầu từ bàn phím

    @JsonProperty("end_date")
    private LocalDateTime endDate; // Chuỗi ngày kết thúc từ bàn phím


}