package moe.pgnhd.theshop;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import moe.pgnhd.theshop.model.BestellPosition;
import moe.pgnhd.theshop.model.Bestellung;
import moe.pgnhd.theshop.model.Models;
import moe.pgnhd.theshop.model.Produkt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Verwaltung {
    private static Logger LOG = LoggerFactory.getLogger(Verwaltung.class);


    // Will create connections
    private HikariDataSource ds;

    private static final String DB_URL = "jdbc:mariadb://localhost/th_eshop";
    private static final String USER = "th_eshop";
    private static final String PWD = System.getenv("TH_ESHOP_DB_PWD");

    public Verwaltung() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PWD);

        ds = new HikariDataSource(config);
        // Test connection
        ds.getConnection().createStatement().executeQuery("SELECT 1;");
    }

    public String getAnyUserFirstName()  {
        String sql = "SELECT vorname FROM Nutzer LIMIT ?";

        try(PreparedStatement stmt = ds.getConnection().prepareStatement(sql)){
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getString("vorname");
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Bestellung> getOrders() {

        String sql = "SELECT *\n" +
                "FROM Bestellposition\n" +
                "\n" +
                "JOIN Bestellung ON Bestellposition.bestellung = Bestellung.id\n" +
                "JOIN Produkt ON Bestellposition.produkt = Produkt.id\n" +
                "\n" +
                "ORDER BY Bestellung.id ASC, Bestellposition.id ASC";

        try(PreparedStatement stmt = ds.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Bestellung> bestellungen = Models.multiple_one_to_many(rs,
                    Bestellung.class, BestellPosition.class, "bestellPositionen");
            return bestellungen;
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public List<Produkt> searchProducts(String bezeichnung) {
        String sql = "SELECT *\n" +
                "FROM Produkt\n" +
                "JOIN Verkaeufer ON Produkt.verkaeufer = Verkaeufer.id\n" +
                "WHERE Produkt.bezeichnung LIKE ?\n" +
                "ORDER BY Produkt.id ASC";

        try(PreparedStatement stmt = ds.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + bezeichnung + "%");
            ResultSet rs = stmt.executeQuery();
            return Models.list_of(rs, Produkt.class);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public Produkt getProduct(String id){
       String sql =  "Select *\n" +
                     "FROM Produkt\n" +
                     "WHERE Produkt.id = ?";
        try(PreparedStatement stmt = ds.getConnection().prepareStatement(sql)) {
            stmt.setInt(1,Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return new Produkt(rs);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

}
