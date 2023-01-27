package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product implements ResultSetConstructable {
    private int id;
    private double price;
    private String name;
    private int available;
    private Seller seller;

    public Product(int id, double price, String name, int available, Seller seller) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.available = available;
        this.seller = seller;
    }

    public Product(ResultSet rs) throws SQLException {
        try {
            this.id = rs.getInt("Product.id");
            this.price = rs.getDouble("Product.price");
            this.name = rs.getString("Product.name");
            this.available = rs.getInt("Product.available");
            this.seller = new Seller(rs);
        } catch (SQLException e) {
            // object will have null attributes
        }
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Seller getSeller() {
        return seller;
    }

    public void getSeller(Seller seller) {
        this.seller = seller;
    }
}
