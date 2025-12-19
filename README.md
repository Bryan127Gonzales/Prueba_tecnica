# Prueba Técnica Backend - ET Armadillo S.A.C.

## Descripción del Desafío

Bienvenido a la prueba técnica para el puesto de **Desarrollador Backend Junior** en ET Armadillo S.A.C.

Deberás desarrollar una API REST para un sistema de reserva de salas de coworking utilizando Java 17+, Spring Boot 3 y PostgreSQL.

## Requisitos Técnicos

- **Java:** Versión 17 o superior
- **Framework:** Spring Boot 3.x (obligatorio)
- **Base de Datos:** PostgreSQL
- **Build Tool:** Maven o Gradle (tú eliges)
- **Dependencias adicionales:** Puedes agregar las que consideres necesarias (Spring Data JPA, validaciones, etc.)

## Funcionalidades Requeridas

### 1. Gestión de Salas
Implementar las siguientes operaciones para las salas de coworking:

- **Registrar una sala** con los siguientes campos:
  - Nombre (obligatorio)
  - Capacidad de personas (obligatorio, mínimo 1)
  - Ubicación/Piso (opcional)
  - Equipamiento disponible (proyector, pizarra, etc.)
  - Estado (disponible, en mantenimiento, fuera de servicio)

- **Listar todas las salas**
  - Debe permitir filtrar por capacidad mínima
  - Debe permitir filtrar por estado
  - Debe permitir filtrar por disponibilidad en una fecha/hora específica

- **Obtener información de una sala específica** por su ID

- **Actualizar información de una sala**

- **Eliminar una sala**

### 2. Gestión de Reservas
Implementar las siguientes operaciones:

- **Crear una reserva** con los siguientes campos:
  - ID de la sala (obligatorio)
  - Nombre del responsable (obligatorio)
  - Email de contacto (obligatorio)
  - Fecha y hora de inicio (obligatorio)
  - Fecha y hora de fin (obligatorio)
  - Propósito/Motivo de la reserva (opcional)

- **Listar todas las reservas**
  - Debe permitir filtrar por sala
  - Debe permitir filtrar por fecha
  - Debe permitir filtrar por responsable

- **Obtener una reserva específica** por su ID

- **Cancelar una reserva** (eliminar)

- **Consultar disponibilidad** de una sala en un rango de fecha/hora

### 3. Validaciones
- Una sala no puede tener reservas superpuestas (dos reservas al mismo tiempo)
- La fecha/hora de fin debe ser posterior a la de inicio
- La capacidad de una sala debe ser mayor a 0
- El email debe tener un formato válido
- No se pueden crear reservas en el pasado
- Una reserva debe tener una duración mínima de 30 minutos

### 4. Manejo de Errores
- Respuestas apropiadas cuando una sala o reserva no existe
- Manejo de conflictos de horario (sala ya reservada)
- Validación de datos de entrada
- Manejo de errores de base de datos

## Puntos Opcionales (Bonus)

Si deseas, puedes implementar:

- **Reportes y estadísticas** (salas más usadas, horarios pico, tasa de ocupación)
- **Paginación** en el listado de salas y reservas
- **Búsqueda avanzada** de salas por equipamiento o características
- **Modificación de reservas** (cambiar hora, cambiar sala)
- **Docker/Docker Compose** para facilitar la ejecución del proyecto
- **Documentación de API** (Swagger/OpenAPI)

## Estructura del Proyecto

**IMPORTANTE:** La estructura de carpetas proporcionada es solo una **sugerencia básica**. Eres libre de:

- Reorganizar completamente la estructura del proyecto
- Agregar las capas que consideres necesarias (controllers, services, repositories, etc.)
- Crear los paquetes que mejor se ajusten a una arquitectura limpia
- Aplicar patrones de diseño que consideres apropiados
- Modificar o eliminar cualquier archivo existente

## Criterios de Evaluación

Evaluaremos los siguientes aspectos:

1. **Funcionalidad** (40%): Que la aplicación cumpla con los requisitos solicitados
2. **Calidad del Código** (30%): Código limpio, legible y bien organizado
3. **Arquitectura** (20%): Organización del proyecto y separación de responsabilidades
4. **Manejo de Errores** (10%): Validaciones y respuestas apropiadas

## Instrucciones de Entrega

1. Sube tu solución a un repositorio público en GitHub (o GitLab/Bitbucket)
2. Incluye un archivo README con:
   - Instrucciones para ejecutar el proyecto
   - Instrucciones para configurar la base de datos
   - Decisiones técnicas importantes que tomaste
   - Cualquier suposición que hayas hecho
3. Envía el enlace del repositorio a la dirección de correo que se te proporcionó

## Configuración Inicial

### Variables de Entorno

1. **Copia el archivo `.env.example` a `.env`:**
   ```bash
   cp .env.example .env
   ```

2. **Edita el archivo `.env`** con tus credenciales de PostgreSQL:
   ```env
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=armadillo_db
   DB_USERNAME=tu_usuario
   DB_PASSWORD=tu_contraseña
   ```

### Base de Datos

1. Instala PostgreSQL si aún no lo tienes
2. Ejecuta el script en `database/schema.sql` para crear la estructura inicial
3. Puedes modificar el esquema según tus necesidades

### Configuración de Spring Boot

1. Las variables de entorno se cargan automáticamente desde `.env`
2. Revisa `src/main/resources/application.properties` para ver la configuración
3. Agrega las dependencias necesarias en `pom.xml` (JPA, validaciones, etc.)

### Ejecución del Proyecto

```bash
# Con Maven
mvn clean install
mvn spring-boot:run

# Con Gradle (si decides usar Gradle)
./gradlew bootRun
```

La aplicación debería ejecutarse en `http://localhost:8080` por defecto.

## Notas Importantes

- No hay una única solución correcta
- Valoramos la creatividad y las buenas prácticas
- Si tienes dudas, puedes hacer suposiciones razonables (documéntalas en tu README)

**¡Mucha suerte!**