package com.example.coolmate.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Size(min = 1, message = "product id must be >= 0")
    private int productId;

    @JsonProperty("image_url")
    @Size(min = 5, max = 100, message = "Image name")
    private String imageUrl;
}
