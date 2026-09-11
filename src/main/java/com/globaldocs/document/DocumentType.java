package com.globaldocs.document;

/**
 * Enum representing all supported document types.
 * Each type has a specific processor and country-specific validation rules.
 */
public enum DocumentType {
    ELECTRONIC_INVOICE("Electronic Invoice", "Validates tax stamps per country (CUFE, CFDI, CAE, TED)"),
    LEGAL_CONTRACT("Legal Contract", "Requires digital signature"),
    FINANCIAL_REPORT("Financial Report", "Argentina requires xlsx format"),
    DIGITAL_CERTIFICATE("Digital Certificate", "Requires certifying authority"),
    TAX_DECLARATION("Tax Declaration", "Requires country-specific tax ID (RUT, RFC, CUIT, RUT_CHILE)");

    private final String displayName;
    private final String description;

    DocumentType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}