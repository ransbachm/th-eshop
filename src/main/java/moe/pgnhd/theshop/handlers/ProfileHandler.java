package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Session;
import moe.pgnhd.theshop.model.User;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class ProfileHandler {
    public static String handleProfile(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        Session session = req.attribute("t_session");
        User user = session.getUser();
        model.put("user", user);
        model.put("seller", Main.management.getSellerFromUser(user));
        return Main.render("profile", model);
    }

    public static String handleMakeUserSeller(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        Session session = req.attribute("t_session");
        boolean success = Main.management.createSeller(session.getUser());
        if (success) model.put("success", true);

        res.redirect("/profile");
        return "";
    }
}
