# GlobalDocs Solutions - Factory Method Document Processor

A functional HTML terminal implementing the **Factory Method design pattern** with Java-style OOP (ES6 Classes) and a bright, vivid light interface.

## Overview

This application demonstrates the Factory Method pattern applied to a multi-country enterprise document processing system. Built following the `factory-method` skill guidelines with proper Java-style class hierarchy.

## Features

- **Factory Method Pattern**: Product, ConcreteProduct, Creator, ConcreteCreator, FactoryProvider
- **Java-Style OOP**: ES6 Classes with getters, abstract classes, interfaces, enums, static methods
- **Bright Light UI**: Vivid colors, gradients, responsive design
- **Batch Processing**: Error isolation per document (one failure doesn't stop the batch)
- **5 Document Types**: Electronic Invoice, Legal Contract, Financial Report, Digital Certificate, Tax Declaration
- **4 Countries**: Colombia, Mexico, Argentina, Chile (each with unique regulations)
- **7 Formats**: pdf, doc, docx, md, csv, txt, xlsx

## How to Run

### Option 1: Direct Browser (Easiest)
1. Download or clone this repository
2. Double-click `main.html` to open in your browser
3. The terminal will load automatically

### Option 2: Local Server
```bash
# Python 3
python -m http.server 8000

# Node.js
npx http-server

# PHP
php -S localhost:8000
```
Then open `http://localhost:8000/main.html`

## Usage

### Commands

| Command | Description |
|---------|-------------|
| `help` | Show all available commands |
| `demo` | Load 4 sample documents from case study |
| `batch` | Process all queued documents |
| `status` | Show queue and processing stats |
| `clear` | Clear queue and results |
| `add <type> <country> <format> <filename> <content>` | Add document to queue |

### Quick Demo
1. Type `demo` and press Enter
2. Type `batch` and press Enter
3. Observe: 3/4 succeed, 1 fails (Mexico invoice missing CFDI stamp)

### Example
```
add ELECTRONIC_INVOICE COLOMBIA pdf invoice_001.pdf "Invoice data CUFE=ABC123"
add LEGAL_CONTRACT ARGENTINA docx contract.docx "Contract SIGNATURE=digital_ok"
add TAX_DECLARATION MEXICO csv taxes.csv "Declaration RFC=ABC123456"
```

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

### Java-Style Classes

| Class | Role | Pattern Element |
|-------|------|-----------------|
| `DocumentType` | Enum (static constants) | Selection criterion |
| `Country` | Enum (static constants) | Country codes |
| `DocumentFormat` | Utility class | Format validation |
| `Document` | Domain model | Data transfer object |
| `DocumentProcessor` | Interface | Product (contract) |
| `AbstractDocumentProcessor` | Abstract class | Template Method |
| `InvoiceProcessor` | Concrete class | ConcreteProduct |
| `ContractProcessor` | Concrete class | ConcreteProduct |
| `FinancialReportProcessor` | Concrete class | ConcreteProduct |
| `DigitalCertificateProcessor` | Concrete class | ConcreteProduct |
| `TaxDeclarationProcessor` | Concrete class | ConcreteProduct |
| `DocumentProcessorFactory` | Abstract class | Creator |
| `*ProcessorFactory` (5) | Concrete classes | ConcreteCreator |
| `DocumentProcessorFactoryProvider` | Static utility | Factory Provider |
| `DocumentFactory` | Static factory | Document creation |
| `BatchDocumentProcessor` | Service | Batch coordinator |
| `BatchResult` | Value object | Result collector |
| `TerminalUI` | Controller | View/UI handler |

## Country-Specific Validations

| Document Type | Colombia | Mexico | Argentina | Chile |
|---------------|----------|--------|-----------|-------|
| **Invoice** | CUFE (DIAN) | CFDI (SAT) | CAE (AFIP) | TED (SII) |
| **Contract** | SIGNATURE | SIGNATURE | SIGNATURE | SIGNATURE |
| **Financial Report** | Any format | Any format | **XLSX only** | Any format |
| **Certificate** | CERT_AUTHORITY | CERT_AUTHORITY | CERT_AUTHORITY | CERT_AUTHORITY |
| **Tax Declaration** | RUT | RFC | CUIT | RUT_CHILE |

## Technical Details

- **Single file**: `main.html` (HTML + CSS + JavaScript)
- **No dependencies**: Pure vanilla JavaScript
- **No build step**: Direct browser execution
- **Responsive**: Works on desktop and mobile
- **OOP**: Full ES6 class-based implementation following Java conventions

## Case Study

Based on **GlobalDocs Solutions** requirements:
- **Volume**: 50,000+ documents daily
- **Countries**: Colombia, Mexico, Argentina, Chile
- **Challenge**: Different regulations per country
- **Pattern**: Factory Method (delegates object creation to subclasses)