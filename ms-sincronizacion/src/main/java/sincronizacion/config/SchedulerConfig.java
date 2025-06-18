package sincronizacion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import sincronizacion.service.SincronizacionService;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final SincronizacionService sincronizacionService;

    public SchedulerConfig(SincronizacionService sincronizacionService) {
        this.sincronizacionService = sincronizacionService;
    }

    @Scheduled(fixedRateString = "#{T(java.util.concurrent.TimeUnit).SECONDS.toMillis(${reloj.intervalo:10})}")
    public void ejecutarSincronizacion() {
        System.out.println("Ejecutando sincronización...");
        sincronizacionService.sincronizarRelojes();
    }
}