package com.globaldocs.factory;

import com.globaldocs.document.ContractProcessor;
import com.globaldocs.document.DocumentProcessor;

/**
 * ConcreteCreator: factory for legal contract processors.
 */
public final class ContractProcessorFactory extends DocumentProcessorFactory {

    @Override
    public DocumentProcessor createProcessor() {
        return new ContractProcessor();
    }
}