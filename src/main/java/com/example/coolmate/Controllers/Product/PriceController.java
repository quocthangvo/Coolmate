package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.Product.IPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/prices")
@RequiredArgsConstructor
public class PriceController {
    private final IPriceService priceService;

    @PostMapping("")
    public ResponseEntity<?> createPrice(
            @Valid @RequestBody PriceDTO priceDTO) {
        try {
            Price createdPrice = priceService.createPrice(priceDTO);
            return ApiResponseUtil.successResponse("Supplier created successfully", createdPrice);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage("Đã xảy ra lỗi không xác định"));
        }

    }


    @GetMapping("")
    public ResponseEntity<List<Price>> getAllPrices(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Price> prices = priceService.getAllPrices();
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPriceById(@PathVariable int id) {
        try {
            Price price = priceService.getPriceById(id);
            return ResponseEntity.ok(price);
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrice(
            @PathVariable int id,
            @Valid @RequestBody PriceDTO priceDTO) {
        try {
            priceService.updatePrice(id, priceDTO);
            return ResponseEntity.ok("Cập nhật price thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }
}
