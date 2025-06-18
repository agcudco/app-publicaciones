package sincronizacion.service;

import org.springframework.stereotype.Service;
import sincronizacion.dto.HoraClienteDto;
import sincronizacion.dto.HoraServidorDto;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SincronizacionService {

    private final Map<String, Long> tiemposClientes = new ConcurrentHashMap<>();
    private static final int INTERVALO_SEGUNDOS = 10;

    public void registrarTiempo(HoraClienteDto dto) {
        tiemposClientes.put(dto.getNombreNodo(), dto.getHoraEnviada());
    }

    public void sincronizarRelojes() {
        if (tiemposClientes.size() >= 2) {
            long ahora = Instant.now().toEpochMilli();

            // Calcular promedio
            long promedio = (ahora + tiemposClientes.values().stream().mapToLong(Long::longValue).sum()) /
                    (tiemposClientes.size() + 1);

            tiemposClientes.clear();
            enviarAjusteRelojes(promedio);
        }
    }

    private void enviarAjusteRelojes(long horaServidor) {
        // Simular envío a través de RabbitMQ
        System.out.println("Servidor envió hora sincronizada: " + new Date(horaServidor));
    }

    public HoraServidorDto getHoraServidor() {
        long ahora = Instant.now().toEpochMilli();
        return new HoraServidorDto(ahora, new HashMap<>());
    }
}

