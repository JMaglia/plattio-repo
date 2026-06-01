# Plattio

Sistema integral de gestión para restaurantes desarrollado como proyecto académico universitario.
La aplicación digitaliza el flujo operativo de un restaurante permitiendo gestionar mesas, sesiones de mesa, pedidos, cocina, mozos, menú, empleados y notificaciones, con el objetivo de reducir errores operativos, mejorar la comunicación interna y optimizar los tiempos de atención.

## Estructura
- Frontend: /frontend
- Backend: /backend

## Cómo levantar el proyecto completo

Terminal 1 — Backend
```bash
mvn spring-boot:run
```

Terminal 2 — Frontend
```bash
npm install
npm start
```

## Contexto académico
- Proyecto: Plattio
- Tipo: Trabajo Integrador Final
- Metodología: Scrum
- Objetivo: Desarrollar un sistema integral para optimizar la operación de restaurantes.

## Requerimientos principales
- Gestión de mesas
- Gestión de sesiones de mesa
- Gestión de pedidos
- Gestión de platos y carta
- Gestión de empleados
- Vista de cocina
- Vista de mozo
- Notificaciones entre sectores
- Seguimiento de estados de pedidos

## Reglas generales
- Nunca commitear archivos .env ni credenciales
- No modificar configuraciones sensibles sin autorización
- Commits descriptivos en español
- Una rama por funcionalidad: feature/nombre-feature
- Antes de instalar dependencias nuevas, solicitar aprobación
- Mantener consistencia con la arquitectura actual
- Priorizar soluciones simples y mantenibles
- Explicar siempre el motivo de cambios importantes

## Estado actual

### Implementado
- Gestión de mesas
- Gestión de sesiones
- Gestión de pedidos
- Gestión de platos
- Gestión de empleados
- Roles de empleados
- Notificaciones básicas
- Vista de cocina
- Vista de mozo
- Temporizadores de preparación
- Derivación de mesas entre mozos
- Arquitectura multicapa Spring Boot
- DTOs / View Objects
- Manejo de excepciones personalizadas

### Pendiente o susceptible de mejoras
- Optimización de consultas
- Refactorización de código legado
- Mejoras UI/UX
- Testing automatizado
- Reportes y métricas
- Optimización de notificaciones en tiempo real