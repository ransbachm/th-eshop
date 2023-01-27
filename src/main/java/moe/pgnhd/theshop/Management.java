package moe.pgnhd.theshop;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import moe.pgnhd.theshop.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Management {
    private static Logger LOG = LoggerFactory.getLogger(Management.class);


    // Will create connections
    private HikariDataSource ds;

    private static final String DB_URL = "jdbc:mariadb://localhost/th_eshop";
    private static final String USER = "th_eshop";
    private static final String PWD = System.getenv("TH_ESHOP_DB_PWD");

    public Management() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PWD);

        ds = new HikariDataSource(config);
        // Test connection
        ds.getConnection().createStatement().executeQuery("SELECT 1;");
    }

    public String getAnyUserFirstName()  {
        String sql = "SELECT firstname FROM User LIMIT ?";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getString("User.firstname");
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Order> getOrders() {

        String sql = "SELECT * \n" +
                "FROM OrderItem\n" +
                "\n" +
                "JOIN `Order` ON OrderItem.`Order` = `Order`.id\n" +
                "JOIN Product ON OrderItem.product = Product.id\n" +
                "\n" +
                "ORDER BY `Order`.id ASC, OrderItem.id ASC";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Order> orders = Models.multiple_one_to_many(rs,
                    Order.class, OrderItem.class, "orderItems");
            return orders;
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public List<Product> searchProducts(String name) {
        String sql = "SELECT *\n" +
                "FROM Product\n" +
                "JOIN Seller ON Product.seller = Seller.id\n" +
                "WHERE Product.name LIKE ?\n" +
                "ORDER BY Product.id ASC";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            return Models.list_of(rs, Product.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public Product getProduct(String id){
       String sql =  "Select *\n" +
                     "FROM Product\n" +
                     "JOIN Seller on Product.seller = Seller.id\n" +
                     "WHERE Product.id = ?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1,Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return new Product(rs);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public List<Product> getProductsOfSeller(String id){
        String sql =  "Select *\n" +
                "FROM Product\n" +
                "WHERE Product.seller = ?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1,Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return Models.list_of(rs, Product.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public Seller getSeller(String id){
        String sql = "SELECT *\n" +
                     "FROM Seller\n" +
                     "WHERE Seller.id = ?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1,Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return new Seller(rs);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }
}
