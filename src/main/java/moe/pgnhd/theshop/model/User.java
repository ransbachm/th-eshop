package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements ResultSetConstructable {
    protected int id;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String pwdhash;
    protected String salt;
    protected String housenumber;
    protected String street;
    protected String zipcode;
    protected boolean active;
    protected String activationcode;

    static User from(ResultSet rs) {
        try {
            return new User(rs);
        } catch (SQLException e) {
            return null;
        }

    }

    protected User(ResultSet rs) throws SQLException {
        this.id = rs.getInt("User.id");
        this.firstname = rs.getString("User.firstname");
        this.lastname = rs.getString("User.lastname");
        this.email = rs.getString("User.email");
        this.pwdhash = rs.getString("User.pwdhash");
        this.salt = rs.getString("User.salt");
        this.housenumber = rs.getString("User.housenumber");
        this.street = rs.getString("User.street");
        this.zipcode = rs.getString("User.zipcode");
        this.active = rs.getBoolean("User.active");
        this.activationcode = rs.getString("User.activationcode");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwdhash() {
        return pwdhash;
    }

    public void setPwdhash(String pwdhash) {
        this.pwdhash = pwdhash;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivationcode() {
        return activationcode;
    }

    public void setActivationcode(String activationcode) {
        this.activationcode = activationcode;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
