package org.example.post.enumeration;

import javax.persistence.AttributeConverter;

import static org.example.post.enumeration.Tag.*;

public class TagConverter implements AttributeConverter<Tag, Integer> {

    public static final int NEWS_ID = 10;
    public static final int JAVA_ID = 20;
    public static final int DATA_ID = 30;
    public static final int CLOUD_ID = 40;

    @Override
    public Integer convertToDatabaseColumn(Tag value) {
        if (value == null) return null;

        int result = 0;

        switch (value) {
            case NEWS:
                result = NEWS_ID; break;
            case JAVA:
                result = JAVA_ID; break;
            case DATA:
                result = DATA_ID; break;
            case CLOUD:
                result = CLOUD_ID; break;
        }
        return result;
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
