package com.example.ciyashop.model;

/**
 * Created by Bhumi Shah on 12/5/2017.
 */

public class Cart {

    private int variationid, quantity,buyNow;
    private String productid;
    private int  cartId;
    private String variation;
    private CategoryList categoryList;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    private String product;

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public int getVariationid() {
        return variationid;
    }

    public void setVariationid(int variationid) {
        this.variationid = variationid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public CategoryList getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(CategoryList categoryList) {
        this.categoryList = categoryList;
    }

    public int getBuyNow() {
        return buyNow;
    }

    public void setBuyNow(int buyNow) {
        this.buyNow = buyNow;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
