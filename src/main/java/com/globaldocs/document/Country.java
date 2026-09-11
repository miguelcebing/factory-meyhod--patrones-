package com.globaldocs.document;

/**
 * Enum representing the LATAM countries where GlobalDocs operates.
 * Each country has unique regulatory requirements for document processing.
 */
public enum Country {
    COLOMBIA("Colombia", "DIAN", "CUFE"),
    MEXICO("Mexico", "SAT", "CFDI"),
    ARGENTINA("Argentina", "AFIP", "CAE"),
    CHILE("Chile", "SII", "TED");

    private final String displayName;
    private final String taxAuthority;
    private final String requiredStamp;

    Country(String displayName, String taxAuthority, String requiredStamp) {
        this.displayName = displayName;
        this.taxAuthority = taxAuthority;
        this.requiredStamp = requiredStamp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTaxAuthority() {
        return taxAuthority;
    }

    public String getRequiredStamp() {
        return requiredStamp;
    }

    @Override
    public String toString() {
        return displayName;
    }
}