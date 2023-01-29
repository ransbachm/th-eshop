package moe.pgnhd.theshop.handlers.Filters;

import spark.Request;
import spark.Response;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RequireLogin {

    private static class Entry {
        public boolean exact;
        public String path;

        public Entry(String[] split) {
            this.exact = split[0].equals("exact") ? true : false;
            this.path = split[1];
        }
    }

    private static List<Entry> whitelist = new ArrayList<>();
    static {

        InputStream is = RequireLogin.class.getResourceAsStream("/whitelist.list");
        Scanner scan = new Scanner(is);
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            if(line.startsWith("#") || line.isBlank()) {
                // ignore
            } else {
                whitelist.add(new Entry(line.split(" ")));
            }
        }

    }

    private static boolean onWhitelist(String path) {
        for(Entry e : whitelist) {
            if(e.exact) {
                if(path.equals(e.path) || path.equals(e.path+"/")) {
                    return true;
                }
            } else {
                if(path.startsWith(e.path)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void filterRequireLogin(Request req, Response res) {
        if(req.session() == null) {
            req.session(true);
        }

        Object _user = req.session().attribute("user");
        if(_user == null && !onWhitelist(req.pathInfo())) {
            req.session().attribute("login_redirect_back", req.pathInfo());
            res.redirect("/login");
        }

    }
}
