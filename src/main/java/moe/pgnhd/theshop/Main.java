package moe.pgnhd.theshop;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import io.github.cdimascio.dotenv.Dotenv;
import moe.pgnhd.theshop.handlers.BasketHandler;
import moe.pgnhd.theshop.handlers.Filters.RequireLogin;
import moe.pgnhd.theshop.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    private static Logger LOG = LoggerFactory.getLogger(Main.class);
    public static Management management;
    public static Dotenv dotenv = Dotenv.load();
    static boolean isDev = Boolean.parseBoolean(dotenv.get("DEV_MODE"));

    private static Handlebars hbs;

    public static String render(String templatePath, Map<String, Object> model) {
        try{
            String design_path = "templates/";
            String design = (String) model.get("t_design");
            switch (design) {
                case "secondary":
                    design_path += "secondary/"; break;
                case "primary": default:
                    design_path += "primary/"; break;
            }
            Template template = hbs.compile(design_path + templatePath);
            return template.apply(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws SQLException {
        LOG.info("Application is starting");

        exception(Exception.class, (e,req,res) -> {
            LOG.error(req.pathInfo() + " " + e.getMessage());
            res.status(500);
            res.body("<h2>500 Internal Server Error</h2>");
        });

        try {
            management = new Management();
        } catch (SQLException e) {
            LOG.error("DB Connection failed - shutting down");
            LOG.error(e.getMessage());
            return;
        }

        // Because resources are copied to target directory
        // Live refresh cannot work with the class path loader
        if (!isDev) {
            hbs = new Handlebars();
        } else {
            hbs = new Handlebars(new FileTemplateLoader(
                    "src/main/resources/"));
        }
        staticFiles.externalLocation("public");

        port(4567);
        // require logged-in user for paths below
        before("*", RequireLogin::filterRequireLogin);

        get("/", IndexHandler::show);


        get("login", RegisterAndLoginHandler::handleLogin);
        post("login", RegisterAndLoginHandler::handleLoginSubmit);
        get("register", RegisterAndLoginHandler::handleRegister);
        post("register", RegisterAndLoginHandler::handleRegisterSubmit);
        get("register_confirm", RegisterAndLoginHandler::handleRegisterConfirm);
        post("register_confirm", RegisterAndLoginHandler::handleRegisterConfirmSubmit);
        post("logout", RegisterAndLoginHandler::logout);

        get("my/orders", OrderHandler::handleGetOrders);
        get("my/basket", BasketHandler::show);
        post("my/basket/change", BasketHandler::change);
        post("my/basket", BasketHandler::order);


        get("product/create", ProductHandler::handleCreateProduct);
        post("/product/create", ProductHandler::handleCreateProductSubmit);
        post("product/addToCart", ProductHandler::addToBasket);
        get("product/:id", ProductHandler::handleShowProduct);

        get("seller/:id", SellerHandler::handleSeller);

        get("profile", ProfileHandler::handleShowProfile);
        post("profile",ProfileHandler::handleMakeUserSeller);

        get("my/products", ProductHandler::handleMyProducts);
        post("my/products", ProductHandler::handleAlterMyProduct);

        get("search", SearchHandler::handleSearch);

        notFound(Util::handle404);
    }

}