package com.globaldocs.factory;

import com.globaldocs.document.DocumentType;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Single access point for obtaining the correct factory.
 * Maps DocumentType to its corresponding ConcreteCreator.
 */
public final class DocumentProcessorFactoryProvider {

    private static final Map<DocumentType, Supplier<DocumentProcessorFactory>> FACTORY_MAP = Map.of(
        DocumentType.ELECTRONIC_INVOICE,  InvoiceProcessorFactory::new,
        DocumentType.LEGAL_CONTRACT,      ContractProcessorFactory::new,
        DocumentType.FINANCIAL_REPORT,    FinancialReportProcessorFactory::new,
        DocumentType.DIGITAL_CERTIFICATE, DigitalCertificateProcessorFactory::new,
        DocumentType.TAX_DECLARATION,     TaxDeclarationProcessorFactory::new
    );

    private DocumentProcessorFactoryProvider() {
        // Utility class: no instantiation
    }

    /**
     * Get the appropriate factory for the given document type.
     * @param type the document type
     * @return the corresponding factory
     * @throws IllegalArgumentException if type is unknown
     */
    public static DocumentProcessorFactory getFactory(DocumentType type) {
        Supplier<DocumentProcessorFactory> supplier = FACTORY_MAP.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown document type: " + type);
        }
        return supplier.get();
    }
}