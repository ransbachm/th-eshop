package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Produkt;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ProductHandler {
    public static String handleProducts(Request req, Response res) {
        Produkt product = Main.verwaltung.getProduct(req.params(":id"));
        Map<String, Object> model = new HashMap<>();
        model.put("product", product);

        return Main.render("product", model);
    }
}
