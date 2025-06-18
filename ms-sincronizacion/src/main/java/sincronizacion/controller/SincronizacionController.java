package sincronizacion.controller;

import org.springframework.web.bind.annotation.*;
import sincronizacion.dto.HoraServidorDto;
import sincronizacion.service.SincronizacionService;

@RestController
@RequestMapping("/sincronizacion")
public class SincronizacionController {

    private final SincronizacionService sincronizacionService;

    public SincronizacionController(SincronizacionService sincronizacionService) {
        this.sincronizacionService = sincronizacionService;
    }

    /**
     * Endpoint para obtener la hora actual del servidor (sin sincronizar)
     */
    @GetMapping("/hora")
    public HoraServidorDto getHoraServidor() {
        return sincronizacionService.getHoraServidor();
    }
}