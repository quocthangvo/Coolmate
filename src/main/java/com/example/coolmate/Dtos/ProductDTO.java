package com.example.coolmate.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "title must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "price must be less than or equal to 10,000,000")
    private Float price;

    @JsonProperty("promotion_price")
    private Float promotionPrice;

    private String image;

    private String description;

    @JsonProperty("category_id")
    private int categoryId;
}
