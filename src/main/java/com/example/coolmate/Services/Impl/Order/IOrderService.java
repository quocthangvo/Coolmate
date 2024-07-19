package com.example.coolmate.Services.Impl.Order;

import com.example.coolmate.Dtos.OrderDtos.OrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Order.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrderById(int id) throws DataNotFoundException;

    List<Order> findByUserId(int userId);

    Order updateOrder(int id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(int id) throws DataNotFoundException;

    Page<Order> getAllOrders(int page, int limit);

    List<Order> searchByOrderCode(String orderCode);
}
