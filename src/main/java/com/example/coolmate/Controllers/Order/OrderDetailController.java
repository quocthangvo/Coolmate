package com.example.coolmate.Controllers.Order;

import com.example.coolmate.Dtos.OrderDtos.OrderDetailDTO;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Order.OrderDetail;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.OrderDetailResponse;
import com.example.coolmate.Services.Order.OrderDetailService;
import com.example.coolmate.Services.Order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order_details")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")

public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("orderId") int orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        return ApiResponseUtil.successResponse("Successfully", orderDetails);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable("id") int id) {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetailById(id);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));// trả về theo from đã định dạng

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @PathVariable int id,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail updatedOrderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ApiResponseUtil.successResponse("Successfully", updatedOrderDetail);
   
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrderDetailById(
            @Valid @PathVariable("id") int id) {
        try {
            orderDetailService.deleteOrderDetail(id);
            String message = "Xóa chi tiết đơn hàng có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }
}
