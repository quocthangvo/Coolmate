package com.example.coolmate.Repositories.Order;

import com.example.coolmate.Models.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);

    List<Order> findByOrderCodeContaining(String orderCode);

    List<Order> findByUserIdAndStatus(int userId, String status);
}
