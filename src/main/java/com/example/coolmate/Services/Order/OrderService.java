package com.example.coolmate.Services.Order;

import com.example.coolmate.Dtos.OrderDtos.OrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Order.Order;
import com.example.coolmate.Models.Order.OrderStatus;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Repositories.Order.OrderRepository;
import com.example.coolmate.Repositories.UserRepository;
import com.example.coolmate.Services.Impl.Order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        //tìm xem userid có tồn tại hay ko
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: "
                        + orderDTO.getUserId()));
        //convert orderDTO-> order
        // dùng thư viện Model Mapper
        //tạo 1 lồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        //cập nhật các truòng của đơn hàng từ orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        //kiểm tra shipping date phải > = ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() :
                orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least body");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrderById(int id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy order id = "
                        + id));
    }

    @Override
    public List<Order> findByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }

//    @Override
//    public Order updateOrder(int id, OrderDTO orderDTO) throws DataNotFoundException {
//        Order order = orderRepository.findById(id).orElseThrow(() ->
//                new DataNotFoundException("Không tìm thấy order id " + id));
//        User exixtingUser = userRepository.findById(
//                orderDTO.getUserId()).orElseThrow(() ->
//                new DateTimeException("Không tìm thấy user id" + id));
//        //tạo 1 luồng ánh xạ
//        modelMapper.typeMap(OrderDTO.class, Order.class)
//                .addMappings(mapper -> mapper.skip(Order::setId));
//        //cập nhật  các trường của đơn hàng từ orderDTO
//        modelMapper.map(orderDTO, order);
//        order.setUser(exixtingUser);
//        return orderRepository.save(order);
//    }

    @Override
    public void deleteOrder(int id) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Không tìm thấy order id: " + id));
        // Không xóa cứng, chỉ xóa mềm
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public Order updateOrder(int orderId, OrderDTO orderDTO) throws DataNotFoundException {
// Tìm PurchaseOrderDetail theo ID
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy chi tiết đơn đặt hàng với ID: " + orderId));

        // Các trạng thái hợp lệ
        List<String> validStatuses = Arrays.asList(
                "pending", "processing", "shipping", "delivered", "cancelled");

        // Lấy trạng thái mới từ DTO
        String newStatus = orderDTO.getStatus();

        // Kiểm tra xem status mới có hợp lệ không
        if (!validStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + newStatus);
        }

        // Cập nhật thuộc tính status
        order.setStatus(newStatus);

        // Lưu lại thay đổi vào cơ sở dữ liệu
        return orderRepository.save(order);
    }


}
