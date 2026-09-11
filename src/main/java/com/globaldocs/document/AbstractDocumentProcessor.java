package com.globaldocs.document;

/**
 * Abstract base class implementing Template Method pattern.
 * Provides shared format validation and the process() template.
 * Subclasses must implement validateCountryRegulation() and doProcess().
 */
public abstract class AbstractDocumentProcessor implements DocumentProcessor {

    @Override
    public void validateFormat(Document document) throws DocumentProcessingException {
        if (!DocumentFormat.isSupported(document.getFormat())) {
            throw new DocumentProcessingException(
                String.format("Unsupported format '%s' for file %s", document.getFormat(), document.getFileName()));
        }
    }

    @Override
    public abstract void validateCountryRegulation(Document document) throws DocumentProcessingException;

    /**
     * Template method: validates format, validates regulation, then processes.
     */
    @Override
    public final void process(Document document) throws DocumentProcessingException {
        validateFormat(document);
        validateCountryRegulation(document);
        doProcess(document);
    }

    /**
     * Hook method: performs the actual processing logic.
     * @param document the document to process
     * @throws DocumentProcessingException if processing fails
     */
    protected abstract void doProcess(Document document) throws DocumentProcessingException;
}