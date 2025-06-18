package sincronizacion.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import sincronizacion.dto.HoraClienteDto;
import sincronizacion.service.SincronizacionService;

/**
 * Escucha mensajes de tipo 'hora' desde RabbitMQ.
 * Cada mensaje representa la hora local de un nodo cliente.
 */
@Component
public class RelojListener {

    private final SincronizacionService sincronizacionService;
    private final ObjectMapper objectMapper;

    public RelojListener(SincronizacionService sincronizacionService, ObjectMapper objectMapper) {
        this.sincronizacionService = sincronizacionService;
        this.objectMapper = objectMapper;
    }

    /**
     * Recibe un mensaje JSON desde la cola 'reloj.solicitud'
     * Lo convierte a HoraClienteDto y lo registra en el servicio
     */
    @RabbitListener(queues = "reloj.solicitud")
    public void recibirSolicitud(String mensajeJson) {
        try {
            HoraClienteDto dto = objectMapper.readValue(mensajeJson, HoraClienteDto.class);
            System.out.println("Recibido: " + dto);
            sincronizacionService.registrarTiempo(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
