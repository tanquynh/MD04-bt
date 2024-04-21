package ra.md04bt.service;

import ra.md04bt.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order save(Order order);

    Order getOrderById(Long id);

    Order update(Long id, Order order);

    boolean delete(Long id);

    Page<Order> findOrdersByKeyword(String searchTerm, Pageable pageable);
}
