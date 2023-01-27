package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class OrderHandler {
    public static String handleGetOrders(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        model.put("orders", Main.management.getOrders());

        return Main.render("orders", model);
    }
}
