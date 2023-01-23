package org.example.handlers;

import org.example.Main;
import org.example.Verwaltung;
import org.example.model.Bestellung;
import org.example.model.Produkt;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class OrderHandler {
    public static String handleGetOrders(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        model.put("bestellungen", Main.verwaltung.getOrders());

        return Main.render("orders", model);
    }
}
