package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Services.Impl.Product.IPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/prices")
@RequiredArgsConstructor
public class PriceController {
    private final IPriceService priceService;

    @PostMapping("")
    public ResponseEntity<?> createPrice(
            @Valid @RequestBody PriceDTO priceDTO, BindingResult result) {
        try {
            Price createdPrice = priceService.createPrice(priceDTO);
            return ResponseEntity.ok(createdPrice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePrice(@PathVariable int id) {
        try {
            priceService.deletePrice(id);
            return ResponseEntity.ok("Xóa price có id " + id + " thành công");
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
