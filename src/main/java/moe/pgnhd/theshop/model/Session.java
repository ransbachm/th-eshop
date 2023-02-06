package moe.pgnhd.theshop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class Session implements ResultSetConstructable{
    protected String id;
    protected Instant until;
    protected boolean logged_in;
    protected User user;

    static Session from(ResultSet rs) {
        try {
            return new Session(rs);
        } catch (SQLException e) {
            return null;
        }
    }

    protected Session(ResultSet rs) throws SQLException {
        this.id = rs.getString("Session.id");
        this.until = rs.getTimestamp("Session.until").toInstant();
        this.logged_in = rs.getBoolean("Session.logged_in");
        this.user = User.from(rs);
    }

    // Create non-logged in session
    public Session(String id, Instant until) {
        this.id = id;
        this.until = until;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getUntil() {
        return until;
    }

    public void setUntil(Instant until) {
        this.until = until;
    }

    public boolean isLogged_in() {
        return logged_in;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
