package moe.pgnhd.theshop.handlers.Filters;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static spark.Spark.halt;
public class RequireLogin {
    private static Logger LOG = LoggerFactory.getLogger(RequireLogin.class);

    private static class Entry {
        public boolean exact;
        public String path;

        public Entry(String[] split) {
            this.exact = split[0].equals("exact") ? true : false;
            this.path = split[1];
        }
    }

    private static List<Entry> whitelist = new ArrayList<>();
    static {

        InputStream is = RequireLogin.class.getResourceAsStream("/whitelist.list");
        Scanner scan = new Scanner(is);
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            if(line.startsWith("#") || line.isBlank()) {
                // ignore
            } else {
                whitelist.add(new Entry(line.split(" ")));
            }
        }

    }

    private static boolean onWhitelist(String path) {
        for(Entry e : whitelist) {
            if(e.exact) {
                if(path.equals(e.path) || path.equals(e.path+"/")) {
                    return true;
                }
            } else {
                if(path.startsWith(e.path)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static void redirect_login(Request req, Response res) {
        res.cookie("login_redirect_back", req.pathInfo());
        res.redirect("/login");
        halt(); // prevents request from being processed further
    }

    // Ensures user is logged in while on non whitelisted resource
    // On whitelisted resources user/session may be null
    public static void filterRequireLogin(Request req, Response res) {
        Session session;
        boolean onWhiteList = onWhitelist(req.pathInfo());

        // User not logged in
        if(req.cookie("t_session_id") == null) {
            // Creates a session
            session = Main.management.createSession();

            // Makes the client remember the session id
            res.cookie("t_session_id", session.getId());
        } else { // user logged in
            session = Main.management
                    .getSession(req.cookie("t_session_id"));

            if(session == null) {
                // Creates a session
                session = Main.management.createSession();

                // Makes the client remember the session id
                res.cookie("t_session_id", session.getId());

                // Stores session + user for handlers after
                req.attribute("t_session", session);
                
                if (!onWhiteList) {
                    redirect_login(req, res);
                }
                return;
            }

            // Stores session + user for handlers after
            req.attribute("t_session", session);
            req.attribute("user", session.getUser());

            // Also get seller if user is seller
            req.attribute("seller", Main.management.getSellerFromUser(session.getUser()));
        }

        boolean loggedIn = session.isLogged_in();
        boolean expired = session.getUntil().isBefore(Instant.now());

        if(onWhiteList) {
            return;
        } else if(expired) {
            redirect_login(req, res);
            return;
        } else if(loggedIn) {
            return;
        } else {
            redirect_login(req, res);
        }

    }
}
