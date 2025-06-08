package publicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDto {
    private String titulo;
    private int anioPublicacion;
    private String editorial;
    private String isbn;
    private String resumen;
}