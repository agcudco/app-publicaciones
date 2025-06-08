package notifications.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import notifications.dto.NotificacionDto;
import notifications.service.NotificacionService;
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
