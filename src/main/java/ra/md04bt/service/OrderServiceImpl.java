package ra.md04bt.service;

import ra.md04bt.entity.Order;
import ra.md04bt.entity.OrderDetail;
import ra.md04bt.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order update(Long id, Order order) {
        Optional<Order> optionalExistingOrder = orderRepository.findById(id);
        if (optionalExistingOrder.isPresent()) {
            Order existingOrder = optionalExistingOrder.get();
//            TODO: nhớ rằng việc khai báo constructor, getter setter ở entity là cơ sở để có thể gọi getter setter ở đây
            existingOrder.setUserId(order.getUserId());
            existingOrder.setShippingAddress(order.getShippingAddress());
            existingOrder.setPhoneNumber(order.getPhoneNumber());
            existingOrder.setNotes(order.getNotes());
            existingOrder.setCreatedAt(order.getCreatedAt());
            existingOrder.setStatus(order.getStatus());
            existingOrder.setUser(order.getUser());
            return orderRepository.save(existingOrder);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<Order> findOrdersByKeyword(String searchTerm, Pageable pageable) {
        return orderRepository.findOrdersByKeyword(searchTerm, pageable);
    }
}
