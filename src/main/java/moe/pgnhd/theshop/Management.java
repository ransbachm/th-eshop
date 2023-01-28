package moe.pgnhd.theshop;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import moe.pgnhd.theshop.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class Management {
    private static Logger LOG = LoggerFactory.getLogger(Management.class);


    // Will create connections
    private HikariDataSource ds;

    public Management() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Main.dotenv.get("SQL_DB_URL"));
        config.setUsername(Main.dotenv.get("SQL_DB_USER"));
        config.setPassword(Main.dotenv.get("SQL_DB_PWD"));

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
            return Product.from(rs);
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
            return Seller.from(rs);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public void createUser(String firstname, String lastname, String email, String pwdhash, String housenumber,
                           String street, String zipcode, String activation_code) throws SQLIntegrityConstraintViolationException {
        String sql = "INSERT \n" +
                "INTO User\n" +
                "(User.firstname, User.lastname, User.email, User.pwdhash,\n" +
                "User.housenumber, User.street, User.zipcode, User.active, User.activationcode)\n" +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, email);
            stmt.setString(4, pwdhash);
            stmt.setString(5, housenumber);
            stmt.setString(6, street);
            stmt.setString(7, zipcode);
            stmt.setBoolean(8, false);
            stmt.setString(9, activation_code);
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            LOG.info("User  [" + email + "] tried to register already existing email." );
            throw e;
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public User findUserByEmail(String email) {
        String sql = "SELECT *\n" +
                "FROM User\n" +
                "WHERE email=?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return User.from(rs);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    public User findUserByActivationCode(String activationcode) {
        String sql = "SELECT *\n" +
                "FROM User\n" +
                "WHERE activationcode=?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, activationcode);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return User.from(rs);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    public void setUserActivated(int user_id, boolean activated) {
        String sql = "UPDATE User\n" +
                "SET User.active = ?\n" +
                "WHERE User.id = ?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBoolean(1, activated);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }
}
