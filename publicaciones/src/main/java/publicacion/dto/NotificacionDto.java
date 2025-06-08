package publicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDto {
    private String mensaje;
    private String tipo; // Ej: "libro", "articulo"
}
