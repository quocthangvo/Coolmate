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

    @Min(value = 0, message = "Price không được là 0")
    @Max(value = 10000000, message = "price có giá trị lớn nhất là 10,000,000")
    private Float price;

    @JsonProperty("product_id")
    private int productId;
}
