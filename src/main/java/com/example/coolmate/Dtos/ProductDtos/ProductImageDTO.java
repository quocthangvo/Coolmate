package com.example.coolmate.Dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
    private int id;

    @JsonProperty("image_url")
    private String imageUrl;
}
