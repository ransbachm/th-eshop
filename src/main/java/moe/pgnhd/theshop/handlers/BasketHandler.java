package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.*;
import moe.pgnhd.theshop.model.*;
import moe.pgnhd.theshop.model.BasketItem;
import moe.pgnhd.theshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BasketHandler {

    private static Logger LOG = LoggerFactory.getLogger(BasketHandler.class);
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
        Map<String, Object> model = Util.getModel(req);

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
            int orderId = Main.management.orderBasket(user.getId());
            model.put("order", Main.management.getOrder(orderId));
            model.put("total-price", calculateTotalPrice((Order) model.get("order")));
            Mail.sendReceiptMail(user.getEmail(), model);
        } catch(SQLException e) {
            return "An error occurred";
        } catch (OutOfStockException e) {
            model.put("out_of_stock_product", e.getMessage());
            return renderShow(req, res, model);
        } catch (EmptyBasketException e) {
            model.put("submitted_empty_cart", true);
            return renderShow(req, res, model);
        } catch (MessagingException e) {
            LOG.info(e.getMessage());
            model.put("email_send_failure", true);
            return renderShow(req,res,model);
        }


        res.redirect("/my/orders");
        return "";
    }

    public static double calculateTotalPrice(Order order) {
        double price = 0;
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderitem : orderItems){
            price += orderitem.getPrice();
        }
        return price;
    }
}
