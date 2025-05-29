package ru.defix.blog.common.util;

import java.util.Base64;

public class CodingUtility {
    public static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public static String decode(String encodedText) {
        return new String(Base64.getDecoder().decode(encodedText));
    }
}
