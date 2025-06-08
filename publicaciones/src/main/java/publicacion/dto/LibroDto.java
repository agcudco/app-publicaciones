package publicacion.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDto {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @Min(value = 1800, message = "El año debe ser mayor a 1800")
    @Max(value = 2100, message = "El año debe ser menor a 2100")
    private int anioPublicacion;

    @NotBlank(message = "La editorial es obligatoria")
    private String editorial;

    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    @NotBlank(message = "El resumen es obligatorio")
    private String resumen;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    @Positive(message = "El número de páginas debe ser positivo")
    private int numeroPaginas;

    @NotNull(message = "Se requiere el ID del autor")
    private Long autorId;
}