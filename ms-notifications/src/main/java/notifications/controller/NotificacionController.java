package notifications.controller;

import notifications.dto.NotificacionDto;
import notifications.entity.Notificacion;
import notifications.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {
    @Autowired
    private  NotificacionService notificacionService;

    @GetMapping
    public List<Notificacion> listar() {
        return notificacionService.obtenerTodas();
    }

}
