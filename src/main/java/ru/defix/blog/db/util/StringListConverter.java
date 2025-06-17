package ru.defix.blog.db.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        if(strings == null || strings.isEmpty()) return "{}";
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        strings.forEach(element -> builder.append(String.format("\"%s\",", element)));
        return builder.deleteCharAt(builder.length()-1).append("}").toString();
    }

    // {"grege", "grger"}, {}
    @Override
    public List<String> convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty() || s.equals("{}")) return new ArrayList<>();
        s = s.trim();
        return Stream.of(s.substring(1, s.length() - 1).split(",\\s*"))
                .map(element -> element.substring(1, element.length() - 1))
                .toList();
    }
}
