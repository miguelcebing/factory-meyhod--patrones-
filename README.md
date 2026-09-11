# GlobalDocs Solutions - Factory Method Document Processor

Java project implementing the **Factory Method design pattern** for enterprise document processing across LATAM countries.

## Project Structure

```
factory-method-document-processor/
├── pom.xml
├── README.md
├── agents/
│   └── AGENT.md                          # Case study definition
└── src/
    ├── main/java/com/globaldocs/
    │   ├── document/
    │   │   ├── DocumentType.java          # Enum (selection criterion)
    │   │   ├── Country.java               # Enum (COLOMBIA, MEXICO, ARGENTINA, CHILE)
    │   │   ├── DocumentFormat.java        # Utility class (supported formats)
    │   │   ├── Document.java              # Domain model (immutable)
    │   │   ├── DocumentProcessingException.java  # Custom exception
    │   │   ├── DocumentProcessor.java     # Product interface
    │   │   ├── AbstractDocumentProcessor.java    # Abstract product (Template Method)
    │   │   ├── InvoiceProcessor.java      # ConcreteProduct
    │   │   ├── ContractProcessor.java     # ConcreteProduct
    │   │   ├── FinancialReportProcessor.java     # ConcreteProduct
    │   │   ├── DigitalCertificateProcessor.java  # ConcreteProduct
    │   │   └── TaxDeclarationProcessor.java      # ConcreteProduct
    │   ├── factory/
    │   │   ├── DocumentProcessorFactory.java      # Creator abstract
    │   │   ├── InvoiceProcessorFactory.java       # ConcreteCreator
    │   │   ├── ContractProcessorFactory.java      # ConcreteCreator
    │   │   ├── FinancialReportProcessorFactory.java  # ConcreteCreator
    │   │   ├── DigitalCertificateProcessorFactory.java  # ConcreteCreator
    │   │   ├── TaxDeclarationProcessorFactory.java      # ConcreteCreator
    │   │   └── DocumentProcessorFactoryProvider.java    # Factory Provider
    │   ├── batch/
    │   │   ├── BatchResult.java           # Result collector
    │   │   └── BatchDocumentProcessor.java  # Batch coordinator
    │   └── gui/
    │       └── DocumentProcessorGui.java  # Swing GUI
    └── test/java/com/globaldocs/factory/
        └── DocumentProcessorFactoryTest.java  # JUnit 5 tests (24 tests)
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Run Tests
```bash
mvn test
```

### Run GUI
```bash
mvn compile exec:java
```
Or:
```bash
mvn package
java -cp target/factory-method-document-processor-1.0.0.jar com.globaldocs.gui.DocumentProcessorGui
```

### Run from IDE
1. Import as Maven project
2. Run `DocumentProcessorGui.java` (main method)

## Architecture (Factory Method Pattern)

```
DocumentProcessorFactory (Creator - Abstract)
    └── createProcessor() : DocumentProcessor  ← Factory Method
          ├── InvoiceProcessorFactory
          ├── ContractProcessorFactory
          ├── FinancialReportProcessorFactory
          ├── DigitalCertificateProcessorFactory
          └── TaxDeclarationProcessorFactory

DocumentProcessor (Product - Interface)
    ├── InvoiceProcessor
    ├── ContractProcessor
    ├── FinancialReportProcessor
    ├── DigitalCertificateProcessor
    └── TaxDeclarationProcessor

DocumentProcessorFactoryProvider (Single Access Point)
    └── getFactory(DocumentType) → Factory
```

## Classes (20 Java Files)

| Package | Class | Role (GoF) | Description |
|---------|-------|------------|-------------|
| `document` | `DocumentType` | Enum | Selection criterion (5 types) |
| `document` | `Country` | Enum | Country codes + tax authority info |
| `document` | `DocumentFormat` | Utility | Supported formats validation |
| `document` | `Document` | Model | Immutable domain object |
| `document` | `DocumentProcessingException` | Exception | Domain-specific errors |
| `document` | `DocumentProcessor` | Product (interface) | Processing contract |
| `document` | `AbstractDocumentProcessor` | Abstract Product | Template Method pattern |
| `document` | `InvoiceProcessor` | ConcreteProduct | CUFE/CFDI/CAE/TED validation |
| `document` | `ContractProcessor` | ConcreteProduct | Digital signature validation |
| `document` | `FinancialReportProcessor` | ConcreteProduct | Argentina xlsx requirement |
| `document` | `DigitalCertificateProcessor` | ConcreteProduct | Cert authority validation |
| `document` | `TaxDeclarationProcessor` | ConcreteProduct | RUT/RFC/CUIT/RUT_CHILE |
| `factory` | `DocumentProcessorFactory` | Creator (abstract) | Factory Method definition |
| `factory` | `InvoiceProcessorFactory` | ConcreteCreator | Creates InvoiceProcessor |
| `factory` | `ContractProcessorFactory` | ConcreteCreator | Creates ContractProcessor |
| `factory` | `FinancialReportProcessorFactory` | ConcreteCreator | Creates FinancialReportProcessor |
| `factory` | `DigitalCertificateProcessorFactory` | ConcreteCreator | Creates DigitalCertificateProcessor |
| `factory` | `TaxDeclarationProcessorFactory` | ConcreteCreator | Creates TaxDeclarationProcessor |
| `factory` | `DocumentProcessorFactoryProvider` | Factory Provider | Single access point |
| `batch` | `BatchResult` | Value Object | Success/error tracking |
| `batch` | `BatchDocumentProcessor` | Service | Error-isolated batch processing |
| `gui` | `DocumentProcessorGui` | View/Controller | Swing GUI |

## Country-Specific Validations

| Document Type | Colombia | Mexico | Argentina | Chile |
|---------------|----------|--------|-----------|-------|
| **Invoice** | CUFE (DIAN) | CFDI (SAT) | CAE (AFIP) | TED (SII) |
| **Contract** | SIGNATURE | SIGNATURE | SIGNATURE | SIGNATURE |
| **Financial Report** | Any format | Any format | **XLSX only** | Any format |
| **Certificate** | CERT_AUTHORITY | CERT_AUTHORITY | CERT_AUTHORITY | CERT_AUTHORITY |
| **Tax Declaration** | RUT | RFC | CUIT | RUT_CHILE |

## Key Design Decisions

1. **One Creator per ConcreteProduct**: Classic GoF variant since there are 5 document types that may grow independently
2. **Template Method in AbstractDocumentProcessor**: `process()` calls `validateFormat()` → `validateCountryRegulation()` → `doProcess()`
3. **Factory Provider**: Single access point via `DocumentProcessorFactoryProvider.getFactory()` to decouple client from concrete factories
4. **Error isolation**: `BatchDocumentProcessor` catches exceptions per document, never stops the batch
5. **Immutable Document**: Constructor validation with `Objects.requireNonNull`, no setters

## License

MIT License