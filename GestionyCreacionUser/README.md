# Gestión y Creación de Usuarios - Microservicio

## Descripción

Este microservicio está desarrollado con Spring Boot y Maven para gestionar la creación y administración de usuarios en una aplicación. Los tipos de usuarios que maneja son: Pacientes, Médicos y Administradores. Cada uno tiene su propio conjunto de datos y funcionalidades, expuestas mediante APIs REST, siguiendo una arquitectura en capas: Model, Repository, Service y Controller.

## Tecnologías usadas

- Java 17  
- Spring Boot  
- Oracle Database  
- Maven  
- Lombok  
- Postman (para pruebas de endpoints)
- Swagger (SpringDoc OpenAPI) 

## Dependencias incluidas (`pom.xml`)

- `spring-boot-starter-web`  
- `spring-boot-starter-data-jpa`  
- `lombok`  
- `ojdbc8` (Oracle JDBC Driver)  
- `oraclepki`, `osdt_core`, `osdt_cert` (Oracle Wallet Security)  
- `springdoc-openapi-starter-webmvc-ui` (Swagger UI / documentación de API)

## Estructura del proyecto

**Modelos:**  
Definen las entidades principales: Paciente, Médico, Administrador y Rol. Cada clase tiene sus atributos específicos y una relación `@ManyToOne` con la entidad `Rol`.

**Repository:**  
Extienden `JpaRepository` para proporcionar métodos que permiten leer, guardar, actualizar y eliminar datos en la base de datos, incluyendo consultas personalizadas (como buscar por nombre).

**Service:**  
Contienen la lógica de negocio para cada entidad: guardar, actualizar, listar y eliminar usuarios.

**Controller:**  
Define los endpoints REST para exponer las operaciones CRUD de cada entidad. Además, en esta capa se implementan validaciones específicas para asegurar la integridad de los datos antes de que lleguen a la lógica de negocio (por ejemplo, evitar duplicados, asegurar campos requeridos, etc). Ademas maneja respuestas con códigos HTTP adecuados (ejemplo: 200, 201, 400, 404)..

### Nota sobre la entidad Rol

La entidad `Rol` representa los diferentes tipos de usuarios en el sistema (`Paciente`, `Médico`, `Administrador`). Esta entidad **no** cuenta con su propio repositorio, servicio ni controlador, ya que los roles están precargados manualmente en la base de datos mediante scripts SQL (`INSERT` y `COMMIT`).

Los roles se asignan directamente como atributos en las entidades de usuario al momento de su creación.

## Funcionalidades principales

- Crear, actualizar, eliminar y listar usuarios (Pacientes, Médicos, Administradores).
- Búsquedas por atributos relevantes (ejemplo: buscar pacientes por nombre o RUT).
- Validaciones básicas en las solicitudes para asegurar datos completos y evitar duplicados.
- Asignación automática del rol correspondiente al crear un usuario.

## Configuración importante

- Configuración en `application.properties` para conexión a base de datos Oracle con parámetros de Wallet.
- Hibernate configurado para actualizar el esquema automáticamente (`spring.jpa.hibernate.ddl-auto=update`).
