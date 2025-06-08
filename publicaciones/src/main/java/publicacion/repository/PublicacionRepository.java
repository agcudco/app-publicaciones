package publicacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import publicacion.entity.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
}
