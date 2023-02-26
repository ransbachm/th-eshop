package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product implements ResultSetConstructable {
    protected int id;
    protected double price;
    protected String name;
    protected int available;
    protected String description;

    private Seller seller;

    static Product from(ResultSet rs) {
        try {
            return new Product(rs);
        } catch (SQLException e) {
            return null;
        }
    }

    protected Product(ResultSet rs) throws SQLException {
        this.id = rs.getInt("Product.id");
        this.price = rs.getDouble("Product.price");
        this.name = rs.getString("Product.name");
        this.available = rs.getInt("Product.available");
        this.description = rs.getString("Product.description");
        this.seller = Seller.from(rs);
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

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
