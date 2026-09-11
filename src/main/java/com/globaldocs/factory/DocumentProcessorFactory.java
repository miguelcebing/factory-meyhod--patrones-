package com.globaldocs.factory;

import com.globaldocs.document.Document;
import com.globaldocs.document.DocumentProcessingException;
import com.globaldocs.document.DocumentProcessor;

/**
 * Creator abstract class for the Factory Method pattern.
 * Defines the factory method that subclasses must implement.
 */
public abstract class DocumentProcessorFactory {

    /**
     * Factory Method: creates a specific DocumentProcessor.
     * @return a concrete DocumentProcessor instance
     */
    public abstract DocumentProcessor createProcessor();

    /**
     * Template method that uses the factory method to process a document.
     * @param document the document to process
     * @throws DocumentProcessingException if processing fails
     */
    public void processDocument(Document document) throws DocumentProcessingException {
        DocumentProcessor processor = createProcessor();
        processor.process(document);
    }
}