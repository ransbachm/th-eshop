package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BestellPosition implements ResultSetConstructable {
    private int id;
    private int stueckzahl;
    private double preis;
    private Produkt produkt;


    public BestellPosition(ResultSet rs) throws SQLException {
        try {
            this.id = rs.getInt("Bestellposition.id");
            this.stueckzahl = rs.getInt("Bestellposition.stueckzahl");
            this.preis = rs.getDouble("Bestellposition.preis");
            this.produkt = new Produkt(rs);
        } catch (SQLException e) {
            // object will have null attributes
        }
    }

    public BestellPosition(int id, int stueckzahl, double preis, Produkt produkt) {
        this.id = id;
        this.stueckzahl = stueckzahl;
        this.preis = preis;
        this.produkt = produkt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStueckzahl() {
        return stueckzahl;
    }

    public void setStueckzahl(int stueckzahl) {
        this.stueckzahl = stueckzahl;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }
}
