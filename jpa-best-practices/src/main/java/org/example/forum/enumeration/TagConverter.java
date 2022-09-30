package org.example.forum.enumeration;

import jakarta.persistence.AttributeConverter;

import static java.util.Arrays.stream;

public class TagConverter implements AttributeConverter<Tag, String> {

    @Override
    public String convertToDatabaseColumn(Tag value) {
        return value == null ? null : value.getDescription();
    }

    @Override
    public Tag convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        return stream(Tag.values())
                .filter(t -> dbData.equals(t.getDescription()))
                .findAny()
                .orElse(null);
    }
}
