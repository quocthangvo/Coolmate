package com.example.coolmate.Services;

import com.example.coolmate.Dtos.OrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Order.Order;
import com.example.coolmate.Models.Order.OrderDetail;
import com.example.coolmate.Models.Product;
import com.example.coolmate.Repositories.Order.OrderDetailRepository;
import com.example.coolmate.Repositories.Order.OrderRepository;
import com.example.coolmate.Repositories.Product.ProductRepository;
import com.example.coolmate.Services.Impl.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        //tìm xem order có tồn tại ko
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find order with id " + orderDetailDTO.getOrderId()));
        //tìm product theo id
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id " + orderDetailDTO.getOrderId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(orderDetailDTO.getQuantity())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        //lưu vào db
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetailById(int id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Không tìm thấy order detail id" + id));
    }

//    @Override
//    public OrderDetail updateOrderDetail(int id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
//        // tìm kiếm  order detail có tồn tại ko
//        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Cannot find order detail with id" + id));
//        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
//                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id" + id));
//        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Cannot find product with id" + orderDetailDTO.getProductId()));
//        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
//        existingOrderDetail.setQuantity(orderDetailDTO.getQuantity());
//        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
//        existingOrderDetail.setColor(orderDetailDTO.getColor());
//        existingOrderDetail.setOrder(existingOrder);
//        existingOrderDetail.setProduct(existingProduct);
//        return orderDetailRepository.save(existingOrderDetail);
//    }

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
    public List<OrderDetail> findByOrderId(int orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
