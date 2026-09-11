package com.globaldocs.batch;

import com.globaldocs.document.Document;
import com.globaldocs.document.DocumentProcessingException;
import com.globaldocs.factory.DocumentProcessorFactory;
import com.globaldocs.factory.DocumentProcessorFactoryProvider;

import java.util.List;

/**
 * Processes a batch of documents with error isolation.
 * One failed document does not stop the rest of the batch.
 */
public final class BatchDocumentProcessor {

    /**
     * Process all documents in the batch.
     * @param documents list of documents to process
     * @return BatchResult with successes and errors
     */
    public BatchResult processBatch(List<Document> documents) {
        BatchResult result = new BatchResult();

        for (Document document : documents) {
            try {
                DocumentProcessorFactory factory =
                        DocumentProcessorFactoryProvider.getFactory(document.getType());
                factory.processDocument(document);
                result.registerSuccess();
            } catch (DocumentProcessingException e) {
                result.registerError(document.getFileName(), e.getMessage());
            } catch (Exception e) {
                result.registerError(document.getFileName(), "Unexpected error: " + e.getMessage());
            }
        }

        return result;
    }
}