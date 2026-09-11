package com.globaldocs.document;

/**
 * ConcreteProduct: processes financial reports.
 * Argentina requires reports in xlsx format.
 */
public final class FinancialReportProcessor extends AbstractDocumentProcessor {

    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        if (document.getCountry() == Country.ARGENTINA && !"xlsx".equals(document.getFormat())) {
            throw new DocumentProcessingException(
                "Argentina: financial reports must be xlsx format");
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.printf("  [OK] Processing financial report: %s%n", document.getFileName());
    }
}