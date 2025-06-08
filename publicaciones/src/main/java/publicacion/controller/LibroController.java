package publicacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import publicacion.dto.LibroDto;
import publicacion.dto.ResponseDto;
import publicacion.service.LibroService;

import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @PostMapping
    public ResponseDto crear(@RequestBody LibroDto dto) {
        return libroService.crearLibro(dto);
    }

    @GetMapping
    public List<ResponseDto> listar() {
        return libroService.listarLibros();
    }

    @GetMapping("/{id}")
    public ResponseDto obtener(@PathVariable Long id) {
        return libroService.obtenerLibroPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseDto actualizar(@PathVariable Long id, @RequestBody LibroDto dto) {
        return libroService.actualizarLibro(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        libroService.eliminarLibro(id);
    }
}
