package sincronizacion.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import sincronizacion.dto.HoraClienteDto;
import sincronizacion.service.SincronizacionService;

@Component
public class RelojListener {

    private final SincronizacionService sincronizacionService;
    private final ObjectMapper objectMapper;

    public RelojListener(SincronizacionService sincronizacionService, ObjectMapper objectMapper) {
        this.sincronizacionService = sincronizacionService;
        this.objectMapper = objectMapper;
    }

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
