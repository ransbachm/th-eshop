package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.EmptyBasketException;
import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.OutOfStockException;
import moe.pgnhd.theshop.model.BasketItem;
import moe.pgnhd.theshop.model.User;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketHandler {
    public static String show(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        User user = req.attribute("user");
        List<BasketItem> basket = Main.management.getBasketOfUser(user);
        model.put("basket", basket);

        return Main.render("my/basket", model);
    }

    public static String change(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        User user = req.attribute("user");
        int product_id = Integer.parseInt(req.queryParams("product_id"));
        int amount = Integer.parseInt(req.queryParams("amount"));

        if(amount < 0) {
            res.status(400);
            return "";
        } else if(amount == 0) {
            Main.management.deleteBasketItem(user.getId(), product_id);
        } else {
            Main.management.setBasketAmount(user.getId(), product_id, amount);
        }

        return "";
    }

    public static String order(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        User user = req.attribute("user");
        try {
            Main.management.orderBasket(user.getId());
        } catch(SQLException e) {
            return "An error occurred";
        } catch (OutOfStockException | EmptyBasketException e) {
            return e.getMessage();
        }

        res.redirect("/my/orders");
        return "";
    }
}
