package com.globaldocs.factory;

import com.globaldocs.document.DocumentProcessor;
import com.globaldocs.document.TaxDeclarationProcessor;

/**
 * ConcreteCreator: factory for tax declaration processors.
 */
public final class TaxDeclarationProcessorFactory extends DocumentProcessorFactory {

    @Override
    public DocumentProcessor createProcessor() {
        return new TaxDeclarationProcessor();
    }
}