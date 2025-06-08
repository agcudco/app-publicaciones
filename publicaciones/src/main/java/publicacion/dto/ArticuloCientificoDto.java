package publicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloCientificoDto {
    private String titulo;
    private int anioPublicacion;
    private String editorial;
    private String isbn;
    private String resumen;
    private String areaInvestigacion;
    private String revista;
    private Long autorId;
}