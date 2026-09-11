package com.globaldocs.factory;

import com.globaldocs.document.DocumentProcessor;
import com.globaldocs.document.InvoiceProcessor;

/**
 * ConcreteCreator: factory for electronic invoice processors.
 */
public final class InvoiceProcessorFactory extends DocumentProcessorFactory {

    @Override
    public DocumentProcessor createProcessor() {
        return new InvoiceProcessor();
    }
}