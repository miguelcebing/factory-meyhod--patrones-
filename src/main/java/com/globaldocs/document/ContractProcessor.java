package com.globaldocs.document;

/**
 * ConcreteProduct: processes legal contracts.
 * Validates that the contract contains a digital SIGNATURE.
 */
public final class ContractProcessor extends AbstractDocumentProcessor {

    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        if (!document.getContent().contains("SIGNATURE")) {
            throw new DocumentProcessingException(
                String.format("Legal contract missing digital signature for %s", document.getCountry()));
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.printf("  [OK] Processing legal contract: %s%n", document.getFileName());
    }
}