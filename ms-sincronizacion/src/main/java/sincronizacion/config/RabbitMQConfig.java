package sincronizacion.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de colas de RabbitMQ usadas por este microservicio
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Cola donde llegan solicitudes de tiempo de otros microservicios
     */
    @Bean
    public Queue relojSolicitudCola() {
        return QueueBuilder.durable("reloj.solicitud").build();
    }
}