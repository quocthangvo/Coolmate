package com.example.coolmate.Controllers.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/purchase_orders")
public class PurchaseOrderController {
    private final IPurchaseOrderService purchaseOrderService;

    @PostMapping("")
    public ResponseEntity<?> createPurchaseOrder(@RequestBody PurchaseOrderDTO purchaseOrderDTO,
                                                 BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            PurchaseOrder purchaseOrderResponse = purchaseOrderService.createPurchaseOrder(purchaseOrderDTO);
            return ApiResponseUtil.successResponse("PurchaseOrder created successfully", purchaseOrderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }
}
