# 🏨 MallorcaStay — Sistema de Gestión Hotelera Integral

## Descripción General

MallorcaStay es una plataforma de gestión hotelera completa desarrollada en Java con Spring Boot, orientada a hoteles medianos y grandes de las Islas Baleares. El sistema cubre desde la gestión operativa diaria del hotel (reservas, habitaciones, huéspedes, empleados) hasta un panel de analíticas avanzado para la dirección. El proyecto simula un entorno empresarial real con arquitectura por capas, seguridad, API REST documentada y despliegue en contenedores.

---

## Estructura del repositorio y ramas

- Ramas principales:
  - `main` — versión estable/producción
  - `develop` — integración de features
  - `feature/*` — ramas por funcionalidad

- Convenciones:
  - Pull Requests hacia `develop` y revisiones por pares
  - Commits con mensajes descriptivos y ticket asociado (opcional)

---

## Paquetes y arquitectura por capas (base creada)

Paquetes principales creados:
```
com.mallorca.Stay.controller
com.mallorca.Stay.service
com.mallorca.Stay.service.impl
com.mallorca.Stay.repository
com.mallorca.Stay.domain.entity
com.mallorca.Stay.domain.enums
com.mallorca.Stay.dto.request
com.mallorca.Stay.dto.response
com.mallorca.Stay.mapper
com.mallorca.Stay.exception
com.mallorca.Stay.config
com.mallorca.Stay.util
```

---

## Módulos del proyecto (roadmap)

- MÓDULO 1 — Configuración Base y Arquitectura del Proyecto (1 semana)
  - Inicializar proyecto con Spring Initializr.
  - Dependencias clave: Spring Boot, Spring Web, Spring Data JPA, Spring Security, Validation, Lombok, MapStruct, PostgreSQL.
  - Docker Compose para PostgreSQL + pgAdmin.
  - README detallado, diagrama de arquitectura y badges.

- MÓDULO 2 — Modelo de Datos y Base de Datos (1 semana)
  - Diseño relacional completo (Hotel, TipoHabitacion, Habitacion, Cliente, Reserva, Empleado, Factura, ServicioExtra, etc.).
  - Migraciones versionadas con Flyway.
  - Datos de prueba realistas.

- MÓDULO 3 — API REST de Gestión de Habitaciones (1 semana)
  - CRUD de habitaciones y tipos.
  - Endpoint de disponibilidad por rango de fechas y número de personas.
  - Paginación, filtrado y validación.

- MÓDULO 4 — API REST de Clientes (4 días)
  - Registro, edición, búsqueda y baja lógica.

- MÓDULO 5 — API REST de Reservas (2 semanas)
  - Validaciones complejas, máquina de estados, check-in/check-out, notificaciones por email.

- MÓDULO 6 — Facturación (1 semana)
  - Generación automática de facturas PDF, envío por email y rectificaciones.

- MÓDULO 7 — Seguridad con Spring Security y JWT (1 semana)
  - Autenticación JWT, roles y permisos (ADMIN, RECEPCION, LIMPIEZA), refresco de tokens.

- MÓDULO 8 — Panel de Analíticas (2 semanas)
  - Informes: ocupación, RevPAR, ADR, ingresos, ranking de habitaciones, análisis de clientes y previsiones.

- MÓDULO 9 — Frontend con Thymeleaf o React (2 semanas)
  - Dashboard con gráficas (Chart.js), calendario de reservas y formularios.

- MÓDULO 10 — Testing (1 semana)
  - Tests unitarios (JUnit 5 + Mockito), integración (Spring Boot Test, H2), cobertura con JaCoCo.

- MÓDULO 11 — Documentación y Despliegue (1 semana)
  - OpenAPI/Swagger, docker-compose de producción (app + DB + Nginx), despliegue en Railway/Render/Cloud.

---

## Configuración local (rápida)

Prerequisitos:
- Java 17+
- Maven (o usar `./mvnw` si el wrapper está presente)
- Docker Desktop (si vas a usar Docker Compose)

Variables principales en `src/main/resources/application.properties`:

- Base de datos (local / Docker):
```
spring.datasource.url=jdbc:postgresql://localhost:5432/mallorcaStay
spring.datasource.username=mallorca_user
spring.datasource.password=mallorca_pass
```

> Nota: si arrancas la app dentro de `docker-compose`, cambia la URL a `jdbc:postgresql://postgres:5432/mallorcaStay`.

Flyway:
- Las migraciones están en `src/main/resources/db/migration`.
- Actualmente `V1__init.sql` contiene `SELECT 1;` como placeholder para evitar errores en arranque inicial. El esquema completo se definirá en Módulo 2.

Iniciar base de datos y pgAdmin con Docker Compose:

```bash
cd "/Users/pedro/IdeaProjects/Sistema De Gestion Hotelera Integral"
docker compose up -d
```

Comprobar contenedores:

```bash
docker compose ps
```

Logs de Postgres:

```bash
docker compose logs -f postgres
```

Conectar psql (si tienes el cliente instalado):

```bash
psql -h localhost -p 5432 -U mallorca_user -d mallorcaStay
# contraseña: mallorca_pass
```

---

## Cómo compilar y ejecutar la app

Compilar (desde la raíz):

```bash
# Si dispones del wrapper
./mvnw -DskipTests package
# O con Maven instalado
mvn -DskipTests package
```

Ejecutar (jar generado):

```bash
java -jar target/*.jar
```

Ejecutar desde el IDE: configura la `application.properties` y ejecuta la clase `MallorcaStayApplication`.

---

## Notas de desarrollo y buenas prácticas

- Mantener migraciones de Flyway atómicas y numeradas correctamente.
- Añadir fixtures realistas en sql/seed para pruebas y analíticas.
- Usar DTOs y mappers (MapStruct) para desacoplar capa persistencia y presentación.
- Validar entradas con `@Valid` y centralizar manejo de errores con `@RestControllerAdvice`.

---

## Diagrama de arquitectura (placeholder)

![Arquitectura](docs/architecture.png)

> Añade aquí el diagrama ER y el diagrama de componentes cuando estén disponibles.

---

## Contacto

Proyecto preparado por el equipo MallorcaStay. Para dudas o sugerencias, abre un issue o PR en el repositorio.

---

*Última actualización: 27/02/2026*

