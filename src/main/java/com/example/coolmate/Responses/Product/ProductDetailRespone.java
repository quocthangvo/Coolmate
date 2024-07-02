package com.example.coolmate.Responses.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailRespone {
    private int sizeId;
    private String sizeName;
    private int colorId;
    private String colorName;
    private float price;
}
