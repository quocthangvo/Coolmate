package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.CategoryDTO;
import com.example.coolmate.Dtos.ProductDtos.BrandDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Category;
import com.example.coolmate.Models.Product.Brand;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.Product.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;

    @PostMapping("")
    public ResponseEntity<?> createBrand(@Valid @RequestBody BrandDTO brandDTO, BindingResult result){
        try {
            Brand createdBrand = brandService.createBrand(brandDTO);
            ApiResponse<Brand> response = new ApiResponse<>(
                    "Brand created successfully", createdBrand);
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ErrorMessage("Đã xảy ra lỗi không xác định"));
        }
    }


    @GetMapping("")
    public ResponseEntity<List<Brand>> getAllBrands(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Brand> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getBrandById(@PathVariable int id) {
//        try {
//            Brand brand = brandService.getBrandById(id);
//            return ResponseEntity.ok(brand);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
//        }
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBrandById(@PathVariable int id) {
        try {
            brandService.deleteBrandById(id);
            String message = "Xóa thương hiệu có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrandById(
            @PathVariable int id,
            @Valid @RequestBody BrandDTO brandDTO) {
        try {
            Brand updateBrand = brandService.updateBrand(id, brandDTO);
            return ApiResponseUtil.successResponse("Cập nhật thương hiệu thành công", updateBrand);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

}
