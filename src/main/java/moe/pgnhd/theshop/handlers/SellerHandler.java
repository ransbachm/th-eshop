package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Product;
import moe.pgnhd.theshop.model.Seller;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerHandler {
    public static String handleSeller(Request req, Response res) {
        Seller seller = Main.management.getSeller(req.params(":id"));
        List<Product> products = Main.management.getProductsOfSeller(req.params(":id"));
        Map<String, Object> model = new HashMap<>();
        model.put("seller", seller);
        model.put("products", products);

        return Main.render("seller", model);
    }
}
