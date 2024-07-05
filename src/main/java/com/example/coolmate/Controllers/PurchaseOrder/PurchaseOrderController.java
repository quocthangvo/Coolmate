package com.example.coolmate.Controllers.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.PurchaseOrderResponse;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/purchase_orders")
@CrossOrigin("http://localhost:3000")


public class PurchaseOrderController {
    private final IPurchaseOrderService purchaseOrderService;

    @PostMapping("")
    public ResponseEntity<?> createPurchaseOrder(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderDTO);
            PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderResponse.fromPurchase(purchaseOrder);
            return ApiResponseUtil.successResponse(
                    "Purchase Order Detail created successfully", purchaseOrderResponse);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseOrderById(@PathVariable("id") int purchaseChaseId) {
        try {
            PurchaseOrder existingPurchaseOrder = purchaseOrderService
                    .getPurchaseOrderById(purchaseChaseId);
            PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderResponse.fromPurchase(existingPurchaseOrder);
            return ApiResponseUtil.successResponse(
                    "Purchase Order Detail created successfully", purchaseOrderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponse>>> getAllPurchaseOrders(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders(page, limit);

        // Chuyển đổi từ PurchaseOrder sang PurchaseOrderResponse
        List<PurchaseOrderResponse> purchaseOrderResponses = purchaseOrders.stream()
                .map(PurchaseOrderResponse::fromPurchase)
                .collect(Collectors.toList());

        return ApiResponseUtil.successResponse("Successfully", purchaseOrderResponses);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePurchaseOrder(@Valid @PathVariable int id) throws Exception {
        //xóa mềm => cập nhật trường active = false
        //không xóa mất đi order, mà chỉ xóa để active trở về 0
        try {
            purchaseOrderService.deletePurchaseOrder(id);
            String message = "Xóa purchase order có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePurchaseOrder(
            @PathVariable int id,
            @Valid @RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        try {
            PurchaseOrder updatePurchaseOrder = purchaseOrderService.updatePurchaseOrder(id, purchaseOrderDTO);
            PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderResponse.fromPurchase(updatePurchaseOrder);
            return ApiResponseUtil.successResponse(
                    "Purchase Order Detail created successfully", purchaseOrderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }
}
