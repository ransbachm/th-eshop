package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItem implements ResultSetConstructable {
    protected int id;
    protected int amount;
    protected double price;
    protected Product product;
    protected Order order;

    static OrderItem from(ResultSet rs) {
        try {
            return new OrderItem(rs);
        } catch (SQLException e) {
            return null;
        }
    }

    protected OrderItem(ResultSet rs) throws SQLException {
        this.id = rs.getInt("OrderItem.id");
        this.amount = rs.getInt("OrderItem.amount");
        this.price = rs.getDouble("OrderItem.price");
        this.product = Product.from(rs);
        this.order = Order.from(rs);
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
