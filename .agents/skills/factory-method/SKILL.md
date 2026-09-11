---
name: factory-method
description: Crea proyectos Java aplicando el patrón de diseño Factory Method. Úsala SIEMPRE que el usuario pida crear, generar o estructurar un proyecto/módulo en Java a partir de una descripción informal, una lista de productos/variantes a crear, o cuando mencione explícitamente "factory method", "fábrica", "creación de objetos" o "patrón de diseño" en contexto Java. También aplica cuando el usuario entregue información desordenada (requisitos sueltos, notas, bullets) y pida que se "arregle", "organice" o "estructure" antes de generar el código. No usar para otros patrones (Builder, Singleton, Strategy, etc.) salvo que el usuario pida combinarlos explícitamente con Factory Method.
---

# Java Factory Method Scaffolder

Skill para actuar como ingeniero de software senior (10+ años) que recibe una petición
—muchas veces ambigua o desordenada— y entrega un proyecto Java **listo para compilar**
que implementa correctamente el patrón **Factory Method**.

## Filosofía

- Simplicidad sobre ingeniería excesiva: Factory Method resuelve "quién decide qué
  subclase concreta instanciar", no más que eso. No se añaden Abstract Factory,
  Registry dinámico ni reflexión salvo que el usuario lo pida explícitamente.
- Todo el código, nombres de clases, paquetes, métodos y comentarios van en **inglés**.
  Las explicaciones al usuario van en el idioma en que él escribe (normalmente español).
- Comentarios solo explican el "por qué" (ej. por qué el creador es abstracto), nunca
  el "qué" obvio del código.
- Cero dependencias externas: solo JDK estándar + JUnit 5 para el test de ejemplo.

---

## Flujo de trabajo (seguir en orden)

### 1. Recolectar y limpiar la información (paso obligatorio "arreglar info")

Antes de escribir una sola línea de código:

1. Lee la petición completa del usuario, incluso si viene en bullets sueltos, texto
   pegado de un chat, o mezcla de idiomas.
2. Extrae y normaliza en una tabla interna (mostrar al usuario si la entrada era
   desordenada, para que confirme):

   | Campo | Qué extraer |
   |---|---|
   | Dominio del negocio | ej. "notificaciones", "pagos", "vehículos" |
   | Producto abstracto | el "qué" se va a crear (interfaz/clase base) |
   | Variantes concretas | cada tipo específico mencionado (Email, SMS, Push...) |
   | Atributos/comportamiento por variante | qué hace cada una distinto |
   | Criterio de selección | cómo se decide qué variante crear (enum, string, config) |
   | Restricciones técnicas | versión de Java, build tool, framework si aplica |

