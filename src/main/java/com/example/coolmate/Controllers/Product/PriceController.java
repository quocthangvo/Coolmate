package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.Product.PriceResponse;
import com.example.coolmate.Services.Impl.Product.IPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/prices")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")

public class PriceController {
    private final IPriceService priceService;

    @PostMapping("")
    public ResponseEntity<?> createPrice(
            @RequestBody PriceDTO priceDTO) {
        try {
            PriceResponse price = priceService.createPrice(priceDTO);
            return ApiResponseUtil.successResponse("Tạo giá thành công", price);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage("Đã xảy ra lỗi không xác định"));
        }

    }


    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<PriceResponse>>> getAllPrices(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) throws DataNotFoundException {
        Page<PriceResponse> prices = priceService.getAllPrices(page, limit);
        return ApiResponseUtil.successResponse("thành công", prices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPriceById(@PathVariable int id) {
        try {
            PriceResponse
                    price = priceService.getPriceById(id);
            return ApiResponseUtil.successResponse("thành công", price);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePrice(@PathVariable int id) {
        try {
            priceService.deletePrice(id);
            String message = "Xóa nhà cung cấp có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @PutMapping("/product_detail/{id}")
    public ResponseEntity<?> updatePrice(
            @PathVariable int id,
            @Valid @RequestBody PriceDTO priceDTO) {
        try {
            PriceResponse updatedPrice = priceService.updatePriceByProductDetailId(id, priceDTO);
            return ApiResponseUtil.successResponse("Cập nhật thành công", updatedPrice);
        } catch (DataNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }


    @GetMapping("/product_detail/{productDetailId}")
    public ResponseEntity<ApiResponse<PriceResponse>> getPricesByProductDetailId(@PathVariable int productDetailId) {
        try {
            PriceResponse priceResponse = priceService.getPricesByProductDetailId(productDetailId);
            return ApiResponseUtil.successResponse("Thành công", priceResponse);
        } catch (DataNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/price_distinct")
    public ResponseEntity<ApiResponse<Page<?>>> getAllDistinctPricesByProductDetailId(int page, int limit) {
//        return priceService.getAllDistinctPricesByProductDetailId();
        Page<PriceResponse> price = priceService.getAllDistinctPricesByProductDetailId(page, limit);
        return ApiResponseUtil.successResponse("thành công", price);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<PriceResponse>> updatePriceByPriceId(@PathVariable int id,
                                                                           @RequestBody PriceDTO priceDTO) {
        try {
            // Gọi service để cập nhật giá và nhận lại thông tin giá đã cập nhật
            PriceResponse updatedPrice = priceService.updatePrice(id, priceDTO);
            return ApiResponseUtil.successResponse("thành công", updatedPrice);
        } catch (DataNotFoundException e) {
            return ResponseEntity.notFound().build();

        }
    }

    @GetMapping("/search")
    public List<PriceResponse> searchPricesByVersionName(@RequestParam("versionName") String versionName) {
        return priceService.searchPricesByVersionName(versionName);
    }
}


