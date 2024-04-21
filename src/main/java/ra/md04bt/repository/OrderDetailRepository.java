package ra.md04bt.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.md04bt.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> getOrderDetailByOrder_Id(Long id);
}
