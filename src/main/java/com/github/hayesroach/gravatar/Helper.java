package com.github.hayesroach.gravatar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Helper {
    static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    static String md5Hex(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
        }
        return null;
    }

    static Rating getRating(String rating) {
        switch (rating) {
            case "0":
                return Rating.G;
            case "1":
                return Rating.PG;
            case "2":
                return Rating.R;
            default:
                return Rating.X;
        }
    }

    static Rating getRating(int rating) {
        switch (rating) {
            case 0:
                return Rating.G;
            case 1:
                return Rating.PG;
            case 2:
                return Rating.R;
            default:
                return Rating.X;
        }
    }

    static String encodeFileToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + file, e);
        }
    }

    static String encodeInputStreamToBase64(InputStream stream) throws IOException {
        final var bytes = stream.readAllBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
