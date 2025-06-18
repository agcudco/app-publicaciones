package publicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoraClienteDto {
    private String nombreNodo; // Ej: "ms-publicaciones"
    private long horaEnviada;  // En milisegundos
}
