package moe.pgnhd.theshop;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import moe.pgnhd.theshop.model.BestellPosition;
import moe.pgnhd.theshop.model.Bestellung;
import moe.pgnhd.theshop.model.Models;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
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

}