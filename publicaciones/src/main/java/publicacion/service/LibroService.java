package publicacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicacion.dto.LibroDto;
import publicacion.dto.ResponseDto;
import publicacion.entity.Autor;
import publicacion.entity.Libro;
import publicacion.repository.AutorRepository;
import publicacion.repository.LibroRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private NotificacionProducer notificacionProducer;

    public ResponseDto crearLibro(LibroDto dto) {
        Autor autor = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAnioPublicacion(dto.getAnioPublicacion());
        libro.setEditorial(dto.getEditorial());
        libro.setIsbn(dto.getIsbn());
        libro.setResumen(dto.getResumen());
        libro.setGenero(dto.getGenero());
        libro.setNumeroPaginas(dto.getNumeroPaginas());
        libro.setAutor(autor);

        Libro guardado = libroRepository.save(libro);
        // Enviar notificación
        notificacionProducer.enviarNotificacion(
                "Nuevo libro creado: " + libro.getTitulo(),
                "libro"
        );

        return new ResponseDto("Libro creado", guardado);
    }

    public List<ResponseDto> listarLibros() {
        return libroRepository.findAll().stream()
                .map(libro -> new ResponseDto("Libro: " + libro.getTitulo(), libro))
                .collect(Collectors.toList());
    }

    public ResponseDto obtenerLibroPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        return new ResponseDto("Libro encontrado", libro);
    }

    public ResponseDto actualizarLibro(Long id, LibroDto dto) {
        Libro libroExistente = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        libroExistente.setTitulo(dto.getTitulo());
        libroExistente.setAnioPublicacion(dto.getAnioPublicacion());
        libroExistente.setEditorial(dto.getEditorial());
        libroExistente.setIsbn(dto.getIsbn());
        libroExistente.setResumen(dto.getResumen());
        libroExistente.setGenero(dto.getGenero());
        libroExistente.setNumeroPaginas(dto.getNumeroPaginas());

        if (dto.getAutorId() != null) {
            Autor nuevoAutor = autorRepository.findById(dto.getAutorId())
                    .orElseThrow(() -> new RuntimeException("Nuevo autor no encontrado"));
            libroExistente.setAutor(nuevoAutor);
        }

        return new ResponseDto("Libro actualizado", libroRepository.save(libroExistente));
    }

    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }
}