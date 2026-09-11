package com.globaldocs.document;

/**
 * ConcreteProduct: processes digital certificates.
 * Validates that the certificate contains a certifying authority.
 */
public final class DigitalCertificateProcessor extends AbstractDocumentProcessor {

    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        if (!document.getContent().contains("CERT_AUTHORITY")) {
            throw new DocumentProcessingException(
                String.format("Digital certificate missing certifying authority for %s", document.getCountry()));
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.printf("  [OK] Processing digital certificate: %s%n", document.getFileName());
    }
}