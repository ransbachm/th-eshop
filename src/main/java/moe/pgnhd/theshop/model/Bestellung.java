package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Bestellung {
    private int id;
    private List<BestellPosition> bestellPositionen = new ArrayList<>();
    private Date kaufdatum;
    private Nutzer nutzer;

    public Bestellung(int id, Date kaufdatum, Nutzer nutzer) {
        this.id = id;
        this.kaufdatum = kaufdatum;
        this.nutzer = nutzer;
    }

    public Bestellung(ResultSet rs) throws SQLException {
        try {
            this.id = rs.getInt("Bestellung.id");
            this.kaufdatum = rs.getDate("Bestellung.kaufdatum");
            this.nutzer = new Nutzer(rs);
        } catch (SQLException e) {
            // object will have null attributes
        }
    }

    public void addBestellPosition(BestellPosition bestellPosition) {
        bestellPositionen.add(bestellPosition);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<BestellPosition> getBestellPositionen() {
        return bestellPositionen;
    }

    public void setBestellPositionen(List<BestellPosition> bestellPositionen) {
        this.bestellPositionen = bestellPositionen;
    }

    public Date getKaufdatum() {
        return kaufdatum;
    }

    public void setKaufdatum(Date kaufdatum) {
        this.kaufdatum = kaufdatum;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }
}
