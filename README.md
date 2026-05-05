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
│   ├ application.properties
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