# Prueba Técnica – API Backend

**Autor:** BRAYHAM CARLOS GONZALES GUZMAN

---

## Descripción
API REST desarrollada como parte de una prueba técnica.  
Permite la gestión de recursos mediante endpoints HTTP siguiendo buenas prácticas de arquitectura backend.

---

## Tecnologías y versiones

- **Java:** 17
- **Spring Boot:** 3.5.9
- **PostgreSQL:** 18.1
- **Build Tool:** Maven

---

## I. Configuración del entorno

El proyecto utiliza variables de entorno para la configuración.

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
3. **Cambio de puerto del servidor**
- El servicio fue reconfigurado para utilizar el puerto `8081` en lugar del 8080, con el fin de evitar cruce de puertos con otras aplicaciones.


## II. Cambios en el script de base de datos

Se realizaron ajustes al script de la base de datos con el objetivo de mejorar la estructura y consistencia de la información.

### Cambios realizados
- Se estableció una relación entre la tabla bookings y la tabla rooms mediante una llave foránea (room_id) en bookings, la cual referencia el campo id de la tabla rooms.”


### Entorno de base de datos
- La base de datos está configurada para ejecutarse **de manera local**.
- El script puede ejecutarse en una instancia local de PostgreSQL antes de iniciar la aplicación.

## III. Ejecución del Proyecto

   ```bash
   # Con Maven
   mvn clean install
   mvn spring-boot:run
   ```
La aplicación debería ejecutarse en `http://localhost:8081`.

## IV. Decisiones técnicas importantes

- Se utilizó una arquitectura en capas (controlador, servicio y repositorio) para mantener una correcta separación de responsabilidades y facilitar el mantenimiento del código.

- Se agregó una relación entre las tablas(se menciona en el punto II) con el objetivo de cumplir el filtro "Consultar disponibilidad de una sala en un rango de fecha/hora"
- Se implementaron validaciones de negocio a nivel de la API para asegurar que los datos recibidos cumplan con las reglas establecidas.
- Se manejaron los errores de forma controlada, devolviendo respuestas claras y coherentes al cliente.

---

## V. Suposiciones realizadas

- Se asumió que el entorno de ejecución cuenta con una instancia de PostgreSQL configurada de manera local.
- Se asumió que el modelo de datos podía ser ajustado ligeramente (agregando relaciones) para mejorar la coherencia, al no existir restricciones explícitas sobre su modificación.

## VI. Guía de Pruebas de la API

## Consideraciones previas

- #### Estado de las salas (ENUM)

El campo **status** de una sala utiliza un tipo **ENUM**, por lo que **los valores deben enviarse obligatoriamente en mayúsculas**.

Valores permitidos:
- `DISPONIBLE`
- `MANTENIMIENTO`
- `FUERA_DE_SERVICIO`

Enviar un valor distinto o en minúsculas producirá un error de validación.

---

## 1. Gestión de Salas

**Ruta base:** http://localhost:8081/api/rooms
### Crear una sala
- **Método:** POST
- **URL completa:** http://localhost:8081/api/rooms

- **Body (JSON):**
```json
{
   "name": "Sala Técnica",
   "capacity": 8,
   "location": "Piso 3",
   "equipment": "Proyector, Pizarra",
   "status": "DISPONIBLE"
}
```
### Listar todas las salas
- **Método:** GET
- **URL completa:** http://localhost:8081/api/rooms

### Filtros
- Debe permitir filtrar por capacidad mínima: http://localhost:8081/api/rooms?minCapacity=10
  (**Cambiar el valor "10" por el que guste.**)
- Debe permitir filtrar por estado: http://localhost:8081/api/rooms?status=MANTENIMIENTO
  (**Cambiar el status "MANTENIMIENTO" por el que guste.**)
- Obtener información de una sala específica por su ID:http://localhost:8081/api/rooms/2
  (**Cambiar el id "2" por el que exista.**)
### Actualizar una sala
- **Método:** PUT
- http://localhost:8081/api/rooms/2
- Cambiar el id "2" por el que exista.
### Eliminar una sala:
- **Método:** DELETE
- http://localhost:8081/api/rooms/1
- **Cambiar el id "2" por el que exista.**

## 2. Gestión de Reservas

**Ruta base:** http://localhost:8081/api/bookings
### Crear una sala
- **Método:** POST
- **URL completa:** http://localhost:8081/api/bookings

- **Body (JSON):**
    ```json
     {
      "roomId": 2,
      "responsibleName": "Juan Pérez",
      "contactEmail": "juan.perez@mail.com",
      "startTime": "2025-12-26T09:00",
      "endTime": "2025-12-26T11:00",
      "purpose": "Reunión de equipo"
    }
    ```
- El valor de roomId debe existir en la tabla rooms
### Listar todas reservas
- **Método:** GET
- **URL completa:** http://localhost:8081/api/bookings
### Filtros
- Debe permitir filtrar por sala:http://localhost:8081/api/bookings?roomId=5
(**Cambiar el roomid "5" por el que exista.**)
- Debe permitir filtrar por fecha: http://localhost:8081/api/bookings?date=2025-12-27
  (**Cambiar el valor de "date" por uno que exista.**)
- Debe permitir filtrar por responsable: http://localhost:8081/api/bookings?responsible=Juan%20Pérez
  (**Cambiar el valor de "responsible" por uno que exista usando el separador %20.**)

### Obtener una reserva específica por su ID
- http://localhost:8081/api/bookings/2
- Cambiar el id "2" por el que exista.

### Cancelar una reserva (eliminar)
- **Método:** PUT
- http://localhost:8081/api/bookings/1
- Cambiar el id "1" por el que exista.

### Consultar disponibilidad de una sala en un rango de fecha/hora
- **Método:** DELETE
- `api/{idrooms}/availability?start=fecha-hora&end=fecha-hora`
- http://localhost:8081/api/rooms/2/availability?start=2025-12-26T11%3A00&end=2025-12-26T17%3A00
- Rango: 2025-12-26 -> Hora: 11:00-17:00
- Cambiar idrooms (Ejm: 2)
- Para los tiempos se utilizo el separador "%3A" que corresponde al carácter ":" (dos puntos).
- Formato de tiempo: 2025-12-26T11%3A00
- 
## 3. Validaciones
- Una sala NO puede tener reservas superpuestas(dos reservas al mismo tiempo)
  `POST /api/bookings`
- **Cambiar la fecha y hora a una que ya exista**
    ```json
  {
  "roomId": 2,
  "responsibleName": "Prueba Conflicto",
  "contactEmail": "conflicto@mail.com",
  "startTime": "2025-12-26T10:00",
  "endTime": "2025-12-26T11:30",
  "purpose": "Reserva superpuesta"
  }
    ```

- Fin debe ser posterior al inicio
  `POST /api/bookings`
    ```json
      {
      "roomId": 2,
      "responsibleName": "Prueba Conflicto",
      "contactEmail": "conflicto@mail.com",
      "startTime": "2025-12-26T10:00",
      "endTime": "2025-12-26T11:30",
      "purpose": "Reserva superpuesta"
      }
    ```
- Capacidad de sala > 0
      `POST /api/rooms`
    ```json
    {
      "name": "Sala Error",
      "capacity": 0,
      "location": "Piso X",
      "equipment": "Nada",
      "status": "DISPONIBLE"
    }
    ```
- Email con formato válido
    ```json
    {
      "roomId": 2,
      "responsibleName": "Email Malo",
      "contactEmail": "correo-mal-formato",
      "startTime": "2025-12-26T15:00",
      "endTime": "2025-12-26T16:00",
      "purpose": "Prueba email"
    }
    ```
- No se pueden crear reservas en el pasado
  ```json
  {
    "roomId": 2,
    "responsibleName": "Pasado",
    "contactEmail": "pasado@mail.com",
    "startTime": "2023-01-01T09:00",
    "endTime": "2023-01-01T10:00",
    "purpose": "Reserva en el pasado"
  }
  ```
- Duración mínima 30 minutos
  ```json
  {
    "roomId": 2,
    "responsibleName": "Duración corta",
    "contactEmail": "duracion@mail.com",
    "startTime": "2025-12-26T14:00",
    "endTime": "2025-12-26T14:20",
    "purpose": "Muy corta"
  }
  ```
  