package sincronizacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoraServidorDto {
    private long horaServidor;              // Hora promedio o ajustada
    private Map<String, Long> diferencias;  // Diferencia entre hora original y ajustada por nodo
}