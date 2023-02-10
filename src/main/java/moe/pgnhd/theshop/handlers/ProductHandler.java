package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Product;
import moe.pgnhd.theshop.model.Seller;
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

    public static String handleCreateProductSubmit(Request req, Response res){
        Map<String, Object> model = new HashMap<>();

        Seller seller = Main.management.getSellerFromSession(req.attribute("t_session"));
        int sellerID;
        try {
             sellerID = seller.getId();
        } catch (NullPointerException e) {
            model.put("notSeller", true);
            return Main.render("product/create", model);
        }

        try {
            double price = Double.parseDouble(req.queryParams("price"));
            String name = req.queryParams("name").trim();
            String description = req.queryParams("description").trim();
            int available = Integer.parseInt(req.queryParams("available"));
            Main.management.registerProduct(name, price, available, description, sellerID);
        } catch (IllegalArgumentException e){
            model.put("invalidInput", true);
        }

        return Main.render("product/create", model);
    }


}
