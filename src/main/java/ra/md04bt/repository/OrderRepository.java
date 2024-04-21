package ra.md04bt.repository;

import ra.md04bt.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.user u " +
            "LEFT JOIN FETCH o.orderDetails od " +
            "LEFT JOIN FETCH od.product p " +
            "WHERE lower(u.userName) LIKE %:searchTerm% " +
            "OR lower(o.shippingAddress) LIKE %:searchTerm% " +
            "OR lower(o.phoneNumber) LIKE %:searchTerm% " +
            "OR lower(o.notes) LIKE %:searchTerm% " +
            "OR lower(p.productName) LIKE %:searchTerm%")
    Page<Order> findOrdersByKeyword(String searchTerm, Pageable pageable);
}
