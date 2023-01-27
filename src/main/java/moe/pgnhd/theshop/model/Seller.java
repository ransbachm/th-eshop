package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Seller implements ResultSetConstructable {
    private int id;
    private String firstname;
    private String lastname;



    public Seller(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Seller(ResultSet rs) throws SQLException {
        this.id = rs.getInt("Seller.id");
        this.firstname = rs.getString("Seller.firstname");
        this.lastname = rs.getString("Seller.lastname");
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
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
}
