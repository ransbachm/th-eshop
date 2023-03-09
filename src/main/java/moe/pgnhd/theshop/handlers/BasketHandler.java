package moe.pgnhd.theshop.handlers;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import moe.pgnhd.theshop.*;
import moe.pgnhd.theshop.model.*;
import moe.pgnhd.theshop.model.BasketItem;
import moe.pgnhd.theshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BasketHandler {

    private static Logger LOG = LoggerFactory.getLogger(BasketHandler.class);
    private static String renderShow(Request req, Response res, Map<String, Object> model) {
        User user = req.attribute("user");
        List<BasketItem> basket = Main.management.getBasketOfUser(user);
        model.put("basket", basket);

        double total = basket.stream()
                .mapToDouble((e) -> e.getAmount() * e.getProduct().getPrice())
                .sum();

        String client_token = Main.payments.generate_client_token();
        model.put("client_token", client_token);
        model.put("total-price", total);

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
        Connection con = null;
        try {
            try {
                String nonce = req.queryParams("nonce");
                if(nonce == null || nonce.isBlank()) {
                    res.status(400);
                    return "no nonce provided";
                }

                Object[] b_res = Main.management.order_basket_no_commit(user.getId());
                con = (Connection) b_res[0];
                int orderId = (int) b_res[1];
                BigDecimal amount = (BigDecimal) b_res[2];

                Result<Transaction> t_res = Main.payments.sale(nonce, amount);
                LOG.info(t_res.toString());

                if(!t_res.isSuccess()) {
                    throw new PaymentException("Payment failed: " + t_res.getMessage());
                }

                // Payment was made -> transaction cannot be rolled back now!
                Main.management.order_basket_complete(con);

                model.put("order", Main.management.getOrder(orderId));
                model.put("total-price", amount);

                try {
                    Mail.sendReceiptMail(user.getEmail(), model);
                } catch (Exception e) {
                    throw new EmailSendException("Sending email failed", e);
                }
            } catch(SQLException e) {
                LOG.error(e.getMessage());
                con.rollback();
                return "An error occurred";
            } catch (OutOfStockException e) {
                model.put("out_of_stock_product", e.getMessage());
                return renderShow(req, res, model);
            } catch (EmptyBasketException e) {
                model.put("submitted_empty_cart", true);
                return renderShow(req, res, model);
            } catch (EmailSendException e) {
                // DO NOT roll back
                LOG.error(e.getMessage());
                model.put("email_send_failure", true);
                return renderShow(req,res,model);
            } catch (PaymentException e) {
                LOG.error(e.getMessage());
                con.rollback();
                res.status(500);
                return "There was an error with your payment. Please contact support";
            } catch (Exception e) {
                LOG.error(e.getMessage());
                if(con != null) {
                    con.close();
                }
                res.status(500);
                return "There was a general error. Please contact support";
            } finally {
                if(con != null) {
                    con.close();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        res.redirect("/my/orders");
        return "";
    }
}
