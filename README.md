# GlobalDocs Solutions - Factory Method Document Processor

Proyecto **Java 17** que implementa el patrón de diseño **Factory Method** para el procesamiento de documentos empresariales en Latinoamérica. Incluye interfaz gráfica **Swing** y **HTML** interactiva.

---

## Inicialización del Proyecto

### Prerrequisitos

| Requisito | Versión | Verificar con |
|-----------|---------|---------------|
| Java JDK | 17+ | `java -version` |
| Maven | 3.6+ | `mvn -version` (opcional) |
| Navegador web | Chrome/Firefox/Edge | Para la interfaz HTML |

### Estructura del Proyecto

```
factory-method-document-processor/
├── pom.xml                              ← Configuración Maven (Java 17, JUnit 5)
├── index.html                           ← Interfaz HTML interactiva
├── README.md
├── agents/
│   └── AGENT.md                         ← Definición del caso de estudio
└── src/
    ├── main/java/com/globaldocs/
    │   ├── document/                    ← Paquete de dominio
    │   │   ├── DocumentType.java        ← Enum: 5 tipos de documento
    │   │   ├── Country.java             ← Enum: 4 países LATAM
    │   │   ├── DocumentFormat.java      ← Utility: 7 formatos soportados
    │   │   ├── Document.java            ← Modelo inmutable (dominio)
    │   │   ├── DocumentProcessingException.java  ← Excepción de dominio
    │   │   ├── DocumentProcessor.java   ← Interfaz (Product)
    │   │   ├── AbstractDocumentProcessor.java    ← Abstract (Template Method)
    │   │   ├── InvoiceProcessor.java    ← ConcreteProduct
    │   │   ├── ContractProcessor.java   ← ConcreteProduct
    │   │   ├── FinancialReportProcessor.java     ← ConcreteProduct
    │   │   ├── DigitalCertificateProcessor.java  ← ConcreteProduct
    │   │   └── TaxDeclarationProcessor.java      ← ConcreteProduct
    │   ├── factory/                     ← Paquete de fábricas
    │   │   ├── DocumentProcessorFactory.java     ← Creator abstracto
    │   │   ├── InvoiceProcessorFactory.java      ← ConcreteCreator
    │   │   ├── ContractProcessorFactory.java     ← ConcreteCreator
    │   │   ├── FinancialReportProcessorFactory.java  ← ConcreteCreator
    │   │   ├── DigitalCertificateProcessorFactory.java ← ConcreteCreator
    │   │   ├── TaxDeclarationProcessorFactory.java     ← ConcreteCreator
    │   │   └── DocumentProcessorFactoryProvider.java   ← Provider (acceso único)
    │   ├── batch/                       ← Paquete de procesamiento por lotes
    │   │   ├── BatchResult.java         ← Value object (resultados)
    │   │   └── BatchDocumentProcessor.java ← Coordinador de lotes
    │   └── gui/                         ← Paquete de interfaz
    │       └── DocumentProcessorGui.java ← Swing GUI
    └── test/java/com/globaldocs/factory/
        └── DocumentProcessorFactoryTest.java ← 24 tests JUnit 5
```

---

## Cómo Ejecutar

### Opción 1: Interfaz HTML (Recomendada - Sin compilar)

```bash
# Simplemente abrir index.html en el navegador
# Windows:
start index.html

# macOS:
open index.html

# Linux:
xdg-open index.html
```

### Opción 2: Interfaz Swing (Requiere Java)

```bash
# Compilar todas las clases
javac -encoding UTF-8 -d build -sourcepath src/main/java src/main/java/com/globaldocs/gui/DocumentProcessorGui.java

# Ejecutar la GUI
java -cp build com.globaldocs.gui.DocumentProcessorGui
```

### Opción 3: Con Maven

```bash
# Ejecutar tests
mvn test

# Ejecutar GUI Swing
mvn compile exec:java

# Empaquetar JAR
mvn package
java -cp target/factory-method-document-processor-1.0.0.jar com.globaldocs.gui.DocumentProcessorGui
```

### Opción 4: Ejecutar desde IDE

1. Importar como proyecto Maven (IntelliJ IDEA, Eclipse, VS Code)
2. Ejecutar `DocumentProcessorGui.java` (método main)
3. O ejecutar `DocumentProcessorFactoryTest.java` (tests)

---

## Patrón Factory Method - Arquitectura

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

### Clases Java (20 archivos)

