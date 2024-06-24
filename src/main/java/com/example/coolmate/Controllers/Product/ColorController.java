package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.ProductDtos.ColorDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Product.Color;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.Product.IColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/colors")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")

public class ColorController {
    private final IColorService colorService;

    @PostMapping("")
    public ResponseEntity<?> createColor(
            @Valid @RequestBody ColorDTO colorDTO, BindingResult result) {
        try {
            Color createColor = colorService.createColor(colorDTO);
            return ApiResponseUtil.successResponse("Color created successfully", createColor);

        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Đã xảy ra lỗi không xác định" + e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Color>>> getAllColors(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Color> colors = colorService.getAllColors(page, limit);
        return ApiResponseUtil.successResponse("Successfully", colors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColorById(@PathVariable int id) {
        try {
            Color color = colorService.getColorById(id);
            return ApiResponseUtil.successResponse("Successfully", color);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteColorById(@PathVariable int id) {
        try {
            colorService.deleteColorById(id);
            String message = "Xóa màu sắc thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSize(
            @PathVariable int id,
            @Valid @RequestBody ColorDTO colorDTO) {
        try {
            Color updateColor = colorService.updateColor(id, colorDTO);
            return ApiResponseUtil.successResponse("Color updated successfully", updateColor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }
}
