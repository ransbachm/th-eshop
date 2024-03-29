package moe.pgnhd.theshop;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import spark.Request;
import spark.Response;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class Util {
    public static String randomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwzyz0123456789";
        int min_ascii = 0;
        int max_ascii = alphabet.length()-1;
        StringBuilder sb = new StringBuilder(length);
        for(int i=0; i<length; i++) {
            char ch = alphabet.charAt(secureRandom.nextInt(min_ascii, max_ascii+1));
            sb.append(ch);
        }
        return sb.toString();
    }

    public static String getExtension(InputStream is) {
        try {
            TikaConfig tikaConfig = new TikaConfig();
            Detector detector = tikaConfig.getDetector();

            TikaInputStream stream = TikaInputStream.get(is);

            Metadata metadata = new Metadata();
            MediaType mediaType = detector.detect(stream, metadata);

            MimeType mimeType = tikaConfig.getMimeRepository().forName(mediaType.toString());
            return mimeType.getExtension();
        } catch (IOException | TikaException e) {
            return null;
        }
    }

    public static boolean isValidImage(String extension) {
        return contains(extension, ".png", ".jpg");

    }

    private static boolean contains(String found_ext, String ... allowed_extensions) {
        for(String allowed : allowed_extensions) {
            if(allowed.equals(found_ext)) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, Object> getModel(Request req) {
        Map<String, Object> model = new HashMap<>();
        model.put("user", req.attribute("user"));
        model.put("seller", req.attribute("seller"));
        model.put("session", req.attribute("t_session"));
        String design = req.cookie("t_design");
        model.put("t_design", design != null ? design : "primary");
        return model;
    }

    public static String handle404(Request req, Response res){
        res.status(404);
        return "404 not found";
    }

    public static <T> List<List<T>> to_list_of_lists(List<T> recommendations, int list_size) {
        List<List<T>> outer_recommendations = new ArrayList<>();
        List<T> crr = new ArrayList<>();
        for(int i = 0; i< recommendations.size(); i++) {
            if(i % list_size == 0 && i > 0) {
                outer_recommendations.add(crr);
                crr = new ArrayList<>();
            }
            crr.add(recommendations.get(i));
        }
        outer_recommendations.add(crr);
        return outer_recommendations;
    }

    public static String md5_hex(String in) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("md5");md.update(in.getBytes());
            for(byte b : md.digest()) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

