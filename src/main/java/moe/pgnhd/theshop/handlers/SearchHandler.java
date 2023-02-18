package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.Util;
import moe.pgnhd.theshop.model.Product;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

public class SearchHandler {
    public static String handleSearch(Request req, Response res) {
        List<Product> products = Main.management.searchProducts(req.queryParams("query"));
        Map<String, Object> model = Util.getModel(req);
        model.put("products", products);
        model.put("result_count", products.size());
        model.put("search_query", req.queryParams("query"));
        return Main.render("search", model);
    }
}
