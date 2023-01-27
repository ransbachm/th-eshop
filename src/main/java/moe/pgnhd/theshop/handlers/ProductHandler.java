package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Product;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ProductHandler {
    public static String handleProducts(Request req, Response res) {
        Product product = Main.management.getProduct(req.params(":id"));
        Map<String, Object> model = new HashMap<>();
        model.put("product", product);

        return Main.render("product", model);
    }
}
