package com.globaldocs.document;

/**
 * ConcreteProduct: processes electronic invoices.
 * Validates country-specific tax stamps: CUFE (CO), CFDI (MX), CAE (AR), TED (CL).
 */
public final class InvoiceProcessor extends AbstractDocumentProcessor {

    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        Country country = document.getCountry();
        String stamp = country.getRequiredStamp();

        if (!document.getContent().contains(stamp)) {
            throw new DocumentProcessingException(
                String.format("%s: missing %s stamp (%s requirement)",
                        country.getDisplayName(), stamp, country.getTaxAuthority()));
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.printf("  [OK] Processing electronic invoice: %s [%s]%n",
                document.getFileName(), document.getCountry());
    }
}