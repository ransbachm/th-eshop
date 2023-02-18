package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.EmptyBasketException;
import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.OutOfStockException;
import moe.pgnhd.theshop.Util;
import moe.pgnhd.theshop.model.BasketItem;
import moe.pgnhd.theshop.model.User;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketHandler {

    private static String renderShow(Request req, Response res, Map<String, Object> model) {
        User user = req.attribute("user");
        List<BasketItem> basket = Main.management.getBasketOfUser(user);
        model.put("basket", basket);

        return Main.render("my/basket", model);
    }
    public static String show(Request req, Response res) {
        return renderShow(req, res, Util.getModel(req));
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
        Map<String, Object> model = Util.getModel(req);

        User user = req.attribute("user");
        try {
            Main.management.orderBasket(user.getId());
        } catch(SQLException e) {
            return "An error occurred";
        } catch (OutOfStockException e) {
            model.put("out_of_stock_product", e.getMessage());
            return renderShow(req, res, model);
        } catch (EmptyBasketException e) {
            model.put("submitted_empty_cart", true);
            return renderShow(req, res, model);
        }

        res.redirect("/my/orders");
        return "";
    }
}
