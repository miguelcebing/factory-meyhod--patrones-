package com.globaldocs.document;

/**
 * Product interface for the Factory Method pattern.
 * Defines the contract that all document processors must implement.
 */
public interface DocumentProcessor {

    /**
     * Validate that the document format is supported.
     * @param document the document to validate
     * @throws DocumentProcessingException if format is not supported
     */
    void validateFormat(Document document) throws DocumentProcessingException;

    /**
     * Validate country-specific regulatory requirements.
     * @param document the document to validate
     * @throws DocumentProcessingException if regulation validation fails
     */
    void validateCountryRegulation(Document document) throws DocumentProcessingException;

    /**
     * Process the document through the full pipeline.
     * @param document the document to process
     * @throws DocumentProcessingException if any validation or processing fails
     */
    void process(Document document) throws DocumentProcessingException;
}