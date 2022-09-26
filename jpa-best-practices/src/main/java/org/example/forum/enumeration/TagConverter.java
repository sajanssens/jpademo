package org.example.forum.enumeration;

import jakarta.persistence.AttributeConverter;

import static org.example.forum.enumeration.Tag.CLOUD;
import static org.example.forum.enumeration.Tag.DATA;
import static org.example.forum.enumeration.Tag.JAVA;
import static org.example.forum.enumeration.Tag.NEWS;

public class TagConverter implements AttributeConverter<Tag, Integer> {

    public static final int NEWS_ID = 10;
    public static final int JAVA_ID = 20;
    public static final int DATA_ID = 30;
    public static final int CLOUD_ID = 40;

    @Override
    public Integer convertToDatabaseColumn(Tag value) {
        return switch (value) {
            case NEWS -> NEWS_ID;
            case JAVA -> JAVA_ID;
            case DATA -> DATA_ID;
            case CLOUD -> CLOUD_ID;
        };
    }

    @Override
    public Tag convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;

        Tag result = null;

        switch (dbData) {
            case NEWS_ID:
                result = NEWS; break;
            case JAVA_ID:
                result = JAVA; break;
            case DATA_ID:
                result = DATA; break;
            case CLOUD_ID:
                result = CLOUD; break;
        }
        return result;
    }
}
