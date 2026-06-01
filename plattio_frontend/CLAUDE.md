# Frontend

## Stack
- Framework: React
- Lenguaje: JavaScript
- Routing: React Router
- Estilos: CSS Modules + CSS tradicional
- Gestor de paquetes: npm

## Comandos

Desarrollo
```bash
npm start
```

Build
```bash
npm run build
```

## Estructura de carpetas
- src/components/ → componentes reutilizables
- src/pages/ → pantallas principales
- src/services/ → llamadas al backend
- src/assets/ → imágenes e íconos
- src/context/ → contexto global
- src/styles/ → estilos compartidos

## Convenciones
- Componentes en PascalCase
- Un componente por archivo
- Servicios en camelCase
- Mantener separación entre UI y llamadas API
- Evitar lógica de negocio compleja dentro de componentes
- Reutilizar componentes existentes antes de crear nuevos
- Priorizar hooks funcionales sobre componentes de clase

## Conexión con backend
- API REST desarrollada en Spring Boot
- Consumir endpoints desde services/
- Evitar URLs hardcodeadas fuera de la configuración
- Mantener consistencia con los DTOs/View Objects enviados por el backend

## Componentes importantes

### Cocina
- CocinaBoard
- PedidoColumn
- PedidoCard
- OrdenamientoBar

### Mozos
- MesaView
- ResumenPedido
- Gestión de pedidos por estado

## Consideraciones especiales
- Existen temporizadores activos en pedidos
- El ordenamiento de pedidos pendientes se maneja con botones
- Hay modales reutilizables para distintas operaciones
- Mantener consistencia visual con el diseño existente