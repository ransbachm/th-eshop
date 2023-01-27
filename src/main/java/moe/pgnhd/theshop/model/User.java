package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements ResultSetConstructable {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String housenumber;
    private String street;
    private String zipcode;


    public User(ResultSet rs) throws SQLException {
        try {
            this.id = rs.getInt("User.id");
            this.firstname = rs.getString("User.firstname");
            this.lastname = rs.getString("User.lastname");
            this.email = rs.getString("User.email");
            this.password = rs.getString("User.password");
            this.housenumber = rs.getString("User.housenumber");
            this.street = rs.getString("User.street");
            this.zipcode = rs.getString("User.zipcode");
        } catch (SQLException e) {
            // object will have null attributes
        }
    }

    public User(int id, String firstname, String lastname, String email, String password, String housenumber, String street, String zipcode) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.housenumber = housenumber;
        this.street = street;
        this.zipcode = zipcode;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
