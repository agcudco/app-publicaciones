package publicacion.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicacion.dto.NotificacionDto;

@Service
public class NotificacionProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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
