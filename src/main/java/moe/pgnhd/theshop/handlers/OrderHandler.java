package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.User;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class OrderHandler {
    public static String handleGetOrders(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        User user = req.attribute("user");
        model.put("orders", Main.management.getMyOrders(user));

        return Main.render("my/orders", model);
    }
}
