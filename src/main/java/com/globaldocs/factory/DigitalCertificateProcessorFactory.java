package com.globaldocs.factory;

import com.globaldocs.document.DigitalCertificateProcessor;
import com.globaldocs.document.DocumentProcessor;

/**
 * ConcreteCreator: factory for digital certificate processors.
 */
public final class DigitalCertificateProcessorFactory extends DocumentProcessorFactory {

    @Override
    public DocumentProcessor createProcessor() {
        return new DigitalCertificateProcessor();
    }
}