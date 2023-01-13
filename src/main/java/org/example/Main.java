package org.example;

import static spark.Spark.*;
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        port(8080);
        get("/", (req, res) -> "Hello World!");
    }
}