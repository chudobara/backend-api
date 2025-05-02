# Backend API - Sistema de Gestión de Clientes y Tickets

## Descripción

Este proyecto implementa una API REST para la gestión de clientes y tickets, desarrollada con Spring Boot. El sistema permite crear, consultar, actualizar y eliminar tanto clientes como tickets, con diversas validaciones de negocio incluyendo una validación externa para la creación de clientes.

## Características

- **Gestión de Clientes**: CRUD completo para clientes
  - Validación externa de clientes a través de API de terceros
  - Validación de DNI único
  - Verificación de datos obligatorios

- **Gestión de Tickets**: CRUD completo para tickets
  - Asociación de tickets a clientes
  - Asignación de tickets adicionales a clientes existentes
  - Validaciones de negocio (costo, fecha, eventos)

- **Configuración de Base de Datos**:
  - Base de datos H2 en memoria para desarrollo
  - Scripts de inicialización de datos

- **Características Técnicas**:
  - Arquitectura por capas (Controller, Service, Repository)
  - Manejo de excepciones centralizado
  - DTOs para transferencia de datos
  - Filtros de seguridad HTTP
  - Codificación UTF-8 completa

## Requisitos

- Java 17 o superior
- Maven 3.8 o superior

## Instalación y Ejecución

1. Clonar el repositorio:
```bash
git clone  https://github.com/chudobara/backend-api.git
cd backend-api
```

2. Compilar el proyecto:
```bash
mvn clean package
```

3. Ejecutar la aplicación:
```bash
java -jar target/backend-api-1.0-SNAPSHOT.jar
```

La aplicación estará disponible en http://localhost:8080

## Configuración

El proyecto incluye tres perfiles de configuración:

- **default**: Configuración predeterminada
- **dev**: Entorno de desarrollo con consola H2 habilitada
- **test**: Configuración para pruebas

Para ejecutar con un perfil específico:
```bash
java -jar target/backend-api-1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

## Cómo utilizar

Para ejecutar la aplicación localmente, sigue estos pasos:

```bash
# Compilar el proyecto
mvn clean package

# Ejecutar la aplicación
java -jar target/backend-api-1.0-SNAPSHOT.jar
```

También puedes ejecutar la aplicación con un perfil específico:

```bash
java -jar target/backend-api-1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

## API

### Introducción

La API está diseñada para proporcionar funcionalidades de gestión de clientes y tickets. Permite crear, consultar, actualizar y eliminar tanto clientes como tickets, con diversas validaciones de negocio incluyendo una validación externa para la creación de clientes.

### Prerrequisitos

- Java 17 o superior
- Maven 3.8 o superior
- Base de datos H2 (incluida)

## Endpoints API

### Clientes

- `GET /api/clients`: Obtener todos los clientes
- `GET /api/clients/{id}`: Obtener un cliente por ID
- `GET /api/clients/dni/{dni}`: Obtener un cliente por DNI
- `POST /api/clients`: Crear un nuevo cliente
- `PUT /api/clients/{id}`: Actualizar un cliente existente
- `DELETE /api/clients/{id}`: Eliminar un cliente

### Tickets

- `GET /api/tickets`: Obtener todos los tickets
- `GET /api/tickets/{id}`: Obtener un ticket por ID
- `GET /api/tickets/client/{clientId}`: Obtener tickets por ID de cliente
- `POST /api/tickets`: Crear un nuevo ticket
- `POST /api/tickets/client/{clientId}`: Asignar un ticket adicional a un cliente
- `PUT /api/tickets/{id}`: Actualizar un ticket existente
- `DELETE /api/tickets/{id}`: Eliminar un ticket

### Descripción de la API

#### Rutas disponibles

##### Clientes (/api/clients)

- **GET /api/clients**: Obtiene todos los clientes.
```bash
# GET /api/clients
# http://localhost:8080/api/clients

{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Perez",
  "dni": "11111111A",
  "fechaNacimiento": "1985-03-15"
}
```

- **GET /api/clients/{id}**: Obtiene un cliente específico por su ID.
```bash
# GET /api/clients/{id}
# http://localhost:8080/api/clients/1

{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Perez",
  "dni": "11111111A",
  "fechaNacimiento": "1985-03-15"
}
```

Si la operación fallara, la respuesta se devolverá en formato JSON:

```json
{
  "message": "No se encontró el cliente con ID: 999"
}
```

- **GET /api/clients/dni/{dni}**: Obtiene un cliente específico por su DNI.
```bash
# GET /api/clients/dni/{dni}
# http://localhost:8080/api/clients/dni/11111111A

{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Perez",
  "dni": "11111111A",
  "fechaNacimiento": "1985-03-15"
}
```

- **POST /api/clients**: Crea un nuevo cliente. Espera recibir por body la información del nuevo cliente.
```bash
# POST /api/clients
# http://localhost:8080/api/clients

{
  "nombre": "John",
  "apellido": "Doe",
  "dni": "12345678A",
  "fechaNacimiento": "1990-01-01"
}
```

Si la operación es exitosa, la respuesta se devolverá en formato JSON:

```json
{
  "id": 5,
  "nombre": "John",
  "apellido": "Doe",
  "dni": "12345678A",
  "fechaNacimiento": "1990-01-01"
}
```

**Importante**: Este endpoint realiza una validación externa del nombre y apellido a través de una API de terceros antes de registrar al cliente.

- **PUT /api/clients/{id}**: Actualiza un cliente existente. Espera recibir por body la información a actualizar.
```bash
# PUT /api/clients/{id}
# http://localhost:8080/api/clients/1

{
  "nombre": "Juan Carlos",
  "apellido": "Perez",
  "dni": "11111111A",
  "fechaNacimiento": "1985-03-15"
}
```

