package com.example.coolmate.Dtos.ProductDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO {
    @NotBlank(message = "Tên thương hệu không để trống")
    private String name;
}
