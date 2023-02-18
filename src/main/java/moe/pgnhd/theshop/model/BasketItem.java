package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BasketItem implements ResultSetConstructable {
    protected int amount;
    protected Product product;

    static BasketItem from(ResultSet rs) {
        try {
            return new BasketItem(rs);
        } catch (SQLException e) {
            return null;
        }
    }

    private BasketItem(ResultSet rs) throws SQLException {
        this.amount = rs.getInt("BasketItem.amount");
        this.product = Product.from(rs);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
