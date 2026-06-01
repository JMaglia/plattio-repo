# Backend

## Stack
- Lenguaje: Java 21
- Framework: Spring Boot 3.x
- Base de datos: PostgreSQL
- ORM: JPA / Hibernate
- Build Tool: Maven

## Comandos

Desarrollo
```bash
mvn spring-boot:run
```

Tests
```bash
mvn test
```

Build
```bash
mvn clean package
```

## Arquitectura

La aplicación sigue arquitectura multicapa:

- Controller → endpoints REST
- Service → lógica de negocio
- DAO → acceso intermedio a datos
- Repository → persistencia JPA
- Model → entidades del dominio
- View/DTO → respuestas al frontend
- Exception → excepciones personalizadas

## Convenciones

### Java
- Clases en PascalCase
- Métodos y atributos en camelCase
- Constructor Injection antes que Field Injection
- Evitar lógica de negocio en Controllers
- Repositories únicamente para persistencia
- Services responsables de las reglas de negocio
- DAOs actúan como capa de acceso a datos
- Las conversiones a View se realizan mediante métodos toView() en las entidades cuando corresponda

### API REST
- Endpoints RESTful
- Utilizar ResponseEntity cuando sea necesario
- Validar entradas antes de ejecutar lógica
- Manejar errores mediante excepciones personalizadas y ControllerAdvice

## Base de datos

### Entidades principales
- Mesa
- SesionMesa
- Pedido
- ItemPedido
- Plato
- Empleado
- Notificacion

### Reglas importantes
- No modificar el esquema de la base de datos sin autorización
- No eliminar relaciones existentes sin analizar impacto
- Mantener compatibilidad con datos ya cargados

## Reglas específicas del proyecto
- El DAO interactúa únicamente con Repository
- Evitar try/catch innecesarios en DAO
- Las excepciones deben gestionarse en capas superiores
- Mantener coherencia con la arquitectura ya implementada
- Explicar siempre el motivo técnico detrás de una propuesta

## IMPORTANTE

Antes de generar código:

1. Revisar si ya existe una implementación similar
2. Mantener el estilo actual del proyecto
3. Evitar duplicación de lógica
4. Priorizar refactorización antes que crear código nuevo paralelo
5. Explicar ventajas y desventajas cuando existan múltiples soluciones