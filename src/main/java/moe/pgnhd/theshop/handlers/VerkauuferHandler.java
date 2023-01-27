package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.model.Produkt;
import moe.pgnhd.theshop.model.Verkauufer;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerkauuferHandler {
    public static String handleVerkauufer(Request req, Response res) {
        Verkauufer verkauufer = Main.verwaltung.getVerkauufer(req.params(":id"));
        List<Produkt> produkte = Main.verwaltung.getProductsOfSeller(req.params(":id"));
        Map<String, Object> model = new HashMap<>();
        model.put("verkauufer", verkauufer);
        model.put("produkte", produkte);

        return Main.render("verkauufer", model);
    }
}
