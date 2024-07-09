package com.example.coolmate.Dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

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

    private String description;

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("prices")
    private List<PriceDTO> prices;

    @JsonProperty("sizes")
    private List<Integer> sizeId;

    @JsonProperty("colors")
    private List<Integer> colorId;
}
