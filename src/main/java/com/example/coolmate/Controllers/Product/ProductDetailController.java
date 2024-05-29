package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.Product.IProductDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product_details")
@RequiredArgsConstructor
public class ProductDetailController {
    private final IProductDetailService productDetailService;

    @PostMapping("")
    public ResponseEntity<?> createProductDetail(
            @Valid @RequestBody ProductDetailDTO productDetailDTO, BindingResult result) {
        try {
            ProductDetail createdProductDetail = productDetailService.createProductDetail(productDetailDTO);
            return ApiResponseUtil.successResponse("Product detail created successfully", createdProductDetail); // Trả về dữ liệu của danh mục được tạo

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetailById(@PathVariable int id) {
        try {
            ProductDetail productDetail = productDetailService.getProductDetailById(id);
            return ResponseEntity.ok(productDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDetail>> getAllProductDetails(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<ProductDetail> productDetails = productDetailService.getAllProductDetails();
        return ResponseEntity.ok(productDetails);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProductDetail(@PathVariable int id) {
        try {
            productDetailService.deleteProductDetail(id);
            String message = "Xóa chi tiết sản phẩm có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updatePrice(
//            @PathVariable int id,
//            @Valid @RequestBody ProductDetailDTO productDetailDTO) {
//        try {
//            ProductDetail updatedProductDetail = productDetailService.updateProductDetail(id, productDetailDTO);
//            return ApiResponseUtil.successResponse("Supplier updated successfully", updatedProductDetail); // Trả về dữ liệu của danh mục được tạo
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
//
//        }
//    }
}
