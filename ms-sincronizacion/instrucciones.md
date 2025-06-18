# Crear el microservicio sincronizacion

## Crear un nuevo proyecto
Agregar las siguientes dependencias
* Spring Boot Web
* Spring AMQP (RabbitMQ)
* Lombok
* Spring Boot DevTools

## Crear la siguiente estructura
```
src/
└── main/
    └── java/
        └── com.ejemplo.sincronizacion/
            ├── SincronizacionApplication.java
            ├── config/
            │   ├── RabbitMQConfig.java
            │   └── SchedulerConfig.java
            ├── dto/
            │   ├── HoraClienteDto.java
            │   └── HoraServidorDto.java
            ├── listener/
            │   └── RelojListener.java
            ├── service/
            │   └── SincronizacionService.java
            └── controller/
                └── SincronizacionController.java
```
## Orden de creación de los archivos
1. SincronizacionApplication.java
2. dto/HoraClienteDto.java
3. dto/HoraServidorDto.java
4. service/SincronizacionService.java
5. listener/RelojListener.java
6. config/RabbitMQConfig.java
7. config/SchedulerConfig.java
8. controller/SincronizacionController.java

## Actualización en ms-publicaciones
Tener en cuenta la nueva estructura

```
src/
└── main/
    └── java/
        └── publicacion/
            ├── PublicacionesApplication.java
            ├── config/
            │   ├── RabbitMQConfig.java
            │   ├── RelojScheduler.java
            │   └── SwaggerConfig.java
            ├── controller/
            │   ├── AutorController.java
            │   ├── LibroController.java
            │   └── ArticuloCientificoController.java
            ├── dto/
            │   ├── AutorDto.java
            │   ├── LibroDto.java
            │   ├── ArticuloCientificoDto.java
            │   ├── ResponseDto.java
            │   ├── HoraClienteDto.java  <-- Nuevo
            │   └── NotificacionDto.java <-- Modificado
            ├── entity/
            │   ├── Publicacion.java
            │   ├── Libro.java
            │   ├── ArticuloCientifico.java
            │   └── Autor.java
            ├── repository/
            │   ├── AutorRepository.java
            │   ├── LibroRepository.java
            │   └── ArticuloCientificoRepository.java
            ├── service/
            │   ├── AutorService.java
            │   ├── LibroService.java
            │   ├── ArticuloCientificoService.java
            │   ├── RelojProducer.java      <-- Nuevo
            │   └── NotificacionProducer.java <-- Modificado
            └── listener/                  <-- Nuevo paquete
                └── RelojListener.java     <-- Vacío por ahora (opcional)
```
### Orden de creación de los archivos
1. **DTO: Enviar Hora del Cliente:** HoraClienteDto.java
```
package publicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la hora local de este microservicio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoraClienteDto {
    private String nombreNodo; // Nombre del microservicio
    private long horaEnviada;  // Tiempo actual en milisegundos
}
```
* Define cómo se estructura un mensaje de tiempo enviado al servidor de sincronización.
* Se serializa como JSON antes de enviarse por RabbitMQ.
2. **Productor: Envía Mensaje de Hora al Servidor Centralizado:** /service/RelojProducer.java
```
package publicacion.service;

import publicacion.dto.HoraClienteDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RelojProducer {

    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    private final String nombreNodo;

    public RelojProducer(AmqpTemplate amqpTemplate, ObjectMapper objectMapper) {
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
        this.nombreNodo = "ms-publicaciones";
    }

    public void enviarHora() throws Exception {
        HoraClienteDto dto = new HoraClienteDto(nombreNodo, Instant.now().toEpochMilli());
        String json = objectMapper.writeValueAsString(dto);
        amqpTemplate.convertAndSend("reloj.solicitud", json);
    }
}
```
* Usa AmqpTemplate para enviar mensajes a la cola reloj.solicitud.
* Serializa el objeto HoraClienteDto a JSON usando ObjectMapper.
3. **Programador de Tarea Periódica:** /config/RelojScheduler.java
```
package publicacion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import publicacion.service.RelojProducer;

@Configuration
@EnableScheduling
public class RelojScheduler {

    private final RelojProducer relojProducer;

    public RelojScheduler(RelojProducer relojProducer) {
        this.relojProducer = relojProducer;
    }

    @Scheduled(fixedRate = 10000) // Cada 10 segundos
    public void reportarHora() {
        try {
            relojProducer.enviarHora();
            System.out.println("Cliente: Hora local enviada al servidor de sincronización");
        } catch (Exception e) {
            System.err.println("Error al enviar hora local: " + e.getMessage());
        }
    }
}

```
* Programa una tarea que se ejecuta cada 10 segundos.
* Cada vez, llama a RelojProducer.enviarHora() para notificar al servidor.
4. **Configuración de Cola en RabbitMQ:** /config/RabbitMQConfig.java
```
package publicacion.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue relojSolicitudCola() {
        return QueueBuilder.durable("reloj.solicitud").build();
    }
}
```
* Define una cola durable (reloj.solicitud) para garantizar persistencia de mensajes.
* Es consumida por el microservicio ms-sincronizacion.

