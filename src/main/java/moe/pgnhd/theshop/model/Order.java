package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Order implements ResultSetConstructable {
    protected int id;
    protected List<OrderItem> orderItems = new ArrayList<>();
    protected Date date;
    protected User user;


    static Order from(ResultSet rs) {
        try {
            return new Order(rs);
        } catch (SQLException e) {
            return null;
        }
    }

    protected Order(ResultSet rs) throws SQLException {
        this.id = rs.getInt("Order.id");
        this.date = rs.getDate("Order.date");
        this.user = User.from(rs);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
