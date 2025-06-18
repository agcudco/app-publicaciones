package sincronizacion.service;

import org.springframework.stereotype.Service;
import sincronizacion.dto.HoraClienteDto;
import sincronizacion.dto.HoraServidorDto;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio encargado de recibir las horas de los clientes,
 * calcular el promedio y enviar la hora corregida.
 */

@Service
public class SincronizacionService {

    // Almacena temporalmente las horas recibidas de los nodos
    private final Map<String, Long> tiemposClientes = new ConcurrentHashMap<>();

    private static final int INTERVALO_SEGUNDOS = 10;

    /**
     * Registra la hora local de un nodo cliente
     */
    public void registrarTiempo(HoraClienteDto dto) {
        tiemposClientes.put(dto.getNombreNodo(), dto.getHoraEnviada());
    }

    /**
     * Calcula la hora promedio entre todos los nodos registrados
     * y envía la hora corregida a través de RabbitMQ
     */
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

    /**
     * Simula el envío de la hora corregida a los nodos
     */
    private void enviarAjusteRelojes(long horaServidor) {
        // Simular envío a través de RabbitMQ
        System.out.println("Servidor envió hora sincronizada: " + new Date(horaServidor));
    }

    /**
     * Devuelve la hora actual del servidor (útil para REST API)
     */
    public HoraServidorDto getHoraServidor() {
        long ahora = Instant.now().toEpochMilli();
        return new HoraServidorDto(ahora, new HashMap<>());
    }
}

