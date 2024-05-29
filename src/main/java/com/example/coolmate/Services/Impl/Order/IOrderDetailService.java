package com.example.coolmate.Services.Impl.Order;

import com.example.coolmate.Dtos.OrderDtos.OrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Order.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetail getOrderDetailById(int id) throws DataNotFoundException;

//    OrderDetail updateOrderDetail(int id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deleteOrderDetail(int id) throws DataNotFoundException;

    List<OrderDetail> findByOrderId(int orderId);
}
