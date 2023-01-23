package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import spark.*;

import java.util.*;

public class HelloHandler {
    public static String handleHelloRequest(Request req, Response res) {
        return "Hello World 1";
    }

    public static String handleAnyUserFirstName(Request req, Response res) {
        return Objects.requireNonNull(Main.verwaltung.getAnyUserFirstName());
    }
}
