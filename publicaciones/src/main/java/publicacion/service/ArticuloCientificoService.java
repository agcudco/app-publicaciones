package publicacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicacion.dto.ArticuloCientificoDto;
import publicacion.dto.ResponseDto;
import publicacion.entity.ArticuloCientifico;
import publicacion.entity.Autor;
import publicacion.repository.ArticuloCientificoRepository;
import publicacion.repository.AutorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticuloCientificoService {

    @Autowired
    private ArticuloCientificoRepository articuloRepo;

    @Autowired
    private AutorRepository autorRepo;

    public ResponseDto crearArticulo(ArticuloCientificoDto dto) {
        Autor autor = autorRepo.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        ArticuloCientifico articulo = new ArticuloCientifico();
        articulo.setTitulo(dto.getTitulo());
        articulo.setAnioPublicacion(dto.getAnioPublicacion());
        articulo.setEditorial(dto.getEditorial());
        articulo.setIsbn(dto.getIsbn());
        articulo.setResumen(dto.getResumen());
        articulo.setAreaInvestigacion(dto.getAreaInvestigacion());
        articulo.setRevista(dto.getRevista());
        articulo.setAutor(autor);

        return new ResponseDto("Artículo creado", articuloRepo.save(articulo));
    }

    public List<ResponseDto> listarArticulos() {
        return articuloRepo.findAll().stream()
                .map(a -> new ResponseDto("Artículo: " + a.getTitulo(), a))
                .collect(Collectors.toList());
    }

    public ResponseDto obtenerArticulo(Long id) {
        ArticuloCientifico articulo = articuloRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));
        return new ResponseDto("Artículo encontrado", articulo);
    }

    public ResponseDto actualizarArticulo(Long id, ArticuloCientificoDto dto) {
        ArticuloCientifico articuloExistente = articuloRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        articuloExistente.setTitulo(dto.getTitulo());
        articuloExistente.setAnioPublicacion(dto.getAnioPublicacion());
        articuloExistente.setEditorial(dto.getEditorial());
        articuloExistente.setIsbn(dto.getIsbn());
        articuloExistente.setResumen(dto.getResumen());
        articuloExistente.setAreaInvestigacion(dto.getAreaInvestigacion());
        articuloExistente.setRevista(dto.getRevista());

        if (dto.getAutorId() != null) {
            Autor nuevoAutor = autorRepo.findById(dto.getAutorId())
                    .orElseThrow(() -> new RuntimeException("Nuevo autor no encontrado"));
            articuloExistente.setAutor(nuevoAutor);
        }

        return new ResponseDto("Artículo actualizado", articuloRepo.save(articuloExistente));
    }

    public void eliminarArticulo(Long id) {
        articuloRepo.deleteById(id);
    }
}