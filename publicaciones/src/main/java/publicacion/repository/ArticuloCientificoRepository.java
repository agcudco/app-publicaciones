package publicacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import publicacion.entity.ArticuloCientifico;

public interface ArticuloCientificoRepository extends JpaRepository<ArticuloCientifico, Long> {
}
