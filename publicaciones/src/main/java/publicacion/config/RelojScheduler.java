package publicacion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import publicacion.service.RelojProducer;

@Configuration
@EnableScheduling
public class RelojScheduler {

    private final RelojProducer relojProducer;

    public RelojScheduler(RelojProducer relojProducer) {
        this.relojProducer = relojProducer;
    }

    @Scheduled(fixedRate = 10000) // Cada 10 segundos
    public void reportarHora() {
        try {
            relojProducer.enviarHora();
            System.out.println("Cliente: Hora local enviada al servidor de sincronización");
        } catch (Exception e) {
            System.err.println("Error al enviar hora local: " + e.getMessage());
        }
    }
}