| Paquete | Clase | Rol GoF | Descripción |
|---------|-------|---------|-------------|
| `document` | `DocumentType` | Enum | Criterio de selección (5 tipos) |
| `document` | `Country` | Enum | Países + info de autoridad tributaria |
| `document` | `DocumentFormat` | Utility | Validación de formatos |
| `document` | `Document` | Modelo | Objeto de dominio inmutable |
| `document` | `DocumentProcessingException` | Excepción | Errores de dominio |
| `document` | `DocumentProcessor` | Product (interfaz) | Contrato de procesamiento |
| `document` | `AbstractDocumentProcessor` | Product abstracto | Template Method |
| `document` | `InvoiceProcessor` | ConcreteProduct | Validación CUFE/CFDI/CAE/TED |
| `document` | `ContractProcessor` | ConcreteProduct | Validación de firma digital |
| `document` | `FinancialReportProcessor` | ConcreteProduct | Requisito xlsx Argentina |
| `document` | `DigitalCertificateProcessor` | ConcreteProduct | Validación autoridad certificadora |
| `document` | `TaxDeclarationProcessor` | ConcreteProduct | RUT/RFC/CUIT/RUT_CHILE |
| `factory` | `DocumentProcessorFactory` | Creator (abstracto) | Definición factory method |
| `factory` | `InvoiceProcessorFactory` | ConcreteCreator | Crea InvoiceProcessor |
| `factory` | `ContractProcessorFactory` | ConcreteCreator | Crea ContractProcessor |
| `factory` | `FinancialReportProcessorFactory` | ConcreteCreator | Crea FinancialReportProcessor |
| `factory` | `DigitalCertificateProcessorFactory` | ConcreteCreator | Crea DigitalCertificateProcessor |
| `factory` | `TaxDeclarationProcessorFactory` | ConcreteCreator | Crea TaxDeclarationProcessor |
| `factory` | `DocumentProcessorFactoryProvider` | Provider | Punto de acceso único |
| `batch` | `BatchResult` | Value Object | Tracking de éxito/error |
| `batch` | `BatchDocumentProcessor` | Servicio | Procesamiento con aislamiento de errores |
| `gui` | `DocumentProcessorGui` | Vista/Controller | Swing GUI |

---

## Validaciones por País

| Tipo de Documento | Colombia | México | Argentina | Chile |
|-------------------|----------|--------|-----------|-------|
| **Factura Electrónica** | CUFE (DIAN) | CFDI (SAT) | CAE (AFIP) | TED (SII) |
| **Contrato Legal** | SIGNATURE | SIGNATURE | SIGNATURE | SIGNATURE |
| **Reporte Financiero** | Cualquier formato | Cualquier formato | **Solo XLSX** | Cualquier formato |
| **Certificado Digital** | CERT_AUTHORITY | CERT_AUTHORITY | CERT_AUTHORITY | CERT_AUTHORITY |
| **Declaración Tributaria** | RUT | RFC | CUIT | RUT_CHILE |

---

## Demo Rápida

### HTML
1. Abrir `index.html` en el navegador
2. Hacer clic en **"Load Demo"**
3. Hacer clic en **"Process Batch"**
4. Ver resultados: 3 exitosos, 1 error (México falta CFDI)

### Java Swing
```bash
java -cp build com.globaldocs.gui.DocumentProcessorGui
```
1. Hacer clic en **"Load Demo"**
2. Hacer clic en **"Process Batch"**

---

## Tests JUnit 5

```bash
mvn test
```

24 tests cubriendo:
- Creación de todos los tipos de processor via factory
- Validaciones de país para invoices (CUFE, CFDI, CAE, TED)
- Validación de firma para contratos
- Validación de formato xlsx para Argentina
- Validaciones de campos tributarios (RUT, RFC, CUIT, RUT_CHILE)
- Procesamiento por lotes con aislamiento de errores

---

## Decisiones de Diseño

1. **Creator por ConcreteProduct**: Variante clásica GoF ya que hay 5 tipos que pueden crecer independientemente
2. **Template Method en AbstractDocumentProcessor**: `process()` ejecuta `validateFormat()` → `validateCountryRegulation()` → `doProcess()`
3. **Factory Provider**: Punto de acceso único via `DocumentProcessorFactoryProvider.getFactory()` para desacoplar al cliente
4. **Aislamiento de errores**: `BatchDocumentProcessor` atrapa excepciones por documento, nunca detiene el lote
5. **Documento inmutable**: Validación en constructor con `Objects.requireNonNull`, sin setters

---

## License

MIT License