package moe.pgnhd.theshop;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

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
}
