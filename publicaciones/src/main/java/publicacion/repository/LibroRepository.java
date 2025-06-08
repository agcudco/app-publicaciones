package publicacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import publicacion.entity.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
