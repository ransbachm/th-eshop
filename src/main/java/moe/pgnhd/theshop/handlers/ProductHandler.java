package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.Util;
import moe.pgnhd.theshop.model.Product;
import moe.pgnhd.theshop.model.Seller;
import moe.pgnhd.theshop.model.Session;
import moe.pgnhd.theshop.model.User;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductHandler {
    private static Path uploadDir = Paths.get("public/upload/product");

    public static String handleShowProduct(Request req, Response res) {
        Product product = Main.management.getProduct(req.params(":id"));
        Map<String, Object> model = new HashMap<>();
        model.put("product", product);

        return Main.render("product/show", model);
    }

    public static String handleCreateProduct(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        return Main.render("product/create", model);
    }

    public static String handleCreateProductSubmit(Request req, Response res){
        Map<String, Object> model = new HashMap<>();
        boolean doNotCreateProduct = false;
        // Required for file upload
        req.attribute("org.eclipse.jetty.multipartConfig",
                new MultipartConfigElement(uploadDir.resolve("tmp.tmp").toString()));
        uploadDir.toFile().mkdirs();

        User user = req.attribute("user");
        Seller seller = Main.management.getSellerFromUser(user);
        if(seller == null) {
            model.put("notSeller", true);
            doNotCreateProduct = true;
        }
        int sellerID = seller.getId();
        int productID;

        try {
            double price = Double.parseDouble(req.queryParams("price"));
            if(price < 0){
                model.put("pricetolow", true);
                doNotCreateProduct = true;
            }
            String name = req.queryParams("name").trim();
            if(name.isBlank()){
                model.put("noname", true);
                doNotCreateProduct = true;
            }

            String description = req.queryParams("description").trim();
            if(description.isBlank()){
                model.put("nodescription", true);
                doNotCreateProduct = true;
            }

            int available = Integer.parseInt(req.queryParams("available"));
            if(available < 0){
                model.put("availabletolow", true);
                doNotCreateProduct = true;
            }

            String tmp_code = saveTempImage(req.raw().getPart("image"));

            if(doNotCreateProduct) {return Main.render("product/create", model);}

            productID = Main.management.registerProduct(name, price, available, description, sellerID);
            renameTempImage(tmp_code, productID);

        } catch (IllegalArgumentException e){
            if(e.getMessage().equals("Not supported file type")) {
                model.put("invalidImageFormat", true);
                return Main.render("product/create", model);
            }
            model.put("invalidInput", true);
            return Main.render("product/create", model);
        } catch (SQLException | ServletException | IOException e) {
            res.status(500);
            return "Something went wrong";
        }

        res.redirect("/product/" + productID);
        return "";
    }

    private static void renameTempImage(String tmp_filename, int productID) {
        File tmp = uploadDir.resolve(tmp_filename).toFile();

        String[] split = tmp.toString().split("\\.");
        String extension = split[split.length-1];
        File new_file = uploadDir.resolve(productID + "." + extension).toFile();

        tmp.renameTo(new_file);
    }

    private static String saveTempImage(Part part) throws IOException, IllegalArgumentException {
        try(InputStream is = part.getInputStream()) {
            String extension = Util.getExtension(is);
            if(Util.isValidImage(extension)) {
                String random_name = Util.randomString(32);
                String filename = "tmp_" + random_name + ".png";
                FileOutputStream fos = new FileOutputStream(
                        uploadDir.resolve(filename).toFile());

                BufferedImage img = ImageIO.read(is);
                ImageIO.write(img, "png", fos);
                fos.close();
                return filename;
            } else {
                throw new IllegalArgumentException("Not supported file type");
            }
        }
    }

    public static Object handleMyProducts(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        Session session = req.attribute("t_session");
        List<Product> products = Main.management.getProductsOfSeller(session.getUser().getId());
        model.put("products", products);

        return Main.render("products", model);
    }

    public static Object handleAlterMyProduct(Request req, Response res) {

        int new_amount = Integer.parseInt(req.queryParams("new_amount"));
        int productId = Integer.parseInt(req.queryParams("productid"));
        if(new_amount > 0) {
            Main.management.setProductAvailablity(productId, new_amount);
        }

        res.redirect("/my/products");
        return "";
    }
}