3. Traduce todos los nombres de dominio a inglés técnico estándar (PascalCase para
   clases, camelCase para métodos/variables). Si un término no tiene traducción obvia
   o es ambiguo, pregunta una única vez antes de continuar (ver sección "Cuándo
   preguntar").
4. Si detectas variantes duplicadas, contradictorias o un criterio de selección
   inexistente, resuélvelo con la opción más simple y decláralo brevemente
   ("asumo selección por enum `NotificationType` porque no se especificó origen del
   dato") en vez de detener el trabajo.

### 2. Cuándo preguntar (una sola pregunta clave)

Solo pausar y preguntar si falta información que cambia la estructura del código
(ej. "¿las variantes se seleccionan en tiempo de compilación o vienen de un
`application.properties`/config externo?"). Si la petición es completa o el default
razonable no compromete el diseño, no preguntes: asume y decláralo en una línea.

### 3. Diseñar la estructura Factory Method

Mapeo estándar de roles del patrón → nombres en el proyecto:

| Rol GoF | Elemento Java | Convención de nombre |
|---|---|---|
| Product (interfaz/abstracta) | `interface` o `abstract class` | `<Domain>` (ej. `Notification`) |
| ConcreteProduct | clase concreta | `<Variant><Domain>` (ej. `EmailNotification`) |
| Creator (abstracto) | `abstract class` con el factory method abstracto | `<Domain>Factory` (ej. `NotificationFactory`) |
| ConcreteCreator | subclase del creator, una por variante o un único creator con `switch`/enum | ver decisión abajo |
| Cliente | clase que consume el factory | `<Domain>Client` o el caller real que el usuario indique |

**Decisión de diseño (justificar brevemente en la respuesta al usuario):**

- Si el número de variantes es fijo y pequeño (2–6) y no van a crecer dinámicamente:
  usar **un solo Creator concreto con un `switch` sobre un `enum`** que decide qué
  `ConcreteProduct` instanciar. Es la variante de Factory Method más simple y evita
  crear una jerarquía paralela de Creators innecesaria (trade-off: menos "puro" GoF,
  pero mucho más mantenible para casos chicos — YAGNI).
- Si el usuario indica explícitamente que habrá múltiples familias de creadores
  (ej. un Creator por región, por proveedor, por entorno) o que nuevas variantes se
  añadirán con frecuencia por distintos equipos: usar **una subclase concreta de
  Creator por variante**, que es el Factory Method "clásico" de GoF (cada subclase
  sobreescribe el método fábrica).
- Nunca uses reflexión (`Class.forName`) ni un registro dinámico salvo pedido explícito
  — es over-engineering para el 95% de los casos.

### 4. Generar el proyecto

Estructura estándar Maven (usar Gradle solo si el usuario lo pide):

```
project-root/
├── pom.xml
├── src/
│   ├── main/java/com/<company>/<domain>/
│   │   ├── <Domain>.java                  (Product)
│   │   ├── <Variant1><Domain>.java        (ConcreteProduct)
│   │   ├── <Variant2><Domain>.java
│   │   ├── <Domain>Factory.java           (Creator)
│   │   └── (subclases si aplica variante "clásica")
│   └── test/java/com/<company>/<domain>/
│       └── <Domain>FactoryTest.java
└── README.md
```

Si el usuario no da un nombre de compañía/paquete, usar `com.example.<domain>` y
mencionarlo como asunción, no preguntarlo.

Reglas de código:

- Product como `interface` cuando las variantes no comparten implementación; como
  `abstract class` solo si hay comportamiento común real (evitar herencia por defecto).
- Constructor validation en cada `ConcreteProduct` cuando reciba parámetros (ej.
  `Objects.requireNonNull`, validar strings no vacíos) — nunca dejar un campo sin
  validar si se usa para lógica de negocio.
- El factory method lanza `IllegalArgumentException` con mensaje claro ante un tipo
  no soportado — nunca retorna `null` silenciosamente.
- `enum` para el criterio de selección siempre que el conjunto de variantes sea
  cerrado y conocido en compilación (más seguro que `String` — el compilador atrapa
  typos y el `switch` puede ser exhaustivo).
- Marcar con `// TODO:` deuda técnica explícita, ej. si el criterio de selección
  debería venir de configuración externa pero se implementó hardcoded porque no se
  especificó el origen.

### 5. Test / ejemplo mínimo obligatorio

Todo proyecto generado incluye un test JUnit 5 (`src/test/java/...`) que:
- Crea al menos una instancia de cada variante vía la factory.
- Verifica el tipo concreto retornado (`assertInstanceOf` o equivalente).
- Verifica que un tipo inválido lanza la excepción esperada.

No se entrega el proyecto sin este test — es la prueba de que compila y funciona.

### 6. Entrega

1. Generar todos los archivos reales (no snippets sueltos) con `create_file`.
2. Compilar mentalmente / revisar que los imports y paquetes sean consistentes en
   todos los archivos antes de presentarlos.
3. Presentar el proyecto con `present_files`, apuntando primero al archivo más
   relevante para que el usuario entienda la estructura (`<Domain>Factory.java` o el
   `README.md` si se generó).
4. En la respuesta de texto (no en los archivos): resumir en 3–5 líneas qué se generó,
   la decisión de diseño tomada (switch único vs subclases) y el trade-off, y cómo
   correr el test (`mvn test`).

---

## Ejemplo de referencia (no copiar literal, usar como plantilla mental)

Dominio: sistema de notificaciones con variantes Email y SMS, selección por enum,
conjunto cerrado de 2 variantes → se elige la opción "switch único" (regla del punto 3).

`Notification.java`
```java
package com.example.notification;

public interface Notification {
    void send(String recipient, String message);
}
```

`EmailNotification.java`
```java
package com.example.notification;

import java.util.Objects;

public final class EmailNotification implements Notification {

    private final String fromAddress;

    public EmailNotification(String fromAddress) {
        // Validated eagerly: a notification with a null sender is a configuration bug,
        // not a runtime condition to handle gracefully.
        this.fromAddress = Objects.requireNonNull(fromAddress, "fromAddress must not be null");
    }

    @Override
    public void send(String recipient, String message) {
        System.out.printf("Email from %s to %s: %s%n", fromAddress, recipient, message);
    }
}
```

`SmsNotification.java`
```java
package com.example.notification;

public final class SmsNotification implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.printf("SMS to %s: %s%n", recipient, message);
    }
}
```

`NotificationFactory.java`
```java
package com.example.notification;

public final class NotificationFactory {

    public enum NotificationType { EMAIL, SMS }

    private NotificationFactory() {
        // Stateless factory: no instance needed.
    }

    public static Notification create(NotificationType type) {
        return switch (type) {
            case EMAIL -> new EmailNotification("no-reply@example.com");
            case SMS -> new SmsNotification();
        };
        // No default branch: an exhaustive switch on the enum makes the compiler
        // catch missing cases if a new NotificationType is added later.
    }
}
```

`NotificationFactoryTest.java`
```java
package com.example.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationFactoryTest {

    @Test
    void createsEmailNotification() {
        Notification notification = NotificationFactory.create(NotificationFactory.NotificationType.EMAIL);
        assertInstanceOf(EmailNotification.class, notification);
    }

    @Test
    void createsSmsNotification() {
        Notification notification = NotificationFactory.create(NotificationFactory.NotificationType.SMS);
        assertInstanceOf(SmsNotification.class, notification);
    }

    @Test
    void rejectsNullType() {
        assertThrows(NullPointerException.class, () -> NotificationFactory.create(null));
    }
}
```

Cuando el caso amerite la variante "clásica" (subclase de Creator por variante),
usar en su lugar:

```java
public abstract class NotificationCreator {
    public abstract Notification createNotification();
}

public final class EmailNotificationCreator extends NotificationCreator {
    @Override
    public Notification createNotification() {
        return new EmailNotification("no-reply@example.com");
    }
}
```

---

## Checklist de edge cases a cubrir siempre

- [ ] Tipo/variante no reconocida → excepción clara, nunca `null`.
- [ ] Parámetros nulos o vacíos en constructores de `ConcreteProduct` → validados.
- [ ] Nuevas variantes futuras: `enum` + `switch` exhaustivo (sin `default`) para que
      el compilador obligue a actualizar la factory.
- [ ] Paquetes y nombres consistentes entre todos los archivos generados.
- [ ] Si el dominio tiene datos sensibles (ej. credenciales de envío), no hardcodear
      valores reales — usar placeholders y marcar con `// TODO:` la carga desde config.

## pom.xml mínimo a generar cuando no exista uno

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>factory-method-project</artifactId>
    <version>1.0.0</version>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
        </plugins>
    </build>
</project>
```

Ajustar la versión de Java (`maven.compiler.source/target`) si el usuario especifica otra.

### 4.1 Interfaz local con botones (GUI de demostración)

Todo proyecto generado incluye una **interfaz gráfica local ejecutable** — una
ventana con botones que permite invocar la factory sin consola ni tests, usando
`javax.swing` (nativo del JDK, cero dependencias). No es parte del patrón GoF, es
una herramienta de verificación manual para quien reciba el proyecto.

Convención de nombre: `<Domain>Gui.java` (ej. `NotificationGui.java`), en el mismo
paquete que el resto del dominio, con un `main(String[] args)` que lanza la ventana.

**Regla clave: la interfaz debe adaptarse al caso, no usar siempre el mismo layout.**
Antes de generarla, analizar cómo se crea cada `ConcreteProduct`:

- **Si el factory method no requiere parámetros de entrada** (o los resuelve
  internamente): un botón por variante del `enum`. Al presionar, se llama a la
  factory y el resultado se muestra en un área de texto/log en la misma ventana.
- **Si el factory method requiere datos del usuario** (ej. destinatario, mensaje,
  cantidad): agregar los `JTextField`/`JComboBox` necesarios arriba de los botones,
  validar que no estén vacíos antes de habilitar el botón, y mostrar el error en la
  misma ventana (nunca un `System.out` silencioso) si falta un dato.
- **Si hay muchas variantes (7+)**: no generar un botón por cada una — usar un
  `JComboBox` para seleccionar el tipo y un solo botón "Crear", para no saturar la
  ventana. Este es el mismo criterio de simplicidad que en la sección 3: elegir el
  layout más simple que cubra el caso, no el más elaborado.
- La ventana nunca contiene lógica de negocio: solo arma el input, llama a la
  factory y muestra el resultado. Toda la lógica real vive en las clases del dominio.
- Debe ejecutarse con `mvn compile exec:java -Dexec.mainClass="com.<company>.<domain>.<Domain>Gui"`
  (agregar `exec-maven-plugin` al `pom.xml` si no está, en vez de pedirle al usuario
  que lo instale manualmente).

Ejemplo mínimo para el caso "sin parámetros, pocas variantes" (adaptar, no copiar
literal — para el caso "con parámetros" o "muchas variantes" seguir las reglas de
arriba, no este layout fijo):

```java
package com.example.notification;

import javax.swing.*;
import java.awt.*;

public final class NotificationGui {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Notification Factory Demo");
        JTextArea log = new JTextArea(10, 30);
        log.setEditable(false);

        JPanel buttons = new JPanel();
        for (NotificationFactory.NotificationType type : NotificationFactory.NotificationType.values()) {
            JButton button = new JButton(type.name());
            // One click = one factory call; the button never builds the Notification itself.
            button.addActionListener(e -> {
                Notification notification = NotificationFactory.create(type);
                notification.send("user@example.com", "Hello from " + type);
                log.append("Created: " + notification.getClass().getSimpleName() + "\n");
            });
            buttons.add(button);
        }

        frame.setLayout(new BorderLayout());
        frame.add(buttons, BorderLayout.NORTH);
        frame.add(new JScrollPane(log), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
```