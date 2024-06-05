package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Product.Color;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.Product.Size;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.Product.IColorService;
import com.example.coolmate.Services.Impl.Product.IProductDetailService;
import com.example.coolmate.Services.Impl.Product.IProductService;
import com.example.coolmate.Services.Impl.Product.ISizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/product_details")
@RequiredArgsConstructor
public class ProductDetailController {
    private final IProductDetailService productDetailService;
    private final IProductService productService;
    private  final ISizeService sizeService;
    private  final IColorService colorService;

    @PostMapping("")
    public ResponseEntity<?> createOrUpdateProductDetail(
            @Valid @RequestBody ProductDetailDTO productDetailDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.toString());
        }
        try {
                ProductDetail newProductDetail = productDetailService.createProductDetail(productDetailDTO);
                return ApiResponseUtil.successResponse("Product detail created successfully", newProductDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Đã xảy ra lỗi khi tạo chi tiết sản phẩm: " + e.getMessage());
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

    @GetMapping("/searchColor")
    public ResponseEntity<?> searchProductDetailByColor(@RequestParam("color") String color){
        try{
            List<ProductDetail> productDetails = productDetailService.searchProductDetailsByColor(color);
            if(productDetails.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm có màu : "+color);
            }
            return ResponseEntity.ok().body(productDetails);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Lỗi khi tìm kiếm màu sắc : "+e.getMessage());
        }
    }

    @GetMapping("/searchSize")
    public ResponseEntity<?> searchProductDetailBySize(@RequestParam("size") String size){
        try{
            List<ProductDetail> productDetails = productDetailService.searchProductDetailBySize(size);
            if(productDetails.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm có size : "+size);
            }
            return ResponseEntity.ok().body(productDetails);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Đã có lỗi xảy ra khi tìm kiếm kích thước : "+e.getMessage());
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
