# Arquitectura Backend y Buenas Prácticas en Java + Spring Boot

> Documento educativo basado en el proyecto Plattio.  
> Objetivo: consolidar criterios de arquitectura y patrones aplicables a cualquier proyecto backend profesional.

---

## Tabla de contenidos

**Sección 1 — Arquitectura por capas**
1. [El problema que resuelve la separación en capas](#1-el-problema-que-resuelve-la-separación-en-capas)
2. [Las capas y su responsabilidad](#2-las-capas-y-su-responsabilidad)
3. [Flujo de una petición de punta a punta](#3-flujo-de-una-petición-de-punta-a-punta)
4. [Principios de diseño que fundamentan la arquitectura](#4-principios-de-diseño-que-fundamentan-la-arquitectura)

**Sección 2 — Patrones y buenas prácticas**
5. [El patrón DTO: no expongas tus entidades](#5-el-patrón-dto-no-expongas-tus-entidades)
6. [El patrón Mapper: separar la conversión](#6-el-patrón-mapper-separar-la-conversión)
7. [Java Records: inmutabilidad sin ceremonias](#7-java-records-inmutabilidad-sin-ceremonias)
8. [Constructor Injection vs Field Injection](#8-constructor-injection-vs-field-injection)
9. [Diseño REST: verbos, códigos y URLs](#9-diseño-rest-verbos-códigos-y-urls)
10. [Excepciones personalizadas y manejo de errores](#10-excepciones-personalizadas-y-manejo-de-errores)
11. [Lógica de dominio en el modelo](#11-lógica-de-dominio-en-el-modelo)
12. [Principios SOLID aplicados al backend](#12-principios-solid-aplicados-al-backend)

---

# Sección 1 — Arquitectura por capas y diseño backend

---

## 1. El problema que resuelve la separación en capas

Imaginá que escribís una función que recibe un HTTP request, consulta la base de datos, aplica lógica de negocio, y devuelve la respuesta. Todo en un solo lugar. Al principio parece conveniente. Pero cuando el sistema crece, ese código se vuelve imposible de cambiar: si cambiás la base de datos, rompés la lógica de negocio; si cambiás la API, tenés que revisar las queries. Cada parte depende de todas las demás.

La arquitectura en capas responde a este problema aplicando un principio simple:

> **Cada parte del sistema debe tener una razón para cambiar, y solo una.**

Cuando separás responsabilidades, una capa puede cambiar sin arrastrar a las demás. Podés migrar de PostgreSQL a MySQL sin tocar los controllers. Podés cambiar el formato JSON de respuesta sin alterar la lógica de negocio. Podés testear la lógica de negocio sin levantar un servidor HTTP.

La separación en capas no es un capricho estético. Es la respuesta práctica a la siguiente pregunta: *¿cuándo va a cambiar esto y por qué razón?*

---

## 2. Las capas y su responsabilidad

### 2.1 Model (Entidad de dominio)

**Qué es:** La representación del objeto de negocio real, mapeada a la base de datos mediante JPA/Hibernate.

**Responsabilidad:** Definir la estructura de datos y encapsular la lógica que pertenece intrínsecamente al objeto (no la lógica de aplicación, sino la de dominio).

**Qué NO debe hacer:**
- Saber cómo se serializa a JSON.
- Saber qué campos exponer a la API.
- Hacer consultas a la base de datos.
- Contener lógica de aplicación (flujos, validaciones de negocio complejas que involucran múltiples entidades).

```java
@Entity
@Table(name = "pedido")
public class Pedido {

    @Column(nullable = false)
    private String estado;

    // La validación de transición de estado PERTENECE al dominio.
    // Esta es lógica del objeto Pedido, no del servicio que lo usa.
    public void iniciarPreparacion() {
        if (!this.estado.equalsIgnoreCase("pendiente")) {
            throw new IllegalStateException("El pedido ya está en preparación o finalizado.");
        }
        this.estado = "en_preparacion";
        this.fechaPreparacion = LocalDateTime.now();
    }
}
```

**Principio de fondo:** el modelo es el corazón del sistema. Si la lógica que describe el comportamiento de un `Pedido` vive dispersa en servicios, el modelo se convierte en un mero contenedor de datos (antipatrón conocido como *Anemic Domain Model*). Mantener lógica de dominio en el modelo hace que el objeto se defienda solo: no importa desde dónde se llame, las reglas de negocio siempre se aplican.

---

### 2.2 Repository

**Qué es:** Interfaz que extiende `JpaRepository` y define cómo consultar la base de datos. Spring Data JPA genera la implementación en tiempo de ejecución.

**Responsabilidad:** Persistencia pura. Solo lee y escribe en la base de datos.

```java
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByEstadoInAndSesion_Mozo_Id(List<String> estados, Long mozoId);
}
```

Los nombres de los métodos son un lenguaje de consulta: Spring los parsea y genera el SQL correspondiente. No hay que escribir SQL manual para la mayoría de los casos.

**Por qué se separa del DAO:** el Repository es el contrato con la tecnología de persistencia. Si en el futuro cambias de JPA a jOOQ, Mongo, o cualquier otra cosa, solo cambiás esta capa.

---

### 2.3 DAO (Data Access Object)

**Qué es:** Una clase que actúa como intermediario entre el Service y el Repository. Encapsula las operaciones de acceso a datos y expone métodos con nombres semánticamente significativos para el negocio.

**Responsabilidad:** Traducir los nombres de negocio a llamadas del Repository.

```java
@Repository
public class PedidoDAO {

    private final PedidoRepository pedidoRepository;

    public PedidoDAO(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> buscarPendientesMozo(List<String> estados, Long mozoId) {
        return pedidoRepository.findByEstadoInAndSesion_Mozo_Id(estados, mozoId);
    }
}
```

**¿Por qué existe el DAO si ya existe el Repository?**

El Repository usa nombres derivados de la tecnología (`findByEstadoInAndSesion_Mozo_Id`). El DAO expone `buscarPendientesMozo`. El Service habla con términos de negocio, no con términos de JPA.

Además, el DAO es el lugar correcto para agrupar operaciones relacionadas, manejar lógica de consulta compleja, y para que el Service no dependa directamente de JPA. Esto hace el código más legible y el testing más fácil.

> **Analogía:** el Repository es el SQL que habla con la base de datos. El DAO es el asistente que traduce "dame los pedidos pendientes del mozo 5" a la query correcta.

---

### 2.4 Service

**Qué es:** La capa de lógica de aplicación. Coordina los objetos de dominio para llevar a cabo casos de uso.

**Responsabilidad:** Orquestar el flujo de un caso de uso completo: validar entradas, buscar entidades, aplicar lógica de negocio, persistir, y devolver el resultado.

```java
@Service
public class PedidoService {

    public void crearPedido(CrearPedidoRequest request) {
        // 1. Validar que la sesión existe
        SesionMesa sesion = sesionMesaDAO.buscarPorId(request.sesionId())
                .orElseThrow(() -> new PedidoException("Sesión no encontrada", HttpStatus.NOT_FOUND));
        // 2. Crear la entidad (lógica de construcción en el dominio)
        // 3. Persistir
        pedidoDAO.guardar(new Pedido(sesion, request.categoria()));
    }
}
```

**Qué NO debe hacer el Service:**
- Saber nada de HTTP (no importa `HttpServletRequest`, `@GetMapping`, ni `ResponseEntity`).
- Construir la respuesta JSON.
- Formatear datos para la presentación.
- Declarar `throws` para excepciones unchecked.

El Service es completamente agnóstico de la capa de presentación. Podés reutilizar el mismo Service desde un Controller HTTP, desde una tarea programada con `@Scheduled`, desde un consumer de mensajería, o desde un test.

---

### 2.5 Controller

**Qué es:** El punto de entrada HTTP. Recibe requests, delega al Service, y devuelve la respuesta.

**Responsabilidad:** Muy acotada: mapear HTTP → Service → HTTP. Nada más.

```java
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Void> crearPedido(@RequestBody CrearPedidoRequest request) {
        pedidoService.crearPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
```

**Qué NO debe hacer el Controller:**
- Lógica de negocio.
- Acceso a datos.
- Transformar entidades a mano.

Un Controller que tiene más de 20-30 líneas en un método probablemente está asumiendo responsabilidades que pertenecen a otras capas.

---

### 2.6 View / DTO

**Qué es:** Un objeto que define exactamente qué datos se envían al cliente. Completamente desacoplado de la entidad.

**Por qué es necesario:** explicado en profundidad en la [Sección 2](#5-el-patrón-dto-no-expongas-tus-entidades).

---

### 2.7 Mapper

**Qué es:** Una clase utilitaria que convierte entidades a Views/DTOs.

**Por qué es necesario:** explicado en profundidad en la [Sección 2](#6-el-patrón-mapper-separar-la-conversión).

---

### 2.8 Exception

**Qué es:** Clases de excepción personalizadas con semántica de negocio, manejadas globalmente por un `@ControllerAdvice`.

**Por qué es necesario:** explicado en profundidad en la [Sección 2](#10-excepciones-personalizadas-y-manejo-de-errores).

---

## 3. Flujo de una petición de punta a punta

Para hacer tangible la arquitectura, recorramos qué pasa cuando el frontend llama a `POST /pedidos`:

```
Cliente HTTP
    │
    ▼
[Controller]  →  recibe el JSON, lo deserializa a CrearPedidoRequest
    │
    ▼
[Service]     →  valida, busca SesionMesa, construye Pedido, persiste
    │
    ├──▶ [DAO]  →  llama al Repository con nombres de negocio
    │      │
    │      ▼
    │   [Repository]  →  ejecuta la query JPA → Base de datos
    │
    ▼
[Controller]  →  recibe void, responde 201 Created
    │
    ▼
Cliente HTTP
```

Cada flecha es un cruce de frontera entre capas. En cada frontera hay un contrato claro:
- Controller → Service: objetos de request (DTOs).
- Service → DAO: entidades de dominio o parámetros simples.
- DAO → Repository: parámetros simples (IDs, strings).
- Service → Controller: entidades de dominio (el Controller las mapea a Views).

---

## 4. Principios de diseño que fundamentan la arquitectura

### 4.1 Separación de responsabilidades (SRP)

Cada clase tiene **una sola razón para cambiar**. Si `PedidoService` cambia, es porque cambió la lógica de negocio de pedidos. Si `PedidoController` cambia, es porque cambió la API HTTP. Nunca ambas cosas en la misma clase.

### 4.2 Bajo acoplamiento

Las capas conocen la capa inmediatamente inferior, nunca saltan capas. El Controller no llama al DAO directamente. El Service no sabe que existe HTTP. Así, cada capa puede evolucionar independientemente.

### 4.3 Alta cohesión

Todo lo relacionado con pedidos vive junto: `PedidoController`, `PedidoService`, `PedidoDAO`, `PedidoRepository`, `Pedido`, `PedidoView`, `PedidoMapper`. Si mañana hay que entender cómo funciona el módulo de pedidos, sabés exactamente dónde buscar.

### 4.4 Inversión de dependencias

Las capas altas (Controller, Service) no dependen de implementaciones concretas, sino de abstracciones. El Service no conoce cómo funciona JPA internamente; habla con el DAO. Esto hace posible el testing: podés reemplazar el DAO por un mock y testear el Service de forma aislada.

---

# Sección 2 — Patrones y buenas prácticas aplicadas

---

## 5. El patrón DTO: no expongas tus entidades

### El problema

Una entidad JPA es un objeto que vive en el contexto de la base de datos. Exponer esa entidad directamente en la API HTTP genera una serie de problemas que, al principio, no se notan, pero que crecen con el sistema.

**Problema 1 — Exposición de datos internos:**
Tu entidad `Empleado` tiene `passwordHash`. Si devolvés la entidad directamente, ese campo aparece en el JSON. Podés parcharlo con `@JsonIgnore`, pero estás usando una anotación de serialización HTTP dentro de un objeto de base de datos: dos mundos que no deberían conocerse.

**Problema 2 — Acoplamiento entre API y base de datos:**
Si renombrás una columna en la base de datos, cambia el JSON que ve el cliente. Si el cliente espera `"numeroMesa"` y vos renombrás el campo a `"nroMesa"`, rompés la API sin querer.

**Problema 3 — Relaciones JPA que se serializan en cascada:**
Una entidad `Pedido` tiene una relación `@ManyToOne` con `SesionMesa`, que tiene `@ManyToOne` con `Mesa`, que tiene `@OneToMany` con todas las sesiones... Si Jackson intenta serializar esto, entra en un ciclo infinito. Por eso aparecen los `@JsonIgnore`, que son parches sobre el síntoma, no la solución al problema.

**Problema 4 — Vistas diferentes del mismo dato:**
El frontend de cocina necesita ver los pedidos con los ítems. El frontend de administración necesita ver pedidos con el nombre del mozo. La misma entidad no puede satisfacer ambas vistas sin condicionales o campos innecesarios.

### La solución: DTO (Data Transfer Object)

Un DTO es un objeto que define exactamente qué datos se transfieren entre capas o entre sistemas. No tiene lógica de negocio. No está anotado con `@Entity`. No tiene ciclos de referencia. Es solo un contrato de datos.

```java
// Entidad: lo que vive en la base de datos
@Entity
public class Pedido {
    private Long id;
    private SesionMesa sesion;   // relación JPA
    private List<ItemPedido> items; // relación JPA
    private LocalDateTime fechaInicio;
    private String estado;
    // ...
}

// DTO: lo que ve el cliente
public record PedidoView(
    Long id,
    String fechaInicio,    // formateado como String, no como LocalDateTime
    String estado,
    Integer numMesa,       // aplanado desde sesion.mesa.numero
    List<ItemPedidoView> items  // no ItemPedido, sino su vista
) {}
```

Observá las diferencias:
- `LocalDateTime` → `String` formateado (la entidad no sabe el formato que quiere el cliente).
- `SesionMesa sesion` → `Integer numMesa` (el cliente no necesita el objeto entero).
- `List<ItemPedido>` → `List<ItemPedidoView>` (cada ítem también tiene su propia vista).

### Dos tipos de DTOs

**View (o Response DTO):** Lo que se envía al cliente. Solo lectura. Representa datos ya procesados.

**Request DTO:** Lo que llega del cliente. Contiene solo los campos necesarios para ejecutar una operación. No es una entidad, es una intención.

```java
// El cliente no manda un Pedido completo.
// Manda solo lo necesario para crear uno.
public record CrearPedidoRequest(Long sesionId, String categoria) {}
```

Esto es importante: nunca aceptes tu entidad directamente como `@RequestBody`. Si aceptás un `Pedido` completo desde el cliente, el cliente podría enviar `id`, `estado`, `fechaInicio`, o cualquier campo que no debería modificar. El Request DTO actúa como lista de lo que el cliente *tiene permitido* enviar.

---

## 6. El patrón Mapper: separar la conversión

### El problema sin Mapper

La conversión de entidad a DTO tiene que vivir en algún lado. Hay tres lugares posibles:

**Opción A — `toView()` en la entidad:**
```java
// ❌ Problema: la entidad ahora importa la View, que importa el Mapper, etc.
// La entidad tiene dependencias hacia arriba de la arquitectura.
public PedidoView toView() {
    return new PedidoView(this.id, this.estado, ...);
}
```

**Opción B — Conversión en el Controller:**
```java
// ❌ Problema: el Controller ahora hace trabajo de transformación de datos.
// Si cambia la View, hay que cambiar el Controller.
PedidoView view = new PedidoView(pedido.getId(), pedido.getEstado(), ...);
```

**Opción C — Clase Mapper dedicada:**
```java
// ✅ La conversión tiene un lugar propio y claro.
public class PedidoMapper {
    private PedidoMapper() {} // no instanciable, solo métodos estáticos
    
    public static PedidoView toView(Pedido pedido) {
        return new PedidoView(
            pedido.getId(),
            formatear(pedido.getFechaInicio()),
            pedido.getEstado(),
            pedido.getSesion().getMesa().getNumero(),
            pedido.getItems().stream().map(ItemPedidoMapper::toView).toList()
        );
    }
}
```

### Por qué la Opción C es la correcta

**La entidad no debe conocer su representación HTTP.** Una entidad es un objeto de dominio. Sabe de sí misma y de sus reglas de negocio. No sabe que existe una API REST, ni que hay un frontend que necesita fechas en formato `"dd/MM/yyyy"`. Si ponés `toView()` en la entidad, la entidad empieza a depender de clases de la capa de presentación, violando el principio de bajo acoplamiento.

**El Mapper centraliza la conversión.** Si cambia el formato de fecha que quiere el frontend, cambiás una línea en `PedidoMapper`, no en la entidad ni en el controller.

**El Mapper puede manejar lógica de presentación compleja sin ensuciar otras capas:**
```java
public static PedidoView toView(Pedido pedido) {
    // Lógica de presentación: el numero de mesa viene de una cadena de asociaciones
    int numMesa = pedido.getSesion().getMesa().getNumero();
    // Lógica de presentación: fechas formateadas
    String fechaInicio = formatear(pedido.getFechaInicio());
    // Lógica de presentación: lista de vistas anidadas
    List<ItemPedidoView> items = pedido.getItems().stream()
        .map(ItemPedidoMapper::toView)
        .toList();
    return new PedidoView(pedido.getId(), fechaInicio, pedido.getEstado(), numMesa, items);
}
```

### Uso en el Controller

```java
// En lugar de:
views = pedidos.stream().map(p -> new PedidoView(p.getId(), ...)).toList();

// El Controller queda limpio:
views = pedidos.stream().map(PedidoMapper::toView).toList();
```

La referencia a método `PedidoMapper::toView` es equivalente a `p -> PedidoMapper.toView(p)`. Esta sintaxis indica claramente que hay una clase responsable de esa conversión.

---

## 7. Java Records: inmutabilidad sin ceremonias

### Qué son

Introducidos en Java 14 (estables en Java 16), los Records son una forma concisa de definir clases de datos inmutables. Con una sola línea, Java genera automáticamente:
- Constructor con todos los parámetros.
- Getters para cada campo (con el mismo nombre del campo, sin prefijo `get`).
- `equals()`, `hashCode()`, y `toString()` correctos.

```java
// Antes de Records: ~40 líneas
public class PlatoView {
    private final Long id;
    private final String nombre;
    private final BigDecimal precio;
    
    public PlatoView(Long id, String nombre, BigDecimal precio) { ... }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    // + equals, hashCode, toString
}

// Con Records: 1 línea
public record PlatoView(Long id, String nombre, BigDecimal precio) {}
```

### Por qué son ideales para DTOs

Los DTOs tienen una característica fundamental: **son inmutables**. Una vez que se crea un `PlatoView`, no tiene sentido modificar sus campos. El DTO es una foto de los datos en un momento dado.

Los Records refuerzan esa inmutabilidad a nivel de lenguaje: no tienen setters. Si alguien intenta modificar los datos, el compilador lo impide. Esto elimina una clase entera de bugs.

Adicionalmente, la inmutabilidad tiene beneficios en entornos concurrentes: los Records son inherentemente thread-safe porque no tienen estado mutable.

### Cuándo usar Records y cuándo no

**Usar Records para:**
- Views / Response DTOs: objetos de solo lectura que se envían al cliente.
- Request DTOs: datos que llegan del cliente (inmutables una vez deserializados).
- Objetos de valor pequeños: coordenadas, rangos de fechas, pares clave-valor.

**No usar Records para:**
- Entidades JPA (`@Entity`): Hibernate necesita un constructor sin argumentos y la capacidad de modificar campos para funcionar.
- Objetos con estado mutable que cambian durante su ciclo de vida.
- Cuando necesitás herencia (Records no pueden extender otras clases).

### Acceso a campos

En un Record, los getters no tienen prefijo `get`:
```java
PlatoView view = new PlatoView(1L, "Milanesa", new BigDecimal("1500"));

// En una clase normal: view.getNombre()
// En un Record:
view.nombre(); // → "Milanesa"
view.precio(); // → 1500
```

Jackson (el serializador JSON de Spring) serializa Records correctamente, generando las keys con el nombre del campo. No hay configuración adicional necesaria.

---

## 8. Constructor Injection vs Field Injection

### Dos formas de inyectar dependencias

Spring puede inyectar dependencias de tres formas: por constructor, por setter, o por campo (field). La más común que verás en código legacy es la inyección por campo:

```java
// ❌ Field Injection
@Service
public class PedidoService {
    
    @Autowired
    private PedidoDAO pedidoDAO;
    
    @Autowired
    private SesionMesaDAO sesionMesaDAO;
}
```

```java
// ✅ Constructor Injection
@Service
public class PedidoService {

    private final PedidoDAO pedidoDAO;
    private final SesionMesaDAO sesionMesaDAO;

    public PedidoService(PedidoDAO pedidoDAO, SesionMesaDAO sesionMesaDAO) {
        this.pedidoDAO = pedidoDAO;
        this.sesionMesaDAO = sesionMesaDAO;
    }
}
```

### Por qué Constructor Injection es superior

**1. Los campos son `final`**

Con field injection, los campos no pueden declararse `final` porque Spring los asigna después de crear el objeto. Con constructor injection, los campos se asignan en el constructor y pueden ser `final`. Un campo `final` es una garantía: una vez asignado, nunca es `null` ni cambia.

**2. Detectás dependencias circulares en tiempo de inicio**

Si la clase A inyecta B y B inyecta A, hay una dependencia circular. Con field injection, Spring intenta resolverla en tiempo de ejecución y puede que falle tarde o de formas inesperadas. Con constructor injection, Spring detecta el ciclo en el arranque de la aplicación y lanza una excepción clara.

**3. El testing es directo**

Con field injection, para testear `PedidoService` necesitás Spring o un framework de mocks que use reflection para inyectar los campos privados. Con constructor injection, simplemente llamás al constructor:

```java
// Test sin Spring, sin mocks mágicos:
PedidoDAO mockDAO = new PedidoDAOMock();
PedidoService service = new PedidoService(mockDAO, sesionMesaDAO);
```

**4. Las dependencias son visibles**

El constructor documenta qué necesita la clase para funcionar. Si un constructor tiene 8 parámetros, es una señal obvia de que la clase tiene demasiadas responsabilidades. Con field injection, esa señal queda oculta.

**5. Inmutabilidad de la instancia**

Con constructor injection y campos `final`, el objeto no puede cambiar sus dependencias después de ser creado. La instancia es completamente predecible.

### La anotación `@Autowired` en el constructor

Cuando una clase tiene un único constructor, Spring la detecta automáticamente y no necesitás la anotación `@Autowired`. Si hay más de un constructor, la anotación sirve para indicar cuál debe usar Spring.

```java
// @Autowired no es necesario si hay un solo constructor
public PedidoService(PedidoDAO pedidoDAO, SesionMesaDAO sesionMesaDAO) {
    this.pedidoDAO = pedidoDAO;
    this.sesionMesaDAO = sesionMesaDAO;
}
```

---

## 9. Diseño REST: verbos, códigos y URLs

### Qué es REST

REST (Representational State Transfer) es un estilo de arquitectura para APIs HTTP. No es un protocolo ni un estándar rígido, sino un conjunto de convenciones que, cuando se siguen, hacen las APIs predecibles y fáciles de usar.

La idea central: **los recursos son sustantivos, los verbos HTTP son las acciones**.

### Los verbos HTTP y cuándo usarlos

| Verbo | Semántica | Idempotente | Tiene body |
|-------|-----------|-------------|------------|
| GET | Obtener un recurso o lista | Sí | No |
| POST | Crear un nuevo recurso | No | Sí |
| PUT | Reemplazar un recurso completo | Sí | Sí |
| PATCH | Modificar parcialmente un recurso | Depende | Sí |
| DELETE | Eliminar un recurso | Sí | Raramente |

**Idempotente** significa que llamar a la operación una o diez veces produce el mismo resultado. Un `DELETE /pedidos/5` elimina el pedido; si lo llamás de nuevo, el pedido ya no existe pero el estado del servidor es el mismo. Un `POST /pedidos` crea un nuevo pedido cada vez que se llama: no es idempotente.

### Cuándo usar PATCH vs PUT

**PUT** reemplaza el recurso completo. Si mandás un `PUT /platos/1` con `{ "nombre": "Milanesa" }`, el servidor reemplaza todos los campos del plato con lo que mandaste. Si omitís `precio`, el precio queda vacío.

**PATCH** modifica campos específicos. Es la acción correcta para:
- Cambiar un estado: `PATCH /pedidos/5/estado` con `{ "estado": "en_preparacion" }`.
- Cambiar un campo puntual: `PATCH /platos/1/precio` con `{ "precio": 1500 }`.
- Cualquier actualización parcial donde no tiene sentido enviar el recurso completo.

```java
// ✅ PUT para actualización completa del plato
@PutMapping("/{id}")
public ResponseEntity<Void> actualizarPlato(@PathVariable Long id,
                                             @RequestBody ActualizarPlatoRequest request) { ... }

// ✅ PATCH para cambiar solo el precio
@PatchMapping("/{id}/precio")
public ResponseEntity<Void> cambiarPrecio(@PathVariable Long id,
                                           @RequestBody CambiarPrecioRequest request) { ... }

// ✅ PATCH para cambiar solo el estado activo/inactivo
@PatchMapping("/{id}/activo")
public ResponseEntity<Void> toggleActivo(@PathVariable Long id) { ... }
```

### Anti-patrones comunes en URLs

```
❌ POST /pedidos/crear          → el verbo ya dice "crear"
❌ GET  /getPedidos             → el verbo ya dice "get"
❌ POST /pedidos/5/cambiarEstado/en_preparacion  → el estado debería ir en el body

✅ POST   /pedidos              → crear pedido
✅ GET    /pedidos              → listar pedidos
✅ GET    /pedidos/5            → obtener pedido 5
✅ PATCH  /pedidos/5/estado     → cambiar estado (payload en el body)
✅ DELETE /pedidos/5            → eliminar pedido 5
```

**Los datos de creación van en el body, no en la URL:**

```
❌ POST /notificaciones/{mensaje}/{tipo}/{mozoId}/{sesionId}
✅ POST /notificaciones  →  body: { "mensaje": "...", "tipo": "...", "mozoId": 3, "sesionId": 7 }
```

Las URLs son para identificar recursos, no para transportar datos. Una URL con datos variables en el path es frágil: el mensaje podría tener caracteres especiales, longitud variable, o necesitar encoding.

### Códigos de estado HTTP

```
200 OK          → operación exitosa que devuelve datos
201 Created     → recurso creado exitosamente (POST)
204 No Content  → operación exitosa sin datos que devolver
400 Bad Request → error del cliente (validación, formato inválido)
401 Unauthorized → no autenticado
403 Forbidden   → autenticado pero sin permiso
404 Not Found   → recurso no encontrado
409 Conflict    → conflicto con el estado actual (ej: duplicado)
500 Internal Server Error → error no controlado del servidor
```

**Una lista vacía no es 404:**

```java
// ❌ Incorrecto: una lista vacía no es "no encontrado"
if (pedidos.isEmpty()) {
    throw new PedidoException("No hay pedidos", HttpStatus.NOT_FOUND);
}

// ✅ Correcto: devolvé la lista vacía con 200
return ResponseEntity.ok(pedidos); // devuelve []
```

Un `404` significa que el *recurso* no existe (la URL es inválida). Una lista vacía es una respuesta válida: el recurso "lista de pedidos" existe, simplemente no tiene elementos.

### ResponseEntity vs retorno directo

```java
// Ambas formas son válidas:
return ResponseEntity.ok(view);
return ResponseEntity.status(HttpStatus.CREATED).build(); // sin body

// Para respuestas simples sin body, también:
return ResponseEntity.noContent().build(); // 204
```

`ResponseEntity<Void>` es la forma correcta cuando la operación no devuelve datos (crear, actualizar, eliminar). Usá `ResponseEntity<T>` cuando devolvés un objeto.

---

## 10. Excepciones personalizadas y manejo de errores

### El problema del manejo de errores disperso

Sin una estrategia centralizada, el manejo de errores termina disperso: cada controller tiene sus propios `try/catch`, los mensajes de error son inconsistentes, y el código de lógica de negocio queda sepultado bajo bloques de manejo de errores.

### Excepciones personalizadas con semántica de negocio

```java
public class PedidoException extends RuntimeException {

    private final HttpStatus status;

    public PedidoException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
```

Dos decisiones clave:

**1. Extiende `RuntimeException`, no `Exception`:**

`Exception` es una excepción *checked*: el compilador obliga a capturarla o declararla en `throws`. Esto ensucia las firmas de métodos y fuerza código defensivo en capas que no deben saber de errores HTTP:

```java
// ❌ Firma sucia: el controller ahora sabe que puede lanzar PedidoException
public ResponseEntity<PedidoView> obtenerPedido(@PathVariable Long id) throws PedidoException { ... }

// ✅ Firma limpia: la excepción se maneja centralmente
public ResponseEntity<PedidoView> obtenerPedido(@PathVariable Long id) { ... }
```

`RuntimeException` es *unchecked*: puede propagarse libremente por las capas hasta ser capturada en un único punto central.

**2. Lleva el código HTTP:**

La excepción sabe qué código HTTP corresponde a su situación. No toda excepción es un 500. Un recurso no encontrado es un 404. Una validación fallida es un 400. Codificar esto en la excepción permite que el manejador central lo use sin lógica extra.

### El manejador global: `@ControllerAdvice`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoException(PedidoException ex) {
        return ResponseEntity
            .status(ex.getStatus())
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Error interno del servidor"));
    }
}
```

`@ControllerAdvice` es un interceptor global: Spring lo llama automáticamente cuando cualquier método anotado con `@ExceptionHandler` coincide con la excepción lanzada.

**Beneficios:**
- Los controllers no tienen `try/catch`.
- El formato de respuesta de error es consistente en toda la API.
- Para agregar manejo de una nueva excepción, agregás un método al `@ControllerAdvice`, sin tocar el código que la lanza.
- Los mensajes de error pueden ser descriptivos para el cliente sin exponer detalles internos del servidor.

### Excepciones de dominio vs excepciones de aplicación

Las **excepciones de dominio** vienen del modelo y no saben de HTTP:
```java
// En Pedido.java — excepción de dominio pura
public void iniciarPreparacion() {
    if (!this.estado.equalsIgnoreCase("pendiente")) {
        throw new IllegalStateException("El pedido ya no está pendiente.");
    }
}
```

Las **excepciones de aplicación** (como `PedidoException`) son específicas del sistema y llevan información HTTP. El `@ControllerAdvice` también puede capturar `IllegalStateException` y convertirlo en un 409 Conflict, si ese mapeo tiene sentido para tu API.

---

## 11. Lógica de dominio en el modelo

### El Anemic Domain Model: el antipatrón más común

Un modelo anémico es una entidad que solo tiene getters y setters: ninguna lógica propia. Toda la lógica de negocio termina en el Service, que manipula el objeto directamente con setters.

```java
// ❌ Modelo anémico
public class Pedido {
    private String estado;
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    // nada más
}

// ❌ Toda la lógica en el Service
public void iniciarPreparacion(Long id) {
    Pedido pedido = pedidoDAO.buscarPorId(id).orElseThrow(...);
    if (!pedido.getEstado().equals("pendiente")) { // validación en el Service
        throw new PedidoException("...", HttpStatus.BAD_REQUEST);
    }
    pedido.setEstado("en_preparacion"); // manipulación directa desde el Service
    pedido.setFechaPreparacion(LocalDateTime.now());
    pedidoDAO.guardar(pedido);
}
```

El problema: si en otro lugar del sistema alguien llama a `pedido.setEstado("en_preparacion")` directamente, sin pasar por el Service, la validación se saltea. La regla de negocio no está protegida.

### El Rich Domain Model: la alternativa correcta

```java
// ✅ El modelo se defiende solo
public class Pedido {
    private String estado;

    public void iniciarPreparacion() {
        if (!this.estado.equalsIgnoreCase("pendiente")) {
            throw new IllegalStateException("El pedido ya no está pendiente.");
        }
        this.estado = "en_preparacion";
        this.fechaPreparacion = LocalDateTime.now();
    }
}

// ✅ El Service solo orquesta
public void iniciarPreparacion(Long id) {
    Pedido pedido = pedidoDAO.buscarPorId(id).orElseThrow(...);
    pedido.iniciarPreparacion(); // la validación está en el dominio
    pedidoDAO.guardar(pedido);
}
```

**Principio:** si una regla describe *qué puede hacer un objeto*, pertenece al objeto. Si describe *cuándo y cómo el sistema debe hacer algo*, pertenece al Service.

| Pertenece al Modelo | Pertenece al Service |
|---------------------|----------------------|
| Un pedido no puede pasar a "en_preparacion" si no está "pendiente" | Notificar al mozo cuando el pedido está listo |
| Un ítem no puede ser entregado si no está listo | Validar que la sesión existe antes de crear un pedido |
| Una notificación no puede completarse dos veces | Calcular y persistir el total al cerrar la sesión |

---

## 12. Principios SOLID aplicados al backend

### S — Single Responsibility Principle (SRP)

Cada clase tiene una sola razón para cambiar.

- **Aplicación en capas:** `PedidoController` solo cambia si cambia la API. `PedidoService` solo cambia si cambia la lógica de negocio. `PedidoDAO` solo cambia si cambia cómo accedemos a los datos.
- **Señal de violación:** un controller que tiene lógica de base de datos, o un service que formatea JSON.

### O — Open/Closed Principle (OCP)

El software debe estar abierto a la extensión y cerrado a la modificación.

- **Aplicación práctica:** el `@ControllerAdvice` está abierto a la extensión (agregás métodos para nuevas excepciones) pero no necesitás modificar el código existente para agregar manejo de una nueva excepción.
- También aplica a los Mappers: para agregar un nuevo campo a la View, modificás `NotificacionMapper.toView()` en un solo lugar, sin tocar el Controller ni la entidad.

### L — Liskov Substitution Principle (LSP)

Los subtipos deben poder usarse en lugar de sus tipos base sin alterar el comportamiento correcto del programa.

- **Aplicación práctica:** todas las excepciones personalizadas (`PedidoException`, `MesaException`, etc.) extienden `RuntimeException`. El `@ControllerAdvice` puede manejar cualquiera con un handler de `RuntimeException` genérico si es necesario. Son intercambiables como excepciones unchecked.

### I — Interface Segregation Principle (ISP)

Los clientes no deberían depender de interfaces que no usan.

- **Aplicación práctica:** `CrearPedidoRequest` solo tiene `sesionId` y `categoria`. El Service no recibe un objeto `Pedido` completo con todos sus campos. El cliente declara exactamente lo que necesita para la operación puntual.
- También aplica a los Request DTOs: `CambiarPrecioRequest` tiene solo `precio`. `ActualizarPlatoRequest` tiene todos los campos del plato. Son interfaces separadas para operaciones separadas.

### D — Dependency Inversion Principle (DIP)

Los módulos de alto nivel no deben depender de módulos de bajo nivel. Ambos deben depender de abstracciones.

- **Aplicación práctica:** `PedidoService` no crea instancias de `PedidoDAO`. Recibe el DAO como dependencia (inversión de control). Spring gestiona las instancias; el Service solo declara qué necesita.
- Resultado: podés reemplazar `PedidoDAO` por una implementación alternativa sin tocar `PedidoService`.

---

### DRY: Don't Repeat Yourself

Cada pieza de conocimiento debe tener una representación única en el sistema.

**Aplicación en Mappers:** la lógica de conversión de `Pedido` a `PedidoView` vive en un solo lugar (`PedidoMapper`). Si cambia el formato de fecha, lo cambiás en el Mapper, y automáticamente aplica en todos los endpoints que devuelven pedidos.

**Aplicación en validaciones:** la regla "el precio debe ser mayor a cero" vive en un solo lugar del Service. No se repite en cada endpoint que recibe un precio.

**Señal de violación:** cuando cambiás una regla de negocio y tenés que buscar y actualizar ese cambio en tres o cuatro lugares diferentes.

---

### Guía rápida de decisiones

Al enfrentar una decisión de diseño, estas preguntas ayudan a encuadrar dónde va cada cosa:

**"¿Cuándo puede cambiar esto y por qué razón?"**
→ Si la respuesta son dos razones distintas, la lógica pertenece a dos clases distintas (SRP).

**"¿Esta regla describe el comportamiento del objeto o el flujo del sistema?"**
→ Si describe el comportamiento del objeto, va en el modelo. Si describe el flujo, va en el Service.

**"¿El cliente necesita ver este dato?"**
→ Si no, no va en el DTO. Si sí, asegurate de que esté en el DTO y no en la entidad cruda.

**"¿Este código depende de la tecnología subyacente (JPA, HTTP, etc.)?"**
→ Si sí, debería estar en la capa correspondiente (Repository para JPA, Controller para HTTP), no filtrarse hacia otras capas.

**"Si cambio la base de datos, ¿qué código tengo que tocar?"**
→ Solo debería ser Repository y DAO. Si la respuesta incluye Service o Controller, el acoplamiento está mal.

---

*Este documento refleja el estado de conocimiento y las decisiones tomadas en el proyecto Plattio. Los principios aquí descritos son transferibles a cualquier proyecto backend con arquitectura en capas.*
