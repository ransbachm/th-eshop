package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.Util;
import moe.pgnhd.theshop.model.Product;
import moe.pgnhd.theshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

public class IndexHandler {
    private static Logger LOG = LoggerFactory.getLogger(IndexHandler.class);

    public static String show(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);
        User user = req.attribute("user");

        // Add recommendations only if possible
        if(user != null) {
            List<Product> reco_products = Main.management.recommend_for_user(user.getId(), 5);
            if(reco_products != null) {
                model.put("recommendations_outer", Util.to_list_of_lists(reco_products, 5));
            }
        }

        return Main.render("index", model);
    }
}
