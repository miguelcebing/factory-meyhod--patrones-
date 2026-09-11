package com.globaldocs.document;

import java.util.Objects;

/**
 * Domain model representing a document to be processed.
 * Immutable value object with all required metadata.
 */
public final class Document {

    private final String fileName;
    private final String format;
    private final DocumentType type;
    private final Country country;
    private final String content;

    public Document(String fileName, String format, DocumentType type, Country country, String content) {
        this.fileName = Objects.requireNonNull(fileName, "fileName must not be null");
        this.format = Objects.requireNonNull(format, "format must not be null").toLowerCase();
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.country = Objects.requireNonNull(country, "country must not be null");
        this.content = Objects.requireNonNull(content, "content must not be null");
    }

    public String getFileName() { return fileName; }
    public String getFormat()   { return format; }
    public DocumentType getType()     { return type; }
    public Country getCountry()  { return country; }
    public String getContent()  { return content; }

    @Override
    public String toString() {
        return String.format("Document{file='%s', format='%s', type=%s, country=%s}",
                fileName, format, type, country);
    }
}