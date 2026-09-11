package com.globaldocs.document;

import java.util.Map;

/**
 * ConcreteProduct: processes tax declarations.
 * Each country requires a specific tax ID field (RUT, RFC, CUIT, RUT_CHILE).
 */
public final class TaxDeclarationProcessor extends AbstractDocumentProcessor {

    private static final Map<Country, String> REQUIRED_FIELDS = Map.of(
        Country.COLOMBIA,  "RUT",
        Country.MEXICO,    "RFC",
        Country.ARGENTINA, "CUIT",
        Country.CHILE,     "RUT_CHILE"
    );

    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        String requiredField = REQUIRED_FIELDS.get(document.getCountry());
        if (requiredField != null && !document.getContent().contains(requiredField)) {
            throw new DocumentProcessingException(
                String.format("Tax declaration missing required field: %s", requiredField));
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.printf("  [OK] Processing tax declaration: %s%n", document.getFileName());
    }
}