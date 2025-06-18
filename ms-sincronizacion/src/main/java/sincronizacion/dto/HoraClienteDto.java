package sincronizacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoraClienteDto {
    private String nombreNodo; // Nombre del microservicio
    private long horaEnviada; // Tiempo del cliente en ms
}