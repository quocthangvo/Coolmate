package com.example.coolmate.Controllers.Order;

import com.example.coolmate.Dtos.OrderDtos.OrderDTO;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Order.Order;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.OrderResponse;
import com.example.coolmate.Services.Impl.Order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Order orderResponse = orderService.createOrder(orderDTO);
            return ApiResponseUtil.successResponse("Order created successfully", orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") int orderId) {
        try {
            Order existingOrder = orderService.getOrderById(orderId);
            return ResponseEntity.ok(OrderResponse.fromOrder(existingOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersByUser(@Valid @PathVariable("user_id") int userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            List<OrderResponse> response = OrderResponse.fromOrderList(orders);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePurchaseOrder(
            @PathVariable int id,
            @Valid @RequestBody OrderDTO orderDTO) {
        try {
            Order updateOrder = orderService.updateOrder(id, orderDTO);
            OrderResponse orderResponse = OrderResponse.fromOrder(updateOrder);
            return ApiResponseUtil.successResponse(
                    "Purchase Order Detail created successfully", orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable int id) throws Exception {
        //xóa mềm => cập nhật trường active = false
        //không xóa mất đi order, mà chỉ xóa để active trở về 0
        try {
            orderService.deleteOrder(id);
            String message = "Xóa đơn hàng có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }

    }

}
