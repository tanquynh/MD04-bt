package ra.md04bt.dto.response;
public class ResponseProductDTO {
    private Long id;
    private String productName;
    private String imageUrl;
    private double price;

    public ResponseProductDTO() {
    }

    public ResponseProductDTO(Long id, String productName, String imageUrl, double price) {
        this.id = id;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
