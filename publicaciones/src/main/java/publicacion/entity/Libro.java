package publicacion.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Libro extends Publicacion{
    private String genero;
    private int numeroPaginas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    @JsonBackReference // Parte inversa, evita la serialización recursiva
    private Autor autor;
}
