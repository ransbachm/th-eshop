package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Produkt;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchHandler {
    public static String handleSearch(Request req, Response res) {
        List<Produkt> products = Main.verwaltung.searchProducts(req.queryParams("query"));
        Map<String, Object> model = new HashMap<>();
        model.put("products", products);
        return Main.render("search", model);
    }
}
