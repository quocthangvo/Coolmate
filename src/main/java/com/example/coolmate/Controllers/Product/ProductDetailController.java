package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.Product.IColorService;
import com.example.coolmate.Services.Impl.Product.IProductDetailService;
import com.example.coolmate.Services.Impl.Product.IProductService;
import com.example.coolmate.Services.Impl.Product.ISizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product_details")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class ProductDetailController {
    private final IProductDetailService productDetailService;
    private final IProductService productService;
    private final ISizeService sizeService;
    private final IColorService colorService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetailById(@PathVariable int id) {
        try {
            ProductDetail productDetail = productDetailService.getProductDetailById(id);
            return ApiResponseUtil.successResponse("Successfully", productDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProductDetails(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "limit") int limit) {
        List<ProductDetailDTO> productDetails = productDetailService.getAllProductDetails(page, limit);
        return ResponseEntity.ok(productDetails);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductDetail>>> findByProductId(@PathVariable int productId) {
        try {
            List<ProductDetail> productDetails = productDetailService.findByProductId(productId);
            return ApiResponseUtil.successResponse("Successfully", productDetails);
        } catch (DataNotFoundException e) {
//            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
            return ResponseEntity.status(404).body(null);
        }
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

//    @GetMapping("/search_color")
//    public ResponseEntity<?> searchProductDetailByColor(@RequestParam("color") String color) {
//        try {
//            List<ProductDetail> productDetails = productDetailService.searchProductDetailsByColor(color);
//            if (productDetails.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm có màu : " + color);
//            }
//            return ResponseEntity.ok().body(productDetails);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Lỗi khi tìm kiếm màu sắc : " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/search_size")
//    public ResponseEntity<?> searchProductDetailBySize(@RequestParam("size") String size) {
//        try {
//            List<ProductDetail> productDetails = productDetailService.searchProductDetailBySize(size);
//            if (productDetails.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm có size : " + size);
//            }
//            return ResponseEntity.ok().body(productDetails);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Đã có lỗi xảy ra khi tìm kiếm kích thước : " + e.getMessage());
//        }
//    }


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
