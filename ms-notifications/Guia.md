# Creación del Proyecto
## 📦 1. Crear el Proyecto ms-notificaciones
### 1.1 Iniciar Proyecto Spring Boot
Usa Spring Initializr o crea manualmente el proyecto con las siguientes dependencias:
* Spring Web
* Spring Data JPA
* PostgreSQL Driver
* Lombok
* Spring AMQP (RabbitMQ)
* SpringDoc OpenAPI UI (Opcional, para documentación)

### 1.2 Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com.ejemplo.notificaciones/
│   │       ├── NotificacionesApplication.java
│   │       ├── config/
│   │       │   ├── RabbitMQConfig.java
│   │       │   └── SwaggerConfig.java (opcional)
│   │       ├── controller/
│   │       │   └── NotificacionController.java
│   │       ├── dto/
│   │       │   └── NotificacionDto.java
│   │       ├── entity/
│   │       │   └── Notificacion.java
│   │       ├── repository/
│   │       │   └── NotificacionRepository.java
│   │       ├── service/
│   │       │   └── NotificacionService.java
│   │       └── listener/
│   │           └── NotificacionListener.java
│   └── resources/
│       ├── application.yml
│       └── data.sql (opcional)
```

## 📄 2. Configuración del Proyecto
### 2.1 Archivo application.yml
```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/notificaciones_db
    username: postgres
    password: tu_contraseña

  jpa:
    hibernate:
      use-new-id-generator-mappings: false
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        use_sql_comments: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect

  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin

server:
  port: 8081

springdoc:
  swagger-ui:
    path: /swagger-ui.html
  api-docs:
    path: /v3/api-docs
```

## 📝 3. Crear Entidades y Componentes
### 3.1 Notificacion.java (Entidad)
```
package com.ejemplo.notificaciones.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;
    private LocalDateTime fecha;
    private String tipo; // Ej: "libro", "articulo"
}
```
### 3.2 NotificacionRepository.java
```
package com.ejemplo.notificaciones.repository;

import com.ejemplo.notificaciones.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {}
```
### 3.3 NotificacionDto.java
```
package com.ejemplo.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDto {
    private String mensaje;
    private String tipo;
}
```
### 3.4 NotificacionService.java
```
package com.ejemplo.notificaciones.service;

import com.ejemplo.notificaciones.dto.NotificacionDto;
import com.ejemplo.notificaciones.entity.Notificacion;
import com.ejemplo.notificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public void guardarNotificacion(NotificacionDto dto) {
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());
        notificacion.setFecha(LocalDateTime.now());

        notificacionRepository.save(notificacion);
    }
}
```

### 3.5 NotificacionListener.java
#### Consumidor
**🔍 Función:**
* El consumidor escucha mensajes en una cola y realiza una acción cuando llega uno.
* En este caso, guarda la notificación en la base de datos.
* Usa la anotación @RabbitListener para escuchar la cola "notificaciones.cola".
* Convierte el mensaje JSON recibido en un objeto Java (NotificacionDto) y lo procesa.
```
package com.ejemplo.notificaciones.listener;

import com.ejemplo.notificaciones.dto.NotificacionDto;
import com.ejemplo.notificaciones.service.NotificacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacionListener {

    private final NotificacionService notificacionService;
    private final ObjectMapper objectMapper;

    public NotificacionListener(NotificacionService notificacionService, ObjectMapper objectMapper) {
        this.notificacionService = notificacionService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "notificaciones.cola")
    public void recibirMensaje(String mensajeJson) {
        try {
            NotificacionDto dto = objectMapper.readValue(mensajeJson, NotificacionDto.class);
            notificacionService.guardarNotificacion(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 3.6 RabbitMQConfig.java
#### La Cola - Queue
**🔍 Función:**
- Una cola almacena temporalmente los mensajes hasta que son consumidos por un consumidor.
- La cola "notificaciones.cola" recibe las notificaciones generadas por ms-publicaciones.
- Se define como durable , lo que significa que sobrevive a reinicios de RabbitMQ.

```
package com.ejemplo.notificaciones.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue notificacionesCola() {
        return QueueBuilder.durable("notificaciones.cola").build();
    }
}
```


### 3.7 NotificacionesApplication.java
```
package com.ejemplo.notificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificacionesApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificacionesApplication.class, args);
    }
}
```
## 🐳 4. Levantar RabbitMQ con Docker
### Crea un archivo docker-compose.yml:
```
version: '3.8'

services:
  rabbitmq:
    image: rabbitmq:3.13-management
    container_name: rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: admin
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
    networks:
      - internal-network

volumes:
  rabbitmq_data:

networks:
  internal-network:
    driver: bridge
```

### levantar el servcio
```
docker-compose up -d
```

### Accede a la interfaz web de RabbitMQ en:
```
http://localhost:15672
Usuario: admin
Contraseña: admin
```

## 🔁 5. Modificar Microservicio ms-publicaciones
### 5.1 Agregar Dependencia de RabbitMQ al pom.xml
**🔍 Función:**
- Proporciona soporte para conectarse con RabbitMQ desde aplicaciones Spring Boot.
- Incluye herramientas como RabbitTemplate, @RabbitListener, etc.

```
<!-- Spring AMQP (RabbitMQ) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```
### 5.2 DTO Compartido: NotificacionDto.java
**🔍 Función:**
* Es el modelo compartido entre los microservicios para definir cómo debe ser un mensaje enviado o recibido.
* Permite que ambos lados entiendan la estructura del mensaje sin acoplamiento directo entre ellos.
```
// src/main/java/publicacion/dto/NotificacionDto.java

package publicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDto {
    private String mensaje;
    private String tipo;
}
```
### 5.3 Servicio: NotificacionProducer.java
**🔍 Función:**
- El productor es quien envía mensajes a RabbitMQ .
- Utiliza RabbitTemplate para publicar mensajes en una cola específica (notificaciones.cola).
- Convierte objetos Java (como NotificacionDto) a formato JSON usando ObjectMapper.
- Es usado desde servicios como LibroService cuando se crea un libro o artículo.
```
package publicacion.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacionProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public NotificacionProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void enviarNotificacion(String mensaje, String tipo) {
        try {
            NotificacionDto dto = new NotificacionDto(mensaje, tipo);
            String json = objectMapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend("notificaciones.cola", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
```

### 5.4 Enviar Notificación al Crear Libro
#### En LibroService.java:
```
@Autowired
private NotificacionProducer notificacionProducer;

// Dentro del método crearLibro(...)
notificacionProducer.enviarNotificacion(
    "Nuevo libro creado: " + libro.getTitulo(),
    "libro"
);
```

### 5.5. Configuración en application.yml
Agregar la siguiente sección en la propiedad spring del proyecto
```
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin
```
**🔍 Función:**
- Define la conexión a RabbitMQ:
- *host* : Dónde está corriendo RabbitMQ.
- *port* : Puerto AMQP (por defecto: 5672).
- *username/password* : Credenciales si usaste autenticación (recomendado).

## ✅ 6. Funcionamiento Final
* Al crear un libro/artículo en ms-publicaciones, se envía una notificación a RabbitMQ.
* El microservicio ms-notificaciones escucha esa cola.
* La notificación es guardada en su base de datos PostgreSQL.
* Se puede acceder a todas las notificaciones vía API REST o Swagger.