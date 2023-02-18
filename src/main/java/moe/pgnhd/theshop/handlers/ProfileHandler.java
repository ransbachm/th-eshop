package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.Util;
import moe.pgnhd.theshop.model.Session;
import moe.pgnhd.theshop.model.User;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ProfileHandler {
    public static String handleShowProfile(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);
        Session session = req.attribute("t_session");
        User user = session.getUser();
        model.put("user", user);
        model.put("seller", Main.management.getSellerFromUser(user));
        return Main.render("profile", model);
    }

    public static String handleMakeUserSeller(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);
        Session session = req.attribute("t_session");
        boolean success = Main.management.createSeller(session.getUser());
        if (success) {
            model.put("success", true);
        }

        res.redirect("/profile");
        return "";
    }
}
