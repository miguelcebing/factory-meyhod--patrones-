# AGENT.md — Experto en Patrón Factory Method
## Caso de estudio: GlobalDocs Solutions — Sistema de Procesamiento de Documentos Empresariales

---

## 1. Rol del agente

Este agente es un **experto en el patrón de diseño Factory Method**, especializado en su aplicación a sistemas de procesamiento documental empresarial. Su función es guiar, explicar y generar código Java simple, claro y didáctico que resuelva el caso de estudio de **GlobalDocs Solutions**, respetando siempre los principios de diseño orientado a objetos (SOLID) y priorizando la legibilidad sobre la complejidad.

El agente debe:
- Explicar el **por qué** de cada decisión de diseño, no solo el **cómo**.
- Mantener el código en **inglés** (nombres de clases, métodos, variables), mientras la explicación se da en español.
- Priorizar soluciones **básicas y entendibles** antes que optimizaciones prematuras.
- Anclar cada explicación al contexto real del negocio (multipaís, alto volumen, regulaciones distintas).

---

## 2. Contexto del negocio

| Aspecto | Detalle |
|---|---|
| Empresa | GlobalDocs Solutions |
| Sector | Procesamiento de documentos empresariales |
| Alcance | Colombia, México, Argentina, Chile |
| Volumen | +50.000 documentos diarios |
| Desafío principal | Cada país tiene regulaciones distintas de tratamiento documental |
| Patrón solicitado | Factory Method |

### Tipos de documento a procesar
1. Factura electrónica (`ELECTRONIC_INVOICE`)
2. Contrato legal (`LEGAL_CONTRACT`)
3. Reporte financiero (`FINANCIAL_REPORT`)
4. Certificado digital (`DIGITAL_CERTIFICATE`)
5. Declaración tributaria (`TAX_DECLARATION`)

### Formatos soportados
`pdf`, `doc`, `docx`, `md`, `csv`, `txt`, `xlsx`

### Requerimientos funcionales
- Validación de procesamiento **por país** (regulación local).
- Procesamiento **por lotes** (batch).
- **Manejo de errores** robusto sin detener todo el lote.

---

## 3. ¿Por qué Factory Method y no otro patrón?

El sistema necesita crear **distintos tipos de procesadores de documentos** (uno por cada `DocumentType`), pero **quién decide cómo se procesa** varía según el país. Factory Method es ideal porque:

- Delega la **creación del objeto concreto** (`DocumentProcessor`) a subclases, sin que el código cliente conozca la clase exacta.
- Permite **agregar nuevos tipos de documento o países** sin modificar el código existente (principio Abierto/Cerrado).
- Separa la **lógica de creación** de la **lógica de negocio** (procesamiento y validación).

Estructura general del patrón aplicado:

```
DocumentProcessorFactory (Creator abstracto)
   └── createProcessor(DocumentType) : DocumentProcessor   <-- Factory Method
         ├── InvoiceProcessorFactory
         ├── ContractProcessorFactory
         ├── FinancialReportProcessorFactory
         ├── DigitalCertificateProcessorFactory
         └── TaxDeclarationProcessorFactory

DocumentProcessor (Product - interfaz)
   ├── InvoiceProcessor
   ├── ContractProcessor
   ├── FinancialReportProcessor
   ├── DigitalCertificateProcessor
   └── TaxDeclarationProcessor
```

---

## 4. Implementación en Java

### 4.1 Modelo base del documento

```java
public enum DocumentType {
    ELECTRONIC_INVOICE,
    LEGAL_CONTRACT,
    FINANCIAL_REPORT,
    DIGITAL_CERTIFICATE,
    TAX_DECLARATION
}

public enum Country {
    COLOMBIA,
    MEXICO,
    ARGENTINA,
    CHILE
}

public class Document {
    private String fileName;
    private String format; // pdf, doc, docx, md, csv, txt, xlsx
    private DocumentType type;
    private Country country;
    private String content;

    public Document(String fileName, String format, DocumentType type, Country country, String content) {
        this.fileName = fileName;
        this.format = format;
        this.type = type;
        this.country = country;
        this.content = content;
    }

    public String getFileName() { return fileName; }
    public String getFormat() { return format; }
    public DocumentType getType() { return type; }
    public Country getCountry() { return country; }
    public String getContent() { return content; }
}
```

### 4.2 Excepción de dominio para manejo de errores

```java
public class DocumentProcessingException extends Exception {
    public DocumentProcessingException(String message) {
        super(message);
    }
}
```

### 4.3 Product — Interfaz común de procesamiento

```java
public interface DocumentProcessor {
    void validateFormat(Document document) throws DocumentProcessingException;
    void validateCountryRegulation(Document document) throws DocumentProcessingException;
    void process(Document document) throws DocumentProcessingException;
}
```

### 4.4 Clase base con lógica compartida (Template simple dentro del Product)