- **DELETE /api/clients/{id}**: Elimina un cliente existente.
```bash
# DELETE /api/clients/{id}
# http://localhost:8080/api/clients/1
```

##### Tickets (/api/tickets)

- **GET /api/tickets**: Obtiene todos los tickets.
```bash
# GET /api/tickets
# http://localhost:8080/api/tickets

[
  {
    "id": 1,
    "cliente": {
      "id": 1,
      "nombre": "Juan",
      "apellido": "Perez",
      "dni": "11111111A",
      "fechaNacimiento": "1985-03-15"
    },
    "evento": "Concierto de Rock",
    "costo": 5000.00,
    "fechaVigencia": "2025-12-30"
  },
  {
    "id": 2,
    "cliente": {
      "id": 1,
      "nombre": "Juan",
      "apellido": "Perez",
      "dni": "11111111A",
      "fechaNacimiento": "1985-03-15"
    },
    "evento": "Festival del Choclo",
    "costo": 3000.00,
    "fechaVigencia": "2025-11-30"
  }
]
```

- **GET /api/tickets/{id}**: Obtiene un ticket específico por su ID.
```bash
# GET /api/tickets/{id}
# http://localhost:8080/api/tickets/1

{
  "id": 1,
  "cliente": {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Perez",
    "dni": "11111111A",
    "fechaNacimiento": "1985-03-15"
  },
  "evento": "Concierto de Rock",
  "costo": 5000.00,
  "fechaVigencia": "2025-12-30"
}
```

- **GET /api/tickets/client/{clientId}**: Obtiene tickets por ID de cliente.
```bash
# GET /api/tickets/client/{clientId}
# http://localhost:8080/api/tickets/client/1

[
  {
    "id": 1,
    "cliente": {
      "id": 1,
      "nombre": "Juan",
      "apellido": "Perez",
      "dni": "11111111A",
      "fechaNacimiento": "1985-03-15"
    },
    "evento": "Concierto de Rock",
    "costo": 5000.00,
    "fechaVigencia": "2025-12-30"
  },
  {
    "id": 2,
    "cliente": {
      "id": 1,
      "nombre": "Juan",
      "apellido": "Perez",
      "dni": "11111111A",
      "fechaNacimiento": "1985-03-15"
    },
    "evento": "Festival del Choclo",
    "costo": 3000.00,
    "fechaVigencia": "2025-11-30"
  }
]
```

- **POST /api/tickets**: Crea un nuevo ticket. Espera recibir por body la información del nuevo ticket.
```bash
# POST /api/tickets
# http://localhost:8080/api/tickets

{
  "cliente": {
    "id": 1
  },
  "evento": "Concierto de Jazz",
  "costo": 4500.00,
  "fechaVigencia": "2025-10-15"
}
```

- **POST /api/tickets/client/{clientId}**: Asigna un ticket adicional a un cliente existente.
```bash
# POST /api/tickets/client/{clientId}
# http://localhost:8080/api/tickets/client/1

{
  "evento": "Festival de Verano",
  "costo": 6000.00,
  "fechaVigencia": "2025-08-20"
}
```

**Nota**: Este endpoint requiere que el cliente ya tenga al menos un ticket existente.

- **PUT /api/tickets/{id}**: Actualiza un ticket existente.
```bash
# PUT /api/tickets/{id}
# http://localhost:8080/api/tickets/1

{
  "evento": "Concierto de Rock & Roll",
  "costo": 5500.00,
  "fechaVigencia": "2025-12-30"
}
```

- **DELETE /api/tickets/{id}**: Elimina un ticket existente.
```bash
# DELETE /api/tickets/{id}
# http://localhost:8080/api/tickets/1
```

## Validación Externa de Clientes

Antes de registrar un cliente, se realiza una validación externa del nombre y apellido mediante un endpoint proporcionado:

- **URL**: https://test.paseshow.com.ar/permissions/paseshow/util/technical-test
- **Método**: POST
- **Headers**: 
  - Content-Type: application/json
  - Authorization: [Base64(nombre:apellido)]
- **Body**: 
  ```json
  {
    "nombre": "John",
    "apellido": "Doe"
  }
  ```

La validación solo permite crear el cliente si el endpoint responde con status code 200 OK y el contenido incluye "OK".

## Estructura del Proyecto

```
src/
  main/
    java/
      com/
        example/
          BackendApiApplication.java    # Punto de entrada
          config/                       # Clases de configuración
          controller/                   # Controladores REST
          exceptions/                   # Excepciones personalizadas
          model/                        # Entidades y DTOs
          repository/                   # Interfaces de repositorio
          service/                      # Servicios de negocio
    resources/
      application.properties            # Configuración principal
      application-dev.properties        # Configuración de desarrollo
      application-test.properties       # Configuración de pruebas
      data.sql                          # Datos iniciales
  test/                                 # Tests unitarios e integración
```

## Base de Datos

El proyecto utiliza H2 como base de datos en memoria para desarrollo. Los datos iniciales se cargan desde el archivo `data.sql`, que incluye:

- 4 clientes de ejemplo
- 3 tickets asociados a estos clientes

## Seguridad

El proyecto incluye un filtro de validación HTTP para proteger contra ataques y solicitudes malformadas.

## Tests

Para ejecutar las pruebas:
```bash
mvn test
```

## Desarrollado con

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/)
- [Maven](https://maven.apache.org/)

## Autor

[Cesar Martin Chuchuy] - [cesarmartinchuchuy@gmail.com]

## Licencia

Este proyecto está licenciado bajo [Licencia] - ver el archivo LICENSE.md para más detalles.
