package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Verkauufer implements ResultSetConstructable {
    private int id;
    private String vorname;
    private String nachname;



    public Verkauufer(int id, String vorname, String nachname) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Verkauufer(ResultSet rs) throws SQLException {
        this.id = rs.getInt("Verkaeufer.id");
        this.vorname = rs.getString("Verkaeufer.vorname");
        this.nachname = rs.getString("Verkaeufer.nachname");
    }

    @Override
    public String toString() {
        return vorname + " " + nachname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
}
