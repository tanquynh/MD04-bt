package ra.md04bt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ra.md04bt.dto.response.ResponseProductDTO;
import ra.md04bt.entity.Product;
import ra.md04bt.exceptions.CustomException;
import ra.md04bt.service.ProductService;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws CustomException {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Validated @RequestBody Product product) {
        Product createdProduct = productService.create(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @Validated @RequestBody Product product) throws CustomException {
        Product updatedProduct = productService.update(productId, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws CustomException {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseProductDTO>> searchProducts(
            @RequestParam("keyword") String searchTerm,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder
    ) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Product> foundProducts = productService.findProductsByKeyword(searchTerm, pageable);
        List<ResponseProductDTO> responseProductDTOs = foundProducts.getContent().stream()
                .map(this::mapProductToResponseProductDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseProductDTOs);
    }
    private ResponseProductDTO mapProductToResponseProductDTO(Product product) {
        return new ResponseProductDTO(
                product.getId(),
                product.getProductName(),
                product.getImageUrl(),
                product.getPrice()
        );
    }
}
