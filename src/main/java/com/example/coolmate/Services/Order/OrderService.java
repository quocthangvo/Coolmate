package com.example.coolmate.Services.Order;

import com.example.coolmate.Dtos.OrderDtos.OrderDTO;
import com.example.coolmate.Dtos.OrderDtos.OrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Models.Order.Order;
import com.example.coolmate.Models.Order.OrderDetail;
import com.example.coolmate.Models.Order.OrderStatus;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Repositories.InventoryRepository;
import com.example.coolmate.Repositories.Order.OrderDetailRepository;
import com.example.coolmate.Repositories.Order.OrderRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Repositories.UserRepository;
import com.example.coolmate.Services.Impl.Order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProductDetailRepository productDetailRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        // Tìm xem user có tồn tại không
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));

        // Convert từ OrderDTO sang Order sử dụng Model Mapper
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setUser(user);
        LocalDateTime orderDateTime = LocalDateTime.now();
        order.setOrderDate(orderDateTime);

        // Thiết lập ngày giao hàng dự kiến sau 3 ngày từ ngày đặt
        LocalDateTime expectedShippingDateTime = orderDateTime.plusDays(3);
        order.setShippingDate(expectedShippingDateTime);

        // Kiểm tra và cập nhật ngày giao hàng thực tế nếu có
        LocalDateTime actualShippingDateTime = orderDTO.getShippingDate();
        if (actualShippingDateTime != null) {
            order.setShippingDate(actualShippingDateTime); // Sử dụng ngày giao hàng thực tế nếu được cung cấp
        }

        // Tạo mã đơn hàng ngẫu nhiên với tiền tố "OR-"
        String orderCode = generateOrderCode();
        order.setOrderCode(orderCode); // Đảm bảo bạn có trường orderCode trong entity Order

        order.setStatus(OrderStatus.PENDING);
        order.setActive(true);

        // Lưu đơn hàng vào cơ sở dữ liệu
        order = orderRepository.save(order);

        // Tạo danh sách chi tiết đơn hàng và lưu vào cơ sở dữ liệu
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
            ProductDetail productDetail = productDetailRepository.findById(detailDTO.getProductDetailId())
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + detailDTO.getProductDetailId()));

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productDetail(productDetail)
                    .quantity(detailDTO.getQuantity())
                    .price(detailDTO.getPrice())
                    .totalMoney(detailDTO.getTotalMoney())
                    .build();

            orderDetails.add(orderDetailRepository.save(orderDetail));
        }

        // Cập nhật danh sách chi tiết đơn hàng vào đơn hàng chính
        order.setOrderDetails(orderDetails);

        return order;
    }

    // Phương thức để tạo mã đơn hàng ngẫu nhiên
    private String generateOrderCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("OR-");
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
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


    public Page<Order> getAllOrders(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit); // PageRequest uses 0-based index
        return orderRepository.findAll(pageable);
    }

    @Override
    public void deleteOrder(int id) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Không tìm thấy order id: " + id));

        // Kiểm tra xem đơn hàng đã bị xóa mềm hay chưa
        if (!order.isActive()) {
            throw new IllegalStateException("Đơn hàng đã bị xóa.");
        }

        // Cập nhật trạng thái thành "cancelled" và đặt active thành false
        order.setStatus(OrderStatus.CANCELLED);
        order.setActive(false);
        orderRepository.save(order);
    }


    @Override
    public Order updateOrder(int orderId, OrderDTO orderDTO) throws DataNotFoundException {
        // Tìm Order theo ID
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết đơn đặt hàng với ID: " + orderId));

        // Lấy trạng thái mới từ DTO
        String newStatusString = orderDTO.getStatus();
        OrderStatus newStatus;

        // Kiểm tra xem trạng thái mới có hợp lệ không
        try {
            newStatus = OrderStatus.valueOf(newStatusString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + newStatusString);
        }

        // Kiểm tra tính hợp lệ của trạng thái chuyển tiếp
        switch (order.getStatus()) {
            case PENDING:
                if (newStatus != OrderStatus.CANCELLED && newStatus != OrderStatus.PROCESSING) {
                    throw new IllegalStateException("Không thể chuyển từ 'Chờ xử lý' sang " + newStatus.getStatusName());
                }
                break;
            case PROCESSING:
                if (newStatus != OrderStatus.SHIPPING) {
                    throw new IllegalStateException("Không thể chuyển từ 'Xác nhận' sang " + newStatus.getStatusName());
                }
                break;
            case SHIPPING:
                if (newStatus != OrderStatus.DELIVERED) {
                    throw new IllegalStateException("Không thể chuyển từ 'Đang giao hàng' sang " + newStatus.getStatusName());
                }
                break;
            default:
                throw new IllegalStateException("Không thể cập nhật đơn hàng có trạng thái: " + order.getStatus().getStatusName());
        }

        // Cập nhật thuộc tính trạng thái
        order.setStatus(newStatus);

        // Nếu trạng thái mới là DELIVERED, cập nhật số lượng tồn kho
        if (newStatus == OrderStatus.PROCESSING) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail detail : orderDetails) {
                ProductDetail productDetail = detail.getProductDetail();
                int orderedQuantity = detail.getQuantity();

                // Lấy thông tin kho hiện tại
                Inventory inventory = inventoryRepository.findByProductDetail(productDetail);

                // Kiểm tra nếu không có bản ghi kho cho sản phẩm này, tạo mới
                if (inventory == null) {
                    throw new DataNotFoundException("Không tìm thấy kho có sản phẩm: " + productDetail.getId());
                }

                // Cập nhật số lượng tồn kho
                inventory.setInventoryQuantity(inventory.getInventoryQuantity() - orderedQuantity);
                inventoryRepository.save(inventory);
            }
        }
        // Lưu
        return orderRepository.save(order);
    }

    public List<Order> searchByOrderCode(String orderCode) {
        return orderRepository.findByOrderCodeContaining(orderCode);
    }
}
