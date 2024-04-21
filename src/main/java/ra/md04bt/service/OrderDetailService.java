package ra.md04bt.service;

import ra.md04bt.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getAllOrderDetails();
    List<OrderDetail> getOrderDetailsByOrderId(Long id);

    OrderDetail save(OrderDetail orderDetail);

    OrderDetail getOrderDetailById(Long id);

    OrderDetail update(Long id, OrderDetail orderDetail);

    boolean delete(Long id);
}
