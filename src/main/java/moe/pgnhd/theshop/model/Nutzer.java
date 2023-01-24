package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Nutzer {
    private int id;
    private String vorname;
    private String nachname;
    private String email;
    private String passwort;
    private String hausnr;
    private String strasse;
    private String plz;


    public Nutzer(ResultSet rs) throws SQLException {
        try {
            this.id = rs.getInt("Nutzer.id");
            this.vorname = rs.getString("Nutzer.vorname");
            this.nachname = rs.getString("Nutzer.nachname");
            this.email = rs.getString("Nutzer.email");
            this.passwort = rs.getString("Nutzer.passwort");
            this.hausnr = rs.getString("Nutzer.hausnr");
            this.strasse = rs.getString("Nutzer.strasse");
            this.plz = rs.getString("Nutzer.plz");
        } catch (SQLException e) {
            // object will have null attributes
        }
    }

    public Nutzer(int id, String vorname, String nachname, String email, String passwort, String hausnr, String strasse, String plz) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.passwort = passwort;
        this.hausnr = hausnr;
        this.strasse = strasse;
        this.plz = plz;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getHausnr() {
        return hausnr;
    }

    public void setHausnr(String hausnr) {
        this.hausnr = hausnr;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }
}
