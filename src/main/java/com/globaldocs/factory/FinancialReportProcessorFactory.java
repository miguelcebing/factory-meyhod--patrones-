package com.globaldocs.factory;

import com.globaldocs.document.DocumentProcessor;
import com.globaldocs.document.FinancialReportProcessor;

/**
 * ConcreteCreator: factory for financial report processors.
 */
public final class FinancialReportProcessorFactory extends DocumentProcessorFactory {

    @Override
    public DocumentProcessor createProcessor() {
        return new FinancialReportProcessor();
    }
}