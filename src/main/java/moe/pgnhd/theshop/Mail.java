package moe.pgnhd.theshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Mail {
    private static Logger LOG = LoggerFactory.getLogger(Management.class);
    public static void block(int seconds) {
        try{Thread.sleep(seconds*1000);} catch (InterruptedException e) {LOG.error(e.getMessage());}
    }

    private static void sendMail(String to, String subject, String content) throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true);
        prop.put("mail.smtp.host", Main.dotenv.get("SMTP_HOST"));
        prop.put("mail.smtp.port", Main.dotenv.get("SMTP_PORT"));
        PasswordAuthentication auth = new PasswordAuthentication(
                Main.dotenv.get("SMTP_USER"),
                Main.dotenv.get("SMTP_PASSWORD"));
        Authenticator auth2 = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return auth;
            }
        };
        Session session = Session.getInstance(prop, auth2);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(Main.dotenv.get("SMTP_FROM"), "Th_Eshop"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(content, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    public static void sendRegisterConfirmMail(Request req, String to, String activation_code) throws MessagingException {
        Map<String, Object> model = Util.getModel(req);
        model.put("activation_code", activation_code);
        String content = Main.render("email/register_confirm", model);

        String subject = "Confirm your Th_Eshop account";
        sendMail(to, subject, content);
    }
}
