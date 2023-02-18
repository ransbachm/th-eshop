package moe.pgnhd.theshop.handlers;

import moe.pgnhd.theshop.Mail;
import moe.pgnhd.theshop.Main;
import moe.pgnhd.theshop.Management;
import moe.pgnhd.theshop.Util;
import moe.pgnhd.theshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Base64;
import java.util.Map;

public class RegisterAndLoginHandler {
    private static Logger LOG = LoggerFactory.getLogger(Management.class);


    private static String calculatePwdHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(hash));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String handleRegister(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);
        return Main.render("register", model);
    }

    public static String handleRegisterSubmit(Request req, Response res) {
        String firstname = req.queryParams("firstname");
        String lastname = req.queryParams("lastname");
        String email = req.queryParams("email");
        String password = req.queryParams("password");
        String housenumber = req.queryParams("housenumber");
        String street = req.queryParams("street");
        String zipcode = req.queryParams("zipcode");

        String activation_code = Util.randomString(32);

        Map<String, Object> model = Util.getModel(req);
        String salt = Util.randomString(20);

        String base64_pwd_hash = calculatePwdHash(password+salt);


        User maybe_user = Main.management.findUserByEmail(email);
        if(maybe_user != null) {
            model.put("email_not_available", true);
            return Main.render("register", model);
        }

        try{
            Mail.sendRegisterConfirmMail(email, activation_code);
        } catch (MessagingException e) {
            LOG.info(e.getMessage());
            model.put("email_send_failure", true);
            return Main.render("register", model);
        }

        // Will not be put into db if sending email already fails
        try {
            Main.management.createUser(firstname, lastname, email, base64_pwd_hash, salt,
                    housenumber, street, zipcode, activation_code);
        } catch (SQLIntegrityConstraintViolationException e) {
            model.put("email_not_available", true);
            return Main.render("register", model);
        }

        req.session().attribute("try_register_email", email);
        res.redirect("/register_confirm");
        return "";
    }

    public static String handleRegisterConfirm(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);
        return Main.render("register_confirm", model);
    }

    private static String failHandleRegisterConfirmSubmit(Map<String, Object> model) {
        model.put("register_confirm_failure", true);
        return Main.render("register_confirm", model);
    }
    public static String handleRegisterConfirmSubmit(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);
        String activationcode = req.queryParams("activationcode");

        User try_user = Main.management.findUserByActivationCode(activationcode);
        if(try_user == null) {
            return failHandleRegisterConfirmSubmit(model);
        } else if(try_user.getActivationcode().equals(activationcode)) {
            Main.management.setUserActivated(try_user.getId(), true);
            model.put("register_confirm_success", true);
            model.put("login_redirect_back", req.session().attribute("login_redirect_back"));
            return Main.render("register_confirm", model);
        }
        return failHandleRegisterConfirmSubmit(model);
    }

    public static String handleLogin(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);
        return Main.render("login", model);
    }


    private static String wrong_pwd(Request req, Response res, Map<String, Object> model) {
        model.put("user_wrong_pwd", true);
        return Main.render("login", model);
    }
    public static String handleLoginSubmit(Request req, Response res) {
        Map<String, Object> model = Util.getModel(req);

        User user = Main.management.findUserByEmail(req.queryParams("email"));
        String pwdHash;

        if(user == null) {
            return wrong_pwd(req, res, model);
        }

        pwdHash = calculatePwdHash(req.queryParams("password") + user.getSalt());

        if(!user.isActive()) {
            model.put("user_not_confirmed", true);
            return Main.render("login", model);
        } else if(user.getPwdhash().equals(pwdHash)) {
            Main.management.logInSession(req.cookie("t_session_id"), user);
            String login_redirect_back = req.cookie("login_redirect_back");
            if(login_redirect_back == null) {
                login_redirect_back = "/";
            }
            res.redirect(login_redirect_back);
            return "";
        } else {
            return wrong_pwd(req, res, model);
        }
    }
}
