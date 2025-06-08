package publicacion.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Data
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String institucion;
    private String email;
    private String biografia;

    @OneToMany(mappedBy = "autor")
    @JsonManagedReference // Parte principal de la relación
    private List<Libro> libros;

    @OneToMany(mappedBy = "autor")
    @JsonManagedReference
    private List<ArticuloCientifico> articulos;
}
