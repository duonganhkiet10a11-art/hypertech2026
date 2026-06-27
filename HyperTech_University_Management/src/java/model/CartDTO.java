package model;

import java.util.HashMap;

public class CartDTO {

    private String email;
    private int productId;
    private int quantity;

    private HashMap<Integer, ProductDTO> cart;

    public CartDTO() {
        cart = new HashMap<>();
    }

    public CartDTO(String email, int productId, int quantity) {
        this.email = email;
        this.productId = productId;
        this.quantity = quantity;
    }

    // ================= GET SET =================
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public HashMap<Integer, ProductDTO> getCart() {
        return cart;
    }

    public void setCart(HashMap<Integer, ProductDTO> cart) {
        this.cart = cart;
    }

    // ================= ADD PRODUCT =================
    public void add(ProductDTO product) {

        if (cart == null) {
            cart = new HashMap<>();
        }

        int id = product.getId();

        if (cart.containsKey(id)) {

            ProductDTO old = cart.get(id);
            old.setQuantity(old.getQuantity() + 1);

        } else {

            product.setQuantity(1);
            cart.put(id, product);
        }
    }

    // ================= REMOVE PRODUCT =================
    public void remove(int id) {

        if (cart != null) {
            cart.remove(id);
        }
    }

    // ================= UPDATE QUANTITY =================
    public void update(int id, int quantity) {

        if (cart != null && cart.containsKey(id)) {

            if (quantity <= 0) {
                cart.remove(id);
            } else {
                cart.get(id).setQuantity(quantity);
            }
        }
    }

    // ================= CLEAR CART =================
    public void clear() {

        if (cart != null) {
            cart.clear();
        }
    }
}
