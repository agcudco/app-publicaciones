package notifications.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue notificacionesCola() {
        return QueueBuilder.durable("notificaciones.cola").build();
    }
}
