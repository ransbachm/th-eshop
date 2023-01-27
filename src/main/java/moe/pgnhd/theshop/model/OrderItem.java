package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItem implements ResultSetConstructable {
    private int id;
    private int amount;
    private double price;
    private Product product;


    public OrderItem(ResultSet rs) throws SQLException {
        try {
            this.id = rs.getInt("OrderItem.id");
            this.amount = rs.getInt("OrderItem.amount");
            this.price = rs.getDouble("OrderItem.price");
            this.product = new Product(rs);
        } catch (SQLException e) {
            // object will have null attributes
        }
    }

    public OrderItem(int id, int amount, double price, Product product) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
