package com.globaldocs.document;

/**
 * Custom exception for document processing errors.
 * Thrown when a document fails validation or processing.
 */
public class DocumentProcessingException extends Exception {

    public DocumentProcessingException(String message) {
        super(message);
    }

    public DocumentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}