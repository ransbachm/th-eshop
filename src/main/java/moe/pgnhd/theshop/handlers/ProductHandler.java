package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Product;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ProductHandler {
    public static String handleShowProduct(Request req, Response res) {
        Product product = Main.management.getProduct(req.params(":id"));
        Map<String, Object> model = new HashMap<>();
        model.put("product", product);

        return Main.render("product/show", model);
    }

    public static String handleCreateProduct(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        return Main.render("product/create", model);
    }

    public static String addToBasket(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        int product_id = Integer.parseInt(req.queryParams("id"));
        int amount = Integer.parseInt(req.queryParams("amount"));
        User user = req.attribute("user");

        if(amount <= 0) {
            res.status(400);
            return "Invalid input";
        }

        Main.management.addProductToBasket(product_id, user, amount);

        res.redirect("/product/" + product_id);
        return "";
    }
}
