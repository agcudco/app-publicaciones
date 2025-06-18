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