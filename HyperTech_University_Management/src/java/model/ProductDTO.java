package model;

public class ProductDTO {

    private int id;
    private int category_id;
    private String name;
    private String cpu;
    private String gpu;
    private String ram;
    private String ssd;
    private String screen;
    private String refresh_rate;
    private float old_price;
    private float new_price;
    private int stock;
    private String description;
    private String image;
    private Boolean status;

    // dùng cho cart
    private int quantity = 1;
    private int discountPercent = 0;

    public ProductDTO() {
    }

    public ProductDTO(int id, int category_id, String name, String cpu, String gpu, String ram, String ssd, String screen, String refresh_rate, float old_price, float new_price, int stock, String description, String image, Boolean status) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.cpu = cpu;
        this.gpu = gpu;
        this.ram = ram;
        this.ssd = ssd;
        this.screen = screen;
        this.refresh_rate = refresh_rate;
        this.old_price = old_price;
        this.new_price = new_price;
        this.stock = stock;
        this.description = description;
        this.image = image;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getSsd() {
        return ssd;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getRefresh_rate() {
        return refresh_rate;
    }

    public void setRefresh_rate(String refresh_rate) {
        this.refresh_rate = refresh_rate;
    }

    public float getOld_price() {
        return old_price;
    }

    public void setOld_price(float old_price) {
        this.old_price = old_price;
    }

    public float getNew_price() {
        return new_price;
    }

    public void setNew_price(float new_price) {
        this.new_price = new_price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // SETTER (đã đúng)
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

// FIX 1: getter phải trả về DB
    public int getDiscountPercent() {
        return discountPercent;
    }

// FIX 2: tính giá sau discount
    public float getFinalPrice() {
        if (discountPercent > 0) {
            return old_price * (100 - discountPercent) / 100;
        }
        return new_price;
    }
}
