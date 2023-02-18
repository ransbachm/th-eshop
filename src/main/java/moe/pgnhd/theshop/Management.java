package moe.pgnhd.theshop;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import moe.pgnhd.theshop.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
                "JOIN User ON Seller.id = User.id\n" +
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
                     "JOIN User ON Seller.id = User.id\n" +
                     "WHERE Product.id = ?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1,Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            return Models.single(rs, Product.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public List<Product> getProductsOfSeller(int id){
        String sql =  "Select *\n" +
                "FROM Product\n" +
                "WHERE Product.seller = ?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return Models.list_of(rs, Product.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public Seller getSeller(String id){
        String sql = "SELECT *\n" +
                     "FROM Seller\n" +
                     "JOIN User ON Seller.id = User.id\n" +
                     "WHERE Seller.id = ?";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1,Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            return Models.single(rs, Seller.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public void createUser(String firstname, String lastname, String email, String pwdhash, String salt, String housenumber,
                           String street, String zipcode, String activation_code) throws SQLIntegrityConstraintViolationException {
        String sql = "INSERT \n" +
                "INTO User\n" +
                "(User.firstname, User.lastname, User.email, User.pwdhash, User.salt,\n" +
                "User.housenumber, User.street, User.zipcode, User.active, User.activationcode)\n" +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, email);
            stmt.setString(4, pwdhash);
            stmt.setString(5, salt);
            stmt.setString(6, housenumber);
            stmt.setString(7, street);
            stmt.setString(8, zipcode);
            stmt.setBoolean(9, false);
            stmt.setString(10, activation_code);
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
            return Models.single(rs, User.class);
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
            return Models.single(rs, User.class);
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

    public User getUserIfSessionValid(String id) {
        String sql = "SELECT * \n" +
                "FROM `Session`\n" +
                "JOIN User ON `Session`.user = User.id\n" +
                "WHERE `Session`.until < NOW()\n" +
                "AND Session.id = ?\n" +
                "LIMIT 1";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return Models.single(rs, User.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public Session createSession() {
        String sql = "INSERT INTO Session\n" +
                "(id, until, user, logged_in) VALUES\n" +
                "(?, ?, NULL, FALSE)";
        String id = Util.randomString(64);
        Instant until = LocalDateTime.now().plusYears(1).toInstant(ZoneOffset.UTC);
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setTimestamp(2, Timestamp.from(until));
            stmt.executeUpdate();
            return new Session(id, until);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public void logInSession(String session_id, User user) {
        String sql = "UPDATE Session \n" +
                "SET Session.user = ?, \n" +
                "Session.logged_in = TRUE\n" +
                "WHERE Session.id = ?\n";
        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, session_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public Session getSession(String session_id) {
        String sql = "SELECT * FROM Session\n" +
                "LEFT JOIN User ON Session.user = User.id\n" +
                "WHERE Session.id = ?";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, session_id);
            ResultSet rs = stmt.executeQuery();
            return Models.single(rs, Session.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    // Returns the id of the product
    public int registerProduct(String name, double price, int available, String description, int seller) throws SQLException {
        String sql = "INSERT INTO `Product` \n" +
                "(Product.`price`, Product.`name`, Product.`available`, " +
                "Product.`description`, Product.`seller`) \n" +
                "VALUES (?,?,?,?,?);";
        String sql2 = "SELECT LAST_INSERT_ID();";
        try(Connection con = ds.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                PreparedStatement stmt2 = con.prepareStatement(sql2)) {
            // Insert
            stmt.setDouble(1, price);
            stmt.setString(2, name);
            stmt.setInt(3, available);
            stmt.setString(4,description);
            stmt.setInt(5, seller);
            stmt.executeUpdate();

            // Get id of inserted data
            ResultSet rs2 = stmt2.executeQuery();
            rs2.next();
            return rs2.getInt("LAST_INSERT_ID()");
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }
    public Seller getSellerFromUser(User user) {
        int id = user.getId();
        String sql = "SELECT * FROM Seller\n" +
                "JOIN User ON User.id = Seller.id\n" +
                "WHERE Seller.id = ?";
        try (Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return Models.single(rs, Seller.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    public boolean createSeller(User user){
        //Will not create a second seller

        if(getSellerFromUser(user) == null){
            String sql = "INSERT INTO `Seller` \n" +
                    "(Seller.`id`,Seller.`balance`) \n" +
                    "VALUES (?, 0);";
            try(Connection con = ds.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, user.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public void setProductAvailablity(int productID, int increase){
        String sql ="UPDATE Product\n" +
                "SET Product.available = ?\n" +
                "WHERE Product.id = ?";
        try(Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, increase);
            stmt.setInt(2, productID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }
}
