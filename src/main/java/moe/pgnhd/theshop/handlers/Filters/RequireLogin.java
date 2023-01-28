package moe.pgnhd.theshop.handlers.Filters;

import spark.Request;
import spark.Response;

import java.util.HashSet;
import java.util.Set;

public class RequireLogin {

    private static Set<String> whitelist = new HashSet<>();
    static {
        whitelist.add("/");
        whitelist.add("/login");
        whitelist.add("/register");
        whitelist.add("/hello");
        whitelist.add("/hello2");
        whitelist.add("/register_confirm");
    }
    public static void filterRequireLogin(Request req, Response res) {
        if(req.session() == null) {
            req.session(true);
        }

        Object _user = req.session().attribute("user");
        if(_user == null && !whitelist.contains(req.pathInfo())) {
            req.session().attribute("login_redirect_back", req.pathInfo());
            res.redirect("/login");
        }

    }
}
