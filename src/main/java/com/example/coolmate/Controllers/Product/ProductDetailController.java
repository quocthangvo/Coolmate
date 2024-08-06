package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.Product.ProductDetailResponse;
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

//    @PostMapping("")
//    public ResponseEntity<?> createOrUpdateProductDetail(
//            @Valid @RequestBody ProductDetailDTO productDetailDTO, BindingResult result) {
//
//        if (result.hasErrors()) {
//            return ResponseEntity.badRequest().body("Validation errors: " + result.toString());
//        }
//        try {
//            ProductDetail newProductDetail = productDetailService.createProductDetail(productDetailDTO);
//            return ApiResponseUtil.successResponse("Product detail created successfully", newProductDetail);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Đã xảy ra lỗi khi tạo chi tiết sản phẩm: " + e.getMessage());
//        }
//
//    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetailById(@PathVariable int id) {
        try {
            ProductDetailResponse productDetail = productDetailService.getProductDetailById(id);
            return ApiResponseUtil.successResponse("Successfully", productDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProductDetails(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "limit") int limit) {
        try {
            List<ProductDetail> responses = productDetailService.getAllProductDetails(page, limit);
            return ApiResponseUtil.successResponse("Successfully", responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductDetailResponse>>> findByProductId(@PathVariable int productId) {
        try {
            List<ProductDetailResponse> productDetails = productDetailService.findByProductId(productId);
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


    @GetMapping("/size/{sizeId}")
    public ResponseEntity<?> getProductsBySizeId(@PathVariable int sizeId) {
        try {
            List<ProductDetail> productDetails = productDetailService.findBySizeId(sizeId);
            return ApiResponseUtil.successResponse("successfully", productDetails);
        } catch (Exception ex) {

            return ResponseEntity.badRequest().body(ex);
        }
    }

    @GetMapping("/color/{colorId}")
    public ResponseEntity<?> getProductsByColorId(@PathVariable int colorId) {
        try {
            List<ProductDetail> productDetails = productDetailService.findByColorId(colorId);
            return ApiResponseUtil.successResponse("successfully", productDetails);
        } catch (Exception ex) {

            return ResponseEntity.badRequest().body(ex);
        }
    }

    @GetMapping("/{sizeId}/{colorId}")
    public ResponseEntity<?> findBySizeIdAndColorId(@PathVariable int sizeId, @PathVariable int colorId) {
        try {
            List<ProductDetail> productDetails = productDetailService.findBySizeIdAndColorId(sizeId, colorId);
            return ApiResponseUtil.successResponse("successfully", productDetails);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ex);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProductDetails(@RequestParam String versionName) {
        try {
            List<ProductDetailResponse> productDetails = productDetailService.searchVersionName(versionName);
            return ApiResponseUtil.successResponse("successfully", productDetails);
        } catch (Exception ex) {

            return ResponseEntity.badRequest().body(ex);
        }
    }

    @GetMapping("/last_price/{id}")
    public ResponseEntity<?> getProductDetailLastPrice(@PathVariable int id) {
        try {
            ProductDetailResponse detailResponse = productDetailService.getProductDetailLastPrice(id);
            return ApiResponseUtil.successResponse("Successfully", detailResponse);
        } catch (Exception e) {
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
