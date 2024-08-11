package com.example.coolmate.Services.Order;

import com.example.coolmate.Dtos.OrderDtos.OrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Order.OrderDetail;
import com.example.coolmate.Repositories.Order.OrderDetailRepository;
import com.example.coolmate.Repositories.Order.OrderRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Responses.Orders.OrderDetailResponse;
import com.example.coolmate.Services.Impl.Order.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderRepository orderRepository;
    private final ProductDetailRepository productDetailRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        // Tìm xem order có tồn tại không
//        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Không tìm thấy đơn hàng với id " + orderDetailDTO.getOrderId()));
//
//        // Kiểm tra xem order có active không
//        if (!order.isActive()) {
//            throw new IllegalStateException("Không thể thêm chi tiết đơn hàng vào đơn hàng không hoạt động.");
//        }
//
//        // Tìm sản phẩm theo id
//        ProductDetail productDetail = productDetailRepository.findById(orderDetailDTO.getProductDetailId())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Không tìm thấy sản phẩm với id " + orderDetailDTO.getProductDetailId()));
//
//        // Kiểm tra xem sản phẩm đã tồn tại trong chi tiết đơn hàng chưa
//        OrderDetail existingOrderDetail = orderDetailRepository.findByOrderAndProductDetail(order, productDetail);
//
//        if (existingOrderDetail != null) {
//            // Nếu sản phẩm đã tồn tại trong chi tiết đơn hàng, chỉ cần tăng số lượng đặt lên
//            existingOrderDetail.setQuantity(existingOrderDetail.getQuantity() + orderDetailDTO.getQuantity());
//            existingOrderDetail.setTotalMoney(existingOrderDetail.getPrice() * existingOrderDetail.getQuantity());
//            return orderDetailRepository.save(existingOrderDetail);
//        } else {
//            // Nếu sản phẩm chưa tồn tại trong chi tiết đơn hàng, tạo một bản ghi mới
//            OrderDetail orderDetail = OrderDetail.builder()
//                    .order(order)
//                    .productDetail(productDetail)
//                    .quantity(orderDetailDTO.getQuantity())
//                    .price(orderDetailDTO.getPrice())
//                    .totalMoney(orderDetailDTO.getTotalMoney())
//                    .build();
//            return orderDetailRepository.save(orderDetail);
//        }
        return null;
    }


    @Override
    public OrderDetail getOrderDetailById(int id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Không tìm thấy order detail id" + id));
    }

    @Override
    public OrderDetail updateOrderDetail(int id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        return null;
    }


    @Override
    public void deleteOrderDetail(int id) {
        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findById(id);
        if (orderDetailOptional.isPresent()) {
            orderDetailRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Order detail with Id " + id + " does not exist.");

        }
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(int orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        return orderDetails.stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .collect(Collectors.toList());
    }

    
}