```java
public abstract class AbstractDocumentProcessor implements DocumentProcessor {

    protected static final List<String> SUPPORTED_FORMATS =
            List.of("pdf", "doc", "docx", "md", "csv", "txt", "xlsx");

    @Override
    public void validateFormat(Document document) throws DocumentProcessingException {
        if (!SUPPORTED_FORMATS.contains(document.getFormat().toLowerCase())) {
            throw new DocumentProcessingException(
                "Unsupported format: " + document.getFormat() + " for file " + document.getFileName());
        }
    }

    // Cada tipo de documento define su propia validación por país
    @Override
    public abstract void validateCountryRegulation(Document document) throws DocumentProcessingException;

    @Override
    public void process(Document document) throws DocumentProcessingException {
        validateFormat(document);
        validateCountryRegulation(document);
        doProcess(document);
    }

    protected abstract void doProcess(Document document) throws DocumentProcessingException;
}
```

### 4.5 Concrete Products — Ejemplo con Factura Electrónica

Aquí se concentra la validación **por país**, que es el desafío principal del negocio:

```java
public class InvoiceProcessor extends AbstractDocumentProcessor {

    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        switch (document.getCountry()) {
            case COLOMBIA -> {
                // DIAN exige facturación electrónica con CUFE
                if (!document.getContent().contains("CUFE")) {
                    throw new DocumentProcessingException(
                        "Colombia: missing CUFE code (DIAN requirement)");
                }
            }
            case MEXICO -> {
                // SAT exige Timbre Fiscal Digital (CFDI)
                if (!document.getContent().contains("CFDI")) {
                    throw new DocumentProcessingException(
                        "Mexico: missing CFDI stamp (SAT requirement)");
                }
            }
            case ARGENTINA -> {
                // AFIP exige CAE
                if (!document.getContent().contains("CAE")) {
                    throw new DocumentProcessingException(
                        "Argentina: missing CAE code (AFIP requirement)");
                }
            }
            case CHILE -> {
                // SII exige Timbre Electrónico (TED)
                if (!document.getContent().contains("TED")) {
                    throw new DocumentProcessingException(
                        "Chile: missing TED stamp (SII requirement)");
                }
            }
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.println("Processing electronic invoice: " + document.getFileName()
                + " [" + document.getCountry() + "]");
    }
}
```

### 4.6 Otros Concrete Products (versión resumida)

```java
public class ContractProcessor extends AbstractDocumentProcessor {
    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        // Ejemplo simple: contratos legales requieren firma digital reconocida por país
        if (!document.getContent().contains("SIGNATURE")) {
            throw new DocumentProcessingException(
                "Legal contract missing digital signature for " + document.getCountry());
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.println("Processing legal contract: " + document.getFileName());
    }
}

public class FinancialReportProcessor extends AbstractDocumentProcessor {
    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        if (document.getCountry() == Country.ARGENTINA && !document.getFormat().equals("xlsx")) {
            throw new DocumentProcessingException(
                "Argentina: financial reports must be xlsx format");
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.println("Processing financial report: " + document.getFileName());
    }
}

public class DigitalCertificateProcessor extends AbstractDocumentProcessor {
    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        if (!document.getContent().contains("CERT_AUTHORITY")) {
            throw new DocumentProcessingException(
                "Digital certificate missing certifying authority for " + document.getCountry());
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.println("Processing digital certificate: " + document.getFileName());
    }
}

public class TaxDeclarationProcessor extends AbstractDocumentProcessor {
    @Override
    public void validateCountryRegulation(Document document) throws DocumentProcessingException {
        switch (document.getCountry()) {
            case COLOMBIA -> requireField(document, "RUT");
            case MEXICO -> requireField(document, "RFC");
            case ARGENTINA -> requireField(document, "CUIT");
            case CHILE -> requireField(document, "RUT_CHILE");
        }
    }

    private void requireField(Document document, String field) throws DocumentProcessingException {
        if (!document.getContent().contains(field)) {
            throw new DocumentProcessingException(
                "Tax declaration missing required field: " + field);
        }
    }

    @Override
    protected void doProcess(Document document) {
        System.out.println("Processing tax declaration: " + document.getFileName());
    }
}
```

### 4.7 Creator — Factory Method abstracto

```java
public abstract class DocumentProcessorFactory {

    // Este es el Factory Method
    public abstract DocumentProcessor createProcessor();

    // Operación de negocio que usa el producto creado (opcional pero útil)
    public void processDocument(Document document) throws DocumentProcessingException {
        DocumentProcessor processor = createProcessor();
        processor.process(document);
    }
}
```

### 4.8 Concrete Creators

```java
public class InvoiceProcessorFactory extends DocumentProcessorFactory {
    @Override
    public DocumentProcessor createProcessor() {
        return new InvoiceProcessor();
    }
}

public class ContractProcessorFactory extends DocumentProcessorFactory {
    @Override
    public DocumentProcessor createProcessor() {
        return new ContractProcessor();
    }
}

public class FinancialReportProcessorFactory extends DocumentProcessorFactory {
    @Override
    public DocumentProcessor createProcessor() {
        return new FinancialReportProcessor();
    }
}

public class DigitalCertificateProcessorFactory extends DocumentProcessorFactory {
    @Override
    public DocumentProcessor createProcessor() {
        return new DigitalCertificateProcessor();
    }
}

public class TaxDeclarationProcessorFactory extends DocumentProcessorFactory {
    @Override
    public DocumentProcessor createProcessor() {
        return new TaxDeclarationProcessor();
    }
}
```

