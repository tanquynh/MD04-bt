package ra.md04bt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md04bt.dto.request.OrderCreationDTO;
import ra.md04bt.dto.request.OrderDetailDTO;
import ra.md04bt.dto.response.ResponseListOrderDTO;
import ra.md04bt.dto.response.ResponseOrderDTO;
import ra.md04bt.dto.response.ResponseOrderDetailDTO;
import ra.md04bt.entity.Order;
import ra.md04bt.entity.OrderDetail;
import ra.md04bt.entity.Product;
import ra.md04bt.entity.User;
import ra.md04bt.service.OrderDetailService;
import ra.md04bt.service.OrderService;
import ra.md04bt.service.ProductService;
import ra.md04bt.service.UserService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/orders")
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    @Autowired
    public OrderController(UserService userService, OrderService orderService, OrderDetailService orderDetailService, ProductService productService) {
        this.userService = userService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
    }

    //    GET ALL
    @GetMapping
    public ResponseEntity<List<ResponseListOrderDTO>> getAllOrders() {
        List<ResponseListOrderDTO> responseListOrderDTOs = orderService.getAllOrders().stream()
                .map(this::mapOrderToListOrderDTO)
                .toList();
        return ResponseEntity.ok(responseListOrderDTOs);
    }

    //    GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseOrderDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            ResponseOrderDTO responseOrderDTO = mapOrderToResponseOrderDTO(order);
            return ResponseEntity.ok(responseOrderDTO);
        }
        return ResponseEntity.notFound().build();
    }


    // CREATE
    @PostMapping
    public ResponseEntity<ResponseOrderDTO> create(@RequestBody OrderCreationDTO orderDTO) {
        User user = userService.getUserById(orderDTO.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Order savedOrder = saveOrderDetails(orderDTO, user);
        if (savedOrder == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ResponseOrderDTO responseOrderDTO = mapOrderToResponseOrderDTO(savedOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrderDTO);
    }



    //    UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order updateOrder) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            Order updatedOrder = orderService.update(id, order);
            return ResponseEntity.ok(updatedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    //    DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return orderService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    //    TODO: SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<ResponseOrderDTO>> search(
            @RequestParam("keyword") String searchTerm,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder
    ) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Order> foundOrders = orderService.findOrdersByKeyword(searchTerm, pageable);
        List<ResponseOrderDTO> responseOrderDTOs = foundOrders.stream()
                .map(this::mapOrderToResponseOrderDTO)
                .toList();

        return ResponseEntity.ok(responseOrderDTOs);
    }
//    mapping methods
    private Order saveOrderDetails(OrderCreationDTO orderDTO, User user) {
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setNotes(orderDTO.getNotes());
        order.setUser(user);

        Order savedOrder = orderService.save(order);

        List<OrderDetailDTO> orderDetailDTOs = orderDTO.getOrderDetails();
        if (orderDetailDTOs != null && !orderDetailDTOs.isEmpty()) {
            for (OrderDetailDTO orderDetailDTO : orderDetailDTOs) {
                Product product = productService.getProductById(orderDetailDTO.getProductId());
                if (product != null) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(savedOrder);
                    orderDetail.setProduct(product);
                    orderDetail.setQuantity(orderDetailDTO.getQuantity());
                    orderDetailService.save(orderDetail);
                }
            }
        }
        return savedOrder;
    }
    private ResponseListOrderDTO mapOrderToListOrderDTO(Order order) {
        ResponseListOrderDTO responseListOrderDTO = new ResponseListOrderDTO();
        responseListOrderDTO.setOrderId(order.getId());
        responseListOrderDTO.setUserName(order.getUser().getUserName());
        responseListOrderDTO.setPhone(order.getPhoneNumber());
        responseListOrderDTO.setAddress(order.getShippingAddress());
        return responseListOrderDTO;
    }

    private ResponseOrderDTO mapOrderToResponseOrderDTO(Order savedOrder) {
        ResponseOrderDTO responseOrderDTO = new ResponseOrderDTO();
        responseOrderDTO.setId(savedOrder.getId());
        responseOrderDTO.setUserName(savedOrder.getUser().getUserName());

        //map OrderDetail to ResponseOrderDetailDTO
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(savedOrder.getId());
        List<ResponseOrderDetailDTO> responseOrderDetailDTOs = orderDetails.stream().map(orderDetail -> {
            ResponseOrderDetailDTO responseOrderDetailDTO = new ResponseOrderDetailDTO();
            responseOrderDetailDTO.setProductName(orderDetail.getProduct().getProductName());
            responseOrderDetailDTO.setImageUrl(orderDetail.getProduct().getImageUrl());;
            responseOrderDetailDTO.setPrice(orderDetail.getProduct().getPrice());
            responseOrderDetailDTO.setQuantity(orderDetail.getQuantity());
            return responseOrderDetailDTO;
        }).toList();
        responseOrderDTO.setOrderDetails(responseOrderDetailDTOs);
        return responseOrderDTO;
    }
}
