package com.example.coolmate.Dtos.ProductDtos;

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
}
