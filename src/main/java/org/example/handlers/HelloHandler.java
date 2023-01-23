package org.example.handlers;

import org.example.Main;
import org.example.Verwaltung;
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
