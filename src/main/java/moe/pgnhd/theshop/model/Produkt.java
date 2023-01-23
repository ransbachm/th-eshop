package moe.pgnhd.theshop.model;

import moe.pgnhd.theshop.handlers.Verkauufer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Produkt {
    private int id;
    private double preis;
    private String bezeichnung;
    private int verfuegbar;
    private Verkauufer verkauufer;

    public Produkt(int id, double preis, String bezeichnung, int verfuegbar, Verkauufer verkauufer) {
        this.id = id;
        this.preis = preis;
        this.bezeichnung = bezeichnung;
        this.verfuegbar = verfuegbar;
        this.verkauufer = verkauufer;
    }

    public Produkt(ResultSet rs) throws SQLException {
        this.id = rs.getInt("Produkt.id");
        this.preis = rs.getDouble("Produkt.preis");
        this.bezeichnung = rs.getString("Produkt.bezeichnung");
        this.verfuegbar = rs.getInt("Produkt.verfuegbar");
        this.verkauufer = new Verkauufer(rs);
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int getVerfuegbar() {
        return verfuegbar;
    }

    public void setVerfuegbar(int verfuegbar) {
        this.verfuegbar = verfuegbar;
    }

    public Verkauufer getVerkauufer() {
        return verkauufer;
    }

    public void setVerkauufer(Verkauufer verkauufer) {
        this.verkauufer = verkauufer;
    }
}