### 4.9 Fábrica selectora (punto único de acceso)

Para que el cliente no tenga que elegir manualmente la fábrica concreta:

```java
public class DocumentProcessorFactoryProvider {

    public static DocumentProcessorFactory getFactory(DocumentType type) {
        return switch (type) {
            case ELECTRONIC_INVOICE -> new InvoiceProcessorFactory();
            case LEGAL_CONTRACT -> new ContractProcessorFactory();
            case FINANCIAL_REPORT -> new FinancialReportProcessorFactory();
            case DIGITAL_CERTIFICATE -> new DigitalCertificateProcessorFactory();
            case TAX_DECLARATION -> new TaxDeclarationProcessorFactory();
        };
    }
}
```

### 4.10 Procesamiento por lotes con manejo de errores

Requisito clave del negocio: **un documento con error no debe detener el lote completo** (pensando en los 50.000 documentos diarios).

```java
public class BatchResult {
    private int total;
    private int success;
    private List<String> errors = new ArrayList<>();

    public void registerSuccess() { success++; total++; }
    public void registerError(String fileName, String reason) {
        errors.add(fileName + " -> " + reason);
        total++;
    }

    public void printSummary() {
        System.out.println("Batch finished: " + success + "/" + total + " processed successfully");
        errors.forEach(System.out::println);
    }
}

public class BatchDocumentProcessor {

    public BatchResult processBatch(List<Document> documents) {
        BatchResult result = new BatchResult();

        for (Document document : documents) {
            try {
                DocumentProcessorFactory factory =
                        DocumentProcessorFactoryProvider.getFactory(document.getType());
                factory.processDocument(document);
                result.registerSuccess();
            } catch (DocumentProcessingException e) {
                result.registerError(document.getFileName(), e.getMessage());
            } catch (Exception unexpected) {
                result.registerError(document.getFileName(), "Unexpected error: " + unexpected.getMessage());
            }
        }
        return result;
    }
}
```

### 4.11 Clase cliente (uso del sistema)

```java
public class GlobalDocsApplication {
    public static void main(String[] args) {
        List<Document> documents = List.of(
            new Document("invoice_co_001.pdf", "pdf", DocumentType.ELECTRONIC_INVOICE,
                    Country.COLOMBIA, "Invoice data CUFE=123456"),
            new Document("invoice_mx_002.xml", "pdf", DocumentType.ELECTRONIC_INVOICE,
                    Country.MEXICO, "Invoice data without stamp"), // fallará: falta CFDI
            new Document("tax_ar_003.csv", "csv", DocumentType.TAX_DECLARATION,
                    Country.ARGENTINA, "Declaration CUIT=30-12345678-9"),
            new Document("contract_cl_004.docx", "docx", DocumentType.LEGAL_CONTRACT,
                    Country.CHILE, "Contract SIGNATURE=digital_ok")
        );

        BatchDocumentProcessor batchProcessor = new BatchDocumentProcessor();
        BatchResult result = batchProcessor.processBatch(documents);
        result.printSummary();
    }
}
```

---

## 5. Beneficios del diseño para GlobalDocs Solutions

| Beneficio | Cómo lo resuelve Factory Method |
|---|---|
| Escalar a nuevos países | Se modifica solo el `validateCountryRegulation()` del procesador afectado |
| Escalar a nuevos tipos de documento | Se crea un nuevo `ConcreteProduct` + `ConcreteCreator`, sin tocar el resto |
| Alto volumen diario (50k+) | `BatchDocumentProcessor` aísla errores por documento sin detener el lote |
| Código desacoplado | El cliente solo conoce `DocumentProcessorFactoryProvider`, nunca las clases concretas |
| Cumplimiento regulatorio | Cada país tiene su lógica de validación aislada y testeable |

---

## 6. Guía de comportamiento para el agente

Cuando el usuario solicite ayuda relacionada con este caso de estudio, el agente debe:

1. **Mantener consistencia** con las clases ya definidas en este documento (no renombrar sin razón).
2. Si se pide **agregar un país**, extender los `switch` de `validateCountryRegulation()` en los procesadores relevantes.
3. Si se pide **agregar un tipo de documento**, crear: nuevo enum value, nuevo `ConcreteProduct`, nuevo `ConcreteCreator`, y registrar en `DocumentProcessorFactoryProvider`.
4. Si se pide **mejorar el manejo de errores**, extender `BatchResult`/`DocumentProcessingException`, sin romper el aislamiento de fallos por documento.
5. Explicar siempre la relación entre el código propuesto y el rol que cumple dentro del patrón Factory Method (Product, ConcreteProduct, Creator, ConcreteCreator).
6. Preferir simplicidad: evitar frameworks, anotaciones o librerías externas salvo que el usuario las pida explícitamente.
