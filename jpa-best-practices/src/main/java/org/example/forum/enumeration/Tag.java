package org.example.forum.enumeration;

public enum Tag {
    NEWS("Nieuws"), JAVA("Java programmeren"), DATA("Data en AI"), CLOUD("Cloud computing");

    private String description;

    Tag(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
