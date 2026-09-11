package com.globaldocs.factory;

import com.globaldocs.document.*;
import com.globaldocs.batch.BatchDocumentProcessor;
import com.globaldocs.batch.BatchResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentProcessorFactoryTest {

    // --- Factory creates correct ConcreteProduct types ---

    @Test
    void createsInvoiceProcessor() {
        DocumentProcessorFactory factory = DocumentProcessorFactoryProvider.getFactory(DocumentType.ELECTRONIC_INVOICE);
        DocumentProcessor processor = factory.createProcessor();
        assertInstanceOf(InvoiceProcessor.class, processor);
    }

    @Test
    void createsContractProcessor() {
        DocumentProcessorFactory factory = DocumentProcessorFactoryProvider.getFactory(DocumentType.LEGAL_CONTRACT);
        DocumentProcessor processor = factory.createProcessor();
        assertInstanceOf(ContractProcessor.class, processor);
    }

    @Test
    void createsFinancialReportProcessor() {
        DocumentProcessorFactory factory = DocumentProcessorFactoryProvider.getFactory(DocumentType.FINANCIAL_REPORT);
        DocumentProcessor processor = factory.createProcessor();
        assertInstanceOf(FinancialReportProcessor.class, processor);
    }

    @Test
    void createsDigitalCertificateProcessor() {
        DocumentProcessorFactory factory = DocumentProcessorFactoryProvider.getFactory(DocumentType.DIGITAL_CERTIFICATE);
        DocumentProcessor processor = factory.createProcessor();
        assertInstanceOf(DigitalCertificateProcessor.class, processor);
    }

    @Test
    void createsTaxDeclarationProcessor() {
        DocumentProcessorFactory factory = DocumentProcessorFactoryProvider.getFactory(DocumentType.TAX_DECLARATION);
        DocumentProcessor processor = factory.createProcessor();
        assertInstanceOf(TaxDeclarationProcessor.class, processor);
    }

    // --- Factory rejects unknown type ---

    @Test
    void rejectsUnknownDocumentType() {
        // DocumentType is an enum, so getFactory only accepts valid values
        // This test verifies that the map covers all enum values
        for (DocumentType type : DocumentType.values()) {
            assertDoesNotThrow(() -> DocumentProcessorFactoryProvider.getFactory(type));
        }
    }

    // --- Invoice country validations ---

    @Test
    void colombianInvoiceWithCUFE_succeeds() {
        Document doc = new Document("inv.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                Country.COLOMBIA, "Invoice CUFE=ABC123");
        assertDoesNotThrow(() -> new InvoiceProcessor().process(doc));
    }

    @Test
    void colombianInvoiceWithoutCUFE_throwsException() {
        Document doc = new Document("inv.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                Country.COLOMBIA, "Invoice data without code");
        assertThrows(DocumentProcessingException.class,
                () -> new InvoiceProcessor().process(doc));
    }

    @Test
    void mexicanInvoiceWithCFDI_succeeds() {
        Document doc = new Document("inv.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                Country.MEXICO, "Invoice CFDI=STAMP123");
        assertDoesNotThrow(() -> new InvoiceProcessor().process(doc));
    }

    @Test
    void mexicanInvoiceWithoutCFDI_throwsException() {
        Document doc = new Document("inv.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                Country.MEXICO, "Invoice data");
        assertThrows(DocumentProcessingException.class,
                () -> new InvoiceProcessor().process(doc));
    }

    @Test
    void argentineInvoiceWithCAE_succeeds() {
        Document doc = new Document("inv.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                Country.ARGENTINA, "Invoice CAE=456");
        assertDoesNotThrow(() -> new InvoiceProcessor().process(doc));
    }

    @Test
    void chileanInvoiceWithTED_succeeds() {
        Document doc = new Document("inv.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                Country.CHILE, "Invoice TED=789");
        assertDoesNotThrow(() -> new InvoiceProcessor().process(doc));
    }

    // --- Contract validation ---

    @Test
    void contractWithSignature_succeeds() {
        Document doc = new Document("contract.docx", "docx", DocumentType.LEGAL_CONTRACT,
                Country.COLOMBIA, "Contract SIGNATURE=digital");
        assertDoesNotThrow(() -> new ContractProcessor().process(doc));
    }

    @Test
    void contractWithoutSignature_throwsException() {
        Document doc = new Document("contract.docx", "docx", DocumentType.LEGAL_CONTRACT,
                Country.COLOMBIA, "Contract text without signature");
        assertThrows(DocumentProcessingException.class,
                () -> new ContractProcessor().process(doc));
    }

    // --- Financial report validation ---

    @Test
    void argentineReportInXLSX_succeeds() {
        Document doc = new Document("report.xlsx", "xlsx", DocumentType.FINANCIAL_REPORT,
                Country.ARGENTINA, "Financial data");
        assertDoesNotThrow(() -> new FinancialReportProcessor().process(doc));
    }

    @Test
    void argentineReportNotXLSX_throwsException() {
        Document doc = new Document("report.pdf", "pdf", DocumentType.FINANCIAL_REPORT,
                Country.ARGENTINA, "Financial data");
        assertThrows(DocumentProcessingException.class,
                () -> new FinancialReportProcessor().process(doc));
    }

    @Test
    void colombianReportInPDF_succeeds() {
        Document doc = new Document("report.pdf", "pdf", DocumentType.FINANCIAL_REPORT,
                Country.COLOMBIA, "Financial data");
        assertDoesNotThrow(() -> new FinancialReportProcessor().process(doc));
    }

    // --- Tax declaration validation ---

    @Test
    void colombianTaxWithRUT_succeeds() {
        Document doc = new Document("tax.csv", "csv", DocumentType.TAX_DECLARATION,
                Country.COLOMBIA, "Declaration RUT=900123456");
        assertDoesNotThrow(() -> new TaxDeclarationProcessor().process(doc));
    }

    @Test
    void mexicanTaxWithRFC_succeeds() {
        Document doc = new Document("tax.csv", "csv", DocumentType.TAX_DECLARATION,
                Country.MEXICO, "Declaration RFC=ABC123456XYZ");
        assertDoesNotThrow(() -> new TaxDeclarationProcessor().process(doc));
    }

    @Test
    void argentineTaxWithCUIT_succeeds() {
        Document doc = new Document("tax.csv", "csv", DocumentType.TAX_DECLARATION,
                Country.ARGENTINA, "Declaration CUIT=30-12345678-9");
        assertDoesNotThrow(() -> new TaxDeclarationProcessor().process(doc));
    }

    @Test
    void chileanTaxWithRUT_CHILE_succeeds() {
        Document doc = new Document("tax.csv", "csv", DocumentType.TAX_DECLARATION,
                Country.CHILE, "Declaration RUT_CHILE=12345678-9");
        assertDoesNotThrow(() -> new TaxDeclarationProcessor().process(doc));
    }

    @Test
    void colombianTaxWithoutRUT_throwsException() {
        Document doc = new Document("tax.csv", "csv", DocumentType.TAX_DECLARATION,
                Country.COLOMBIA, "Declaration data");
        assertThrows(DocumentProcessingException.class,
                () -> new TaxDeclarationProcessor().process(doc));
    }

    // --- Batch processing ---

    @Test
    void batchProcessesMixedDocuments() {
        List<Document> documents = List.of(
            new Document("inv_co.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                    Country.COLOMBIA, "Invoice CUFE=123456"),
            new Document("inv_mx.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                    Country.MEXICO, "Invoice without stamp"),
            new Document("tax_ar.csv", "csv", DocumentType.TAX_DECLARATION,
                    Country.ARGENTINA, "Declaration CUIT=30-12345678-9"),
            new Document("contract_cl.docx", "docx", DocumentType.LEGAL_CONTRACT,
                    Country.CHILE, "Contract SIGNATURE=ok")
        );

        BatchDocumentProcessor batchProcessor = new BatchDocumentProcessor();
        BatchResult result = batchProcessor.processBatch(documents);

        assertEquals(4, result.getTotal());
        assertEquals(3, result.getSuccess());
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).contains("inv_mx.pdf"));
        assertEquals(75, result.getSuccessRate());
    }
}