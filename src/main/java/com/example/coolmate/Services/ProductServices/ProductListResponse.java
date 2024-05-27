package com.example.coolmate.Services.ProductServices;

import com.example.coolmate.Responses.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductListResponse {
    //tổng số trang, trả về kiểu dl phân trang
    private List<ProductResponse> products;
    private int totalPage;
}
