# Prácticas evaluables de Sistemas Distribuidos

## Práctica 1 - Sockets:

### Descripción
Sistema de baneo de usuarios bloqueando mensajes provenientes de puertos específicos.

### Tecnologías
- Maven
- Ant
- Tomcat
- Java

### Estructura del proyecto

```
practica1/
├───src/java/es.ubu.lsi/client
│   ├──ChatClient
│   └──ChatClientImpl
├───src/java/es.ubu.lsi/common
│   └──ChatMessage
├───src/java/es.ubu.lsi/common
│   └──ChatMessage
├───src/java/es.ubu.lsi/server
│   ├──ChatServer
│   └──ChatServerImpl
├───build.xml
├───pom.xml
└───Practica1Chat.iml
```

### Funcionalidades
- Envío de mensajes entre varios clientes
- Baneo y desbaneo de usuarios por parte de clientes


## Práctica 2 — Sistema Spring Boot + API Python:

### Descripción
Aplicación web desarrollada con Spring Boot como frontend/backend y Flask (Python) como API REST.
El objetivo es demostrar el manejo de excepciones tanto a nivel de acceso a datos como en llamadas a APIs de terceros.

### Tecnologías
- **Backend:** Spring Boot 4, Spring Security, JPA, Hibernate, Thymeleaf
- **API:** Python 3, Flask
- **Base de datos:** MySQL 8
- **Contenedores:** Docker, Docker Compose

### Estructura del proyecto

```
practica2/
├── api-python/
│   └── app.py                # API Flask con endpoints de excepciones
├── src/main/java/
│   └── com/example/demo/
│       ├── config/           # Seguridad y autenticación
│       ├── controller/       # Controladores web
│       ├── model/            # Entidades JPA (User, Role)
│       ├── repository/       # Acceso a datos
│       └── service/          # Lógica de negocio
├── src/main/resources/
│   ├── application.properties
│   └── templates/            # Vistas Thymeleaf
│       ├── admin/            # Panel de administración
│       │   ├─panel.html
│       │   └─test.html
│       ├── index.html
│       ├── login.html
│       ├── registro.html
│       └── pokemon.html
└── docker-compose.yml
```

### Funcionalidades
- Registro e inicio de sesión con contraseñas cifradas en BCrypt
- Control de acceso por roles (USER / ADMIN)
- Gestión de roles de usuarios
- Buscador de Pokémon con datos desde la PokeAPI
- Simulación y manejo de excepciones:
    - Excepción de apertura y lectura de archivos
    - Excepción de acceso a base de datos
    - Excepción de llamada a API de terceros (PokeAPI)
- Traducción de errores no críticos al frontend en español

### Cómo ejecutar

#### Requisitos
- Docker y Docker Compose
- Python 3 con Flask (`pip install flask requests`)

#### Base de datos
```bash
docker-compose up mysqldb -d
```

#### API Python
```bash
cd api-python
python app.py
```

#### Spring Boot
Ejecutar la clase `Application.java` desde IntelliJ, o:
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8085`

Usuario administrador por defecto:
- **Usuario:** admin
- **Contraseña:** admin123

Sin embargo se pueden crear nuevos usuarios administradores mediante 
el panel de gestión de administración 

### Pruebas con Postman
Colección incluida con los siguientes endpoints de Flask (`localhost:5000`):

| Método | Endpoint                           | Descripción                 |
|--------|------------------------------------|-----------------------------|
| GET    | `/api/saludo`                      | Respuesta correcta          |
| GET    | `/api/exception/archivo`           | Error de archivo            |
| GET    | `/api/exception/bbdd`              | Error de base de datos      |
| GET    | `/api/exception/pokemon`           | Error de API Pokémon        |
| GET    | `/api/pokemon/pikachu`             | Pokémon real (éxito)        |
| GET    | `/api/pokemon/pokemon-inexistente` | Pokémon inexistente (error) |

## Taller — Sistema de gestión de Eventos con Spring Boot:

### Descripción
El objetivo de la práctica es crear y desarrollar un Frontend sofisticado y estéticamente agradable, con un tema de libre elección (En este caso gestión de eventos) que brinde a los usuarios una experiencia interactiva, intuitiva y cautivadora.

### Tecnologías
- **Backend:** Spring Boot 4, Spring Security, JPA, Hibernate, Thymeleaf
- **Base de datos:** MySQL 8
- **Contenedores:** Docker, Docker Compose
- **Integración de mapas con Google maps**: OpenStreetMap con Leaflet.js y Google Maps
- **Simulación de envío de mails**: Mailpit
- **Colas MQ**: RabbitMQ 3 - Management
- **LLM**: Gemini-2.5-flash para el sistema de FAQ

### Estructura del proyecto

```
taller/
├── src/main/java/
│   └── com/example/demo/
│       ├── config/           # Seguridad y autenticación
│       ├── controller/       # Controladores web
│       ├── dto/              # DTOs para mensajes
│       ├── exception/        # Excepciones
│       ├── faq/              # Frequently Asked Questions
│       ├── model/            # Entidades JPA (User, Role, Reserva, Evento)
│       ├── repository/       # Acceso a datos
│       ├── service/          # Lógica de negocio
│       └── Application.java  # Creación inicial de roles y un usuario administrador
├── src/main/resources/
│   ├── application.properties
│   └── templates/            # Vistas Thymeleaf
│       ├── admin/            # Panel de administración
│       │   ├─eventos/
│       │   │ ├─formulario.html
│       │   │ └─lista.html
│       │   ├─usuarios/
│       │   │ ├─formulario.html
│       │   │ └─lista.html
│       │   └─panel.html
│       ├── eventos/
│       │   ├─buscar.html
│       │   └─detalle.html
│       ├── reserva/
│       │   ├─confirmacion.html
│       │   ├─formulario.html
│       │   └─pago.html
│       ├── usuario.reservas/
│       │   ├─detalle.html
│       │   └─lista.html
│       ├── error.html
│       ├── faq.html
│       ├── index.html
│       ├── login.html
│       ├── politicaPrivacidad.html
│       └── registro.html
├── .env
├── pom.xml
├── Dockerfile
└── docker-compose.yml
```

### Funcionalidades
- Registro e inicio de sesión


- Control de acceso por roles (USER / ADMIN)


- Gestión de roles de usuarios
 

- Gestión de eventos


- Gestión de las reservas mediante colas MQ


- Gestión de Excepciones (Traducción de errores al frontend):
  - Por aforo agotado de un evento
  - Por duplicación de correo electrónico
  - Por duplicación de nombre de usuario
  - Por intento de reserva de un evento que ha sido eliminado o cancelado
  - Por intento de ver un evento que ha sido eliminado o cancelado


- Simulación de correos al reservar entradas


- Simulación de pasarela de pago


- Preguntas a un LLM

### Cómo ejecutar

#### Requisitos
- Docker y Docker Compose
- Java 25

#### Base de datos
```bash
docker-compose up mysqldb -d
```

#### Spring Boot
Ejecutar la clase `Application.java` desde IntelliJ, o:
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8085`

Usuario administrador por defecto:
- **Usuario:** admin
- **Contraseña:** admin123
- **Email:** admin@admin

Sin embargo se pueden crear nuevos usuarios administradores mediante
el panel de gestión de usuarios.

### Pruebas con Mailpit
Podemos simular recibir correos electrónicos con las reservas hechas después de hacer el pago en (`http://localhost:8025`).

Allí veremos los correos destinados a cada usuario que tenga correo electrónico, 
los usuarios que no añadan su correo electrónico en el registro nunca tendrán esta opción. 

### Acceso a la página de RabbitMQ 

Para acceder a las estadísticas, poder ver y gestionar las colas MQ vamos a (`http://localhost:15672/`). 
Ya en esta página accederemos con usuario `admin` y contraseña `admin123`

### Pruebas con Postman
Colección incluida con los siguientes endpoints:

| Método | Endpoint                | Descripción                              | Body                                                               |
|--------|-------------------------|------------------------------------------|--------------------------------------------------------------------|
| POST   | `/login`                | Login admin                              | username=admin, password=admin123                                  |
| POST   | `/login`                | Login usuario normal no existente        | username=maria, password=maria123                                  |
| POST   | `/login`                | Login usuario normal existente           | username=juan, password=juan123                                    |
| GET    | `/login?logout`         | Logout                                   | -                                                                  |
| POST   | `/registro`             | Registro nuevo usuario                   | username=paula, password=paula123                                  |
| POST   | `/registro`             | Registro usuario duplicado (error)       | username=juan, password=juan124                                    |
| POST   | `/faq`                  | FAQ: Preguntar sobre cancelaciones       | question=¿Puedo cancelar una reserva?, conversationId=abc123       |
| POST   | `/faq`                  | FAQ: Preguntar sobre algo no relacionado | question=¿Cuánto cuesta un hotel en Madrid?, conversationId=abc123 |
| GET    | `/politica-privacidad`  | Política de Privacidad                   | -                                                                  |

### Preguntas al LLM
Se puede hacer preguntas al asistente para no tener que leerte toda la política de privacidad. Así preguntas algo específico y te responde lo que quieres saber.
Esto funciona mediante una API de Google AI Studio con un modelo de Gemini.