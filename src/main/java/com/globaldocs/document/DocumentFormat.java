package com.globaldocs.document;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class defining all supported document formats.
 * Immutable list of formats accepted by the processing system.
 */
public final class DocumentFormat {

    public static final List<String> SUPPORTED =
            Collections.unmodifiableList(Arrays.asList("pdf", "doc", "docx", "md", "csv", "txt", "xlsx"));

    private DocumentFormat() {
        // Utility class: no instantiation
    }

    /**
     * Check if the given format is supported.
     * @param format the format to validate (case-insensitive)
     * @return true if supported
     */
    public static boolean isSupported(String format) {
        if (format == null) return false;
        return SUPPORTED.contains(format.toLowerCase());
    }

    /**
     * Get the lowercase version of the format string.
     * @param format the format to normalize
     * @return lowercase format or null if input is null
     */
    public static String normalize(String format) {
        return format != null ? format.toLowerCase() : null;
    }
}