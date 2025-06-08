package publicacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import publicacion.dto.ArticuloCientificoDto;
import publicacion.dto.ResponseDto;
import publicacion.service.ArticuloCientificoService;

import java.util.List;

@RestController
@RequestMapping("/articulos")
public class ArticuloCientificoController {

    @Autowired
    private ArticuloCientificoService articuloService;

    @PostMapping
    public ResponseDto crear(@RequestBody ArticuloCientificoDto dto) {
        return articuloService.crearArticulo(dto);
    }

    @GetMapping
    public List<ResponseDto> listar() {
        return articuloService.listarArticulos();
    }

    @GetMapping("/{id}")
    public ResponseDto obtener(@PathVariable Long id) {
        return articuloService.obtenerArticulo(id);
    }

    @PutMapping("/{id}")
    public ResponseDto actualizar(@PathVariable Long id, @RequestBody ArticuloCientificoDto dto) {
        return articuloService.actualizarArticulo(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        articuloService.eliminarArticulo(id);
    }
}