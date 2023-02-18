package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.User;
import moe.pgnhd.theshop.Util;
import spark.Request;
import spark.Response;

import java.util.Map;

public class OrderHandler {
    public static String handleGetOrders(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);
        model.put("orders", Main.management.getOrders());

        return Main.render("my/orders", model);
    }
}
