# GlobalDocs Solutions - Factory Method Document Processor

A functional HTML terminal application implementing the **Factory Method design pattern** for enterprise document processing, based on the GlobalDocs Solutions case study.

## Overview

This application demonstrates the Factory Method pattern applied to a multi-country document processing system. It processes 5 types of documents across 4 countries (Colombia, Mexico, Argentina, Chile) with country-specific regulatory validation.

## Features

- **Factory Method Pattern**: Complete implementation with Product, ConcreteProduct, Creator, and ConcreteCreator classes
- **Interactive Terminal UI**: Command-line interface in the browser
- **Batch Processing**: Processes multiple documents with error isolation (one failure doesn't stop the batch)
- **Country-Specific Validation**: Each country has unique regulatory requirements
- **5 Document Types**: Electronic Invoice, Legal Contract, Financial Report, Digital Certificate, Tax Declaration
- **7 Supported Formats**: pdf, doc, docx, md, csv, txt, xlsx

## How to Run

### Option 1: Direct Browser (Easiest)
1. Download or clone this repository
2. Double-click `main.html` to open in your default browser
3. The terminal will load automatically

### Option 2: Local Server (Recommended)
```bash
# Using Python 3
python -m http.server 8000

# Using Node.js (if http-server installed)
npx http-server

# Using PHP
php -S localhost:8000
```
Then open `http://localhost:8000/main.html` in your browser.

## Usage

### Commands

| Command | Description |
|---------|-------------|
| `help` | Show all available commands |
| `demo` | Load sample documents from the case study |
| `batch` | Process all queued documents |
| `clear` | Clear queue and results |
| `add <type> <country> <format> <filename> <content>` | Add document to queue |

### Document Types
- `ELECTRONIC_INVOICE`
- `LEGAL_CONTRACT`
- `FINANCIAL_REPORT`
- `DIGITAL_CERTIFICATE`
- `TAX_DECLARATION`

### Countries
- `COLOMBIA`
- `MEXICO`
- `ARGENTINA`
- `CHILE`

### Formats
- `pdf`, `doc`, `docx`, `md`, `csv`, `txt`, `xlsx`

### Quick Start Demo
1. Type `demo` and press Enter
2. Type `batch` and press Enter
3. Observe results - 3/4 documents will succeed, 1 will fail (Mexico invoice missing CFDI)

### Example: Add Custom Document
```
add ELECTRONIC_INVOICE COLOMBIA pdf my_invoice.pdf "Invoice data CUFE=ABC123"
add LEGAL_CONTRACT ARGENTINA docx contract.docx "Contract SIGNATURE=digital_ok"
add TAX_DECLARATION MEXICO csv taxes.csv "Declaration RFC=ABC123456XYZ"
```

## Architecture (Factory Method Pattern)

```
DocumentProcessorFactory (Creator - Abstract)
    └── createProcessor() : DocumentProcessor  <-- Factory Method
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
```

### Key Classes

| Class | Pattern Role | Responsibility |
|-------|--------------|----------------|
| `DocumentProcessor` | Product (Interface) | Defines processing contract |
| `AbstractDocumentProcessor` | Base Product | Shared format validation + template method |
| `InvoiceProcessor` | ConcreteProduct | CUFE/CFDI/CAE/TED validation per country |
| `ContractProcessor` | ConcreteProduct | Digital signature validation |
| `FinancialReportProcessor` | ConcreteProduct | Argentina xlsx requirement |
| `DigitalCertificateProcessor` | ConcreteProduct | Cert authority validation |
| `TaxDeclarationProcessor` | ConcreteProduct | RUT/RFC/CUIT/RUT_CHILE validation |
| `DocumentProcessorFactory` | Creator (Abstract) | Factory Method + processDocument() |
| `*Factory` (5 classes) | ConcreteCreator | Returns specific processor |
| `DocumentProcessorFactoryProvider` | Factory Selector | Single access point via `getFactory()` |
| `BatchDocumentProcessor` | Coordinator | Batch processing with error isolation |
| `BatchResult` | Result Collector | Tracks success/error counts |

## Country-Specific Validations

| Document Type | Colombia | Mexico | Argentina | Chile |
|---------------|----------|--------|-----------|-------|
| **Electronic Invoice** | CUFE (DIAN) | CFDI (SAT) | CAE (AFIP) | TED (SII) |
| **Legal Contract** | SIGNATURE | SIGNATURE | SIGNATURE | SIGNATURE |
| **Financial Report** | Any format | Any format | **XLSX only** | Any format |
| **Digital Certificate** | CERT_AUTHORITY | CERT_AUTHORITY | CERT_AUTHORITY | CERT_AUTHORITY |
| **Tax Declaration** | RUT | RFC | CUIT | RUT_CHILE |

## Error Handling

The batch processor isolates errors per document:
- Failed documents are logged with specific error messages
- Successful documents continue processing
- Summary shows: `Batch finished: X/Y processed successfully`

## Technical Details

- **Single file**: `main.html` (HTML + CSS + JavaScript)
- **No dependencies**: Pure vanilla JavaScript (ES6 Classes)
- **No build step**: Runs directly in browser
- **Responsive**: Works on desktop and mobile
- **OOP**: Full ES6 class-based implementation

## Case Study Context

Based on **GlobalDocs Solutions** requirements:
- **Volume**: 50,000+ documents daily
- **Countries**: Colombia, Mexico, Argentina, Chile
- **Challenge**: Different regulations per country
- **Pattern**: Factory Method (delegates object creation to subclasses)

## License

MIT License - Free to use and modify.