package com.example.coolmate.Dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorDTO {
    @NotBlank(message = "Color name không để trống")
    private String name;

    @JsonProperty("product_id")
    private int ProductId;
}
