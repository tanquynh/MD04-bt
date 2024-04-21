package ra.md04bt.service;

import ra.md04bt.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product create(Product product);

    Product update(Long id, Product product);

    boolean delete(Long id);

    Page<Product> findProductsByKeyword(String searchTerm, Pageable pageable);
}
