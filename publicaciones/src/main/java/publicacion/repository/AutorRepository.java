package publicacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import publicacion.entity.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
