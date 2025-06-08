package publicacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicacion.dto.AutorDto;
import publicacion.dto.ResponseDto;
import publicacion.entity.Autor;
import publicacion.repository.AutorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    // ✅ Crear un nuevo autor
    public ResponseDto crearAutor(AutorDto dto) {
        Autor autor = new Autor();
        autor.setNombre(dto.getNombre());
        autor.setApellido(dto.getApellido());
        autor.setNacionalidad(dto.getNacionalidad());
        autor.setInstitucion(dto.getInstitucion());
        autor.setEmail(dto.getEmail());
        autor.setBiografia(dto.getBiografia());

        Autor guardado = autorRepository.save(autor);
        return new ResponseDto("Autor creado exitosamente", guardado);
    }

    // ✅ Obtener todos los autores
    public List<ResponseDto> listarAutores() {
        return autorRepository.findAll().stream()
                .map(autor -> new ResponseDto("Autor: " + autor.getNombre(), autor))
                .collect(Collectors.toList());
    }

    // ✅ Obtener un autor por ID
    public ResponseDto obtenerAutorPorId(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + id));
        return new ResponseDto("Autor encontrado", autor);
    }

    // ✅ Actualizar un autor
    public ResponseDto actualizarAutor(Long id, AutorDto dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        autor.setNombre(dto.getNombre());
        autor.setApellido(dto.getApellido());
        autor.setNacionalidad(dto.getNacionalidad());
        autor.setInstitucion(dto.getInstitucion());
        autor.setEmail(dto.getEmail());
        autor.setBiografia(dto.getBiografia());

        return new ResponseDto("Autor actualizado", autorRepository.save(autor));
    }

    // ✅ Eliminar un autor
    public void eliminarAutor(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new RuntimeException("Autor no encontrado");
        }
        autorRepository.deleteById(id);
    }
}
