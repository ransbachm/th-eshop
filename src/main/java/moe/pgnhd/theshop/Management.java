package moe.pgnhd.theshop;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import moe.pgnhd.theshop.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Management {
    private static Logger LOG = LoggerFactory.getLogger(Management.class);


    // Will create connections
    private HikariDataSource ds;

    public Management() throws SQLException {
        String db = Main.dotenv.get("SQL_DB");
        String host = Main.dotenv.get("SQL_HOST");

        String jdbc_url = "jdbc:mariadb://" + host + "/" + db;
        LOG.error(jdbc_url);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbc_url);
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

    public List<Order> getMyOrders(User user) {
        String sql = "SELECT * \n" +
                "FROM OrderItem\n" +
                "JOIN `Order` ON OrderItem.`Order` = `Order`.id\n" +
                "JOIN Product ON OrderItem.product = Product.id\n" +
                "JOIN User ON Order.user = User.id\n" +
                "WHERE User.id = ?\n" +
                "ORDER BY `Order`.id ASC, OrderItem.id ASC";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            return Models.multiple_one_to_many(rs, Order.class, OrderItem.class, "orderItems");
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public void addProductToBasket(int product_id, User user, int amount) {
        String sql = "INSERT INTO BasketItem\n" +
                "(product, user, amount) VALUES\n" +
                "(?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "amount = amount + ?\n";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, product_id);
            stmt.setInt(2, user.getId());
            stmt.setInt(3, amount);
            stmt.setInt(4, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public List<BasketItem> getBasketOfUser(User user) {
        String sql = "SELECT *\n" +
                "FROM BasketItem\n" +
                "JOIN Product ON BasketItem.product = Product.id\n" +
                "JOIN Seller ON Product.seller = Seller.id\n" +
                "JOIN User ON Seller.id = User.id\n" +
                "WHERE BasketItem.`user` = ?";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            return Models.list_of(rs, BasketItem.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public void setBasketAmount(int user_id, int product_id, int amount) {
        String sql = "UPDATE BasketItem\n" +
                "SET BasketItem.amount = ?\n" +
                "WHERE BasketItem.product = ?\n" +
                "AND BasketItem.`user` = ?";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setInt(2, product_id);
            stmt.setInt(3, user_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public void deleteBasketItem(int user_id, int product_id) {
        String sql = "DELETE\n" +
                "FROM BasketItem\n" +
                "WHERE BasketItem.product = ?\n" +
                "AND BasketItem.`user` = ?";

        try(Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, product_id);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public void orderBasket(int user_id) throws SQLException {
        try(Connection con = ds.getConnection()) {
            try {
                con.setAutoCommit(false); // start transaction

                // Set variables
                String sql1 = "SET @user_id = ?;";
                PreparedStatement ps1 = con.prepareStatement(sql1);
                ps1.setInt(1, user_id);
                ps1.execute();

                // Check if empty order
                String sql2 = "SELECT * FROM BasketItem\n" +
                        "WHERE BasketItem.user = @user_id";
                ResultSet rs2 = con.prepareStatement(sql2).executeQuery();
                if(!rs2.next()) {
                    throw new EmptyBasketException("Your basket is empty");
                }


                // Check if every product is in stock
                String sql3 = "SELECT Product.*, Product.available - BasketItem.amount AS test\n" +
                        "FROM BasketItem\n" +
                        "JOIN Product ON BasketItem.product = Product.id\n" +
                        "WHERE USER = ?";
                PreparedStatement ps3 = con.prepareStatement(sql3);
                ps3.setInt(1, user_id);
                ResultSet rs3 = ps3.executeQuery();
                while(rs3.next()) {
                    int product_id = rs3.getInt("Product.id");
                    int test = rs3.getInt("test");
                    if(test < 0) {
                        String product_name = rs3.getString("Product.name");
                        throw new OutOfStockException(product_name);
                    }
                }


                // Create new Order
                String sql4 = "INSERT INTO `Order`\n" +
                        "(date, user)\n" +
                        "VALUES(NOW(), @user_id);";
                con.prepareStatement(sql4).executeUpdate();

                // Store order_id
                String sql5 = "SET @order_id = LAST_INSERT_ID();";
                con.prepareStatement(sql5).execute();

                // Create OrderItems for BasketItems
                String sql6 = "INSERT INTO OrderItem\n" +
                        "(amount, price, product, `order`)\n" +
                        "SELECT BasketItem.amount, Product.price, BasketItem.product, @order_id\n" +
                        "FROM BasketItem \n" +
                        "JOIN Product ON BasketItem.product = Product.id\n" +
                        "WHERE BasketItem.user = @user_id;";
                con.prepareStatement(sql6).executeUpdate();

                //  Pay seller correct amounts
                String sql7 = "UPDATE Seller s\n" +
                        "SET balance = balance + \n" +
                        "\t(\n" +
                        "\tSELECT SUM(Product.price * BasketItem.amount)\n" +
                        "\tFROM BasketItem\n" +
                        "\tJOIN Product ON BasketItem.product = Product.id\n" +
                        "\tWHERE Product.seller = s.id \n" +
                        "\t)\n" +
                        "WHERE s.id IN \n" +
                        "\t(\n" +
                        "\tSELECT DISTINCT Product.seller\n" +
                        "\tFROM BasketItem\n" +
                        "\tJOIN Product ON BasketItem.product = Product.id\n" +
                        "\tWHERE Product.seller = s.id\n" +
                        "\t);";
                con.prepareStatement(sql7).executeUpdate();

                // Reduce available products by bought amount
                String sql8 = "UPDATE Product\n" +
                        "JOIN BasketItem ON Product.id = BasketItem.product\n" +
                        "SET Product.available = Product.available - BasketItem.amount\n" +
                        "WHERE BasketItem.user = @user_id;";
                con.prepareStatement(sql8).executeUpdate();

                // Clear Basket
                String sql9 = "DELETE FROM BasketItem\n" +
                        "WHERE BasketItem.user = @user_id;";
                con.prepareStatement(sql9).executeUpdate();
                
                con.commit();
            } catch(OutOfStockException | EmptyBasketException e) {
                con.rollback();
                throw e;
            } catch (Exception e) {
                con.rollback();
                LOG.error(e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            throw e;
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

    public List<Product> recommend_random(int user_id, int max) {
        String sql ="SELECT * \n" +
                "FROM Product\n" +
                "Order BY RAND()\n" +
                "LIMIT ?";
        try(Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, max);
            ResultSet rs = stmt.executeQuery();
            return Models.multiple(rs, Product.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public List<Product> recommend_for_product(int user_id, int product_id, int max) {
        return recommend_random(user_id, max);
    }

    private static List<Integer> get_recommendations_for_user(int user_id, int max) {
        try {
            String[] args = new String[] {"python", "calc.py", String.valueOf(user_id)};
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.directory(new File("py"));
            Process p = pb.start();

            p.waitFor();

            Scanner scan = new Scanner(new BufferedReader(new InputStreamReader(p.getInputStream())));

            List<Integer> recommendations = new ArrayList<>();
            for(int i=0; scan.hasNext() && i<max; i++) {
                String[] split = scan.nextLine().split(",");
                int product_id = Integer.parseInt(split[0]);
                recommendations.add(product_id);
            }
            scan.close();
            return recommendations;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> recommend_for_user(int user_id, int max) {
        List<Integer> reco_ids = get_recommendations_for_user(user_id, max);
        if(reco_ids != null) {
            return getProducts(reco_ids);
        }
        return null;
    }

    private String get_question_marks_for_in_clause(int amount) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i=0; i<amount-1; i++) {
            sb.append("?,");
        }
        sb.append("?)");
        return sb.toString();
    }

    public void insert_ints_into_in_clause(PreparedStatement ps, int start_index, List<Integer> in) throws SQLException {
        for(int i=0; i<in.size(); i++) {
            ps.setInt(start_index+i, in.get(i));
        }
    }

    public List<Product> getProducts(List<Integer> ids) {
        String sql ="SELECT * \n" +
                "FROM Product\n" +
                "WHERE Product.id IN";
        sql += get_question_marks_for_in_clause(ids.size());
        sql += ";";

        try(Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {

            insert_ints_into_in_clause(stmt, 1, ids);
            ResultSet rs = stmt.executeQuery();

            return Models.multiple(rs, Product.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }
}
