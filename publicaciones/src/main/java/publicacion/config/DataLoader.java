package publicacion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import publicacion.entity.ArticuloCientifico;
import publicacion.entity.Autor;
import publicacion.entity.Libro;
import publicacion.repository.ArticuloCientificoRepository;
import publicacion.repository.AutorRepository;
import publicacion.repository.LibroRepository;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private ArticuloCientificoRepository articuloRepository;


    @Override
    public void run(String... args) throws Exception {

        // Verificar si ya hay autores cargados
        if (autorRepository.count() == 0) {

            // Crear autores de prueba
            Autor autor1 = new Autor();
            autor1.setNombre("Carlos");
            autor1.setApellido("García");
            autor1.setNacionalidad("Colombiano");
            autor1.setInstitucion("Universidad Nacional");
            autor1.setEmail("carlos@example.com");
            autor1.setBiografia("Experto en inteligencia artificial");

            Autor autor2 = new Autor();
            autor2.setNombre("María");
            autor2.setApellido("López");
            autor2.setNacionalidad("Mexicana");
            autor2.setInstitucion("UNAM");
            autor2.setEmail("maria@unam.mx");
            autor2.setBiografia("Especialista en big data");

            // Guardar autores
            autorRepository.saveAll(List.of(autor1, autor2));

            System.out.println("✅ Autores cargados: " + autorRepository.count());

            // Crear libros asociados a los autores
            Libro libro1 = new Libro();
            libro1.setTitulo("Inteligencia Artificial Aplicada");
            libro1.setAnioPublicacion(2023);
            libro1.setEditorial("Alfa Editores");
            libro1.setIsbn("978-3-16-148410-0");
            libro1.setResumen("Un libro introductorio sobre IA aplicada.");
            libro1.setGenero("Ciencia");
            libro1.setNumeroPaginas(350);
            libro1.setAutor(autor1);

            Libro libro2 = new Libro();
            libro2.setTitulo("Big Data y Análisis");
            libro2.setAnioPublicacion(2022);
            libro2.setEditorial("Beta Editorial");
            libro2.setIsbn("978-3-16-148410-1");
            libro2.setResumen("Técnicas avanzadas de análisis de datos.");
            libro2.setGenero("Tecnología");
            libro2.setNumeroPaginas(420);
            libro2.setAutor(autor2);

            libroRepository.saveAll(List.of(libro1, libro2));
            System.out.println("✅ Libros cargados: " + libroRepository.count());

            // Crear artículos científicos
            ArticuloCientifico articulo1 = new ArticuloCientifico();
            articulo1.setTitulo("IA en la Medicina");
            articulo1.setAnioPublicacion(2023);
            articulo1.setEditorial("Revista Médica");
            articulo1.setIsbn("978-3-16-148410-2");
            articulo1.setResumen("Aplicaciones de IA en diagnóstico médico.");
            articulo1.setAreaInvestigacion("Medicina");
            articulo1.setRevista("Revista Médica Latinoamericana");
            articulo1.setAutor(autor1);

            ArticuloCientifico articulo2 = new ArticuloCientifico();
            articulo2.setTitulo("Análisis de Datos Masivos");
            articulo2.setAnioPublicacion(2022);
            articulo2.setEditorial("Ciencia Informática");
            articulo2.setIsbn("978-3-16-148410-3");
            articulo2.setResumen("Métodos para procesamiento de grandes volúmenes de datos.");
            articulo2.setAreaInvestigacion("Big Data");
            articulo2.setRevista("Journal of Data Science");
            articulo2.setAutor(autor2);

            articuloRepository.saveAll(List.of(articulo1, articulo2));
            System.out.println("✅ Artículos científicos cargados: " + articuloRepository.count());
        } else {
            System.out.println("🟡 Ya existen datos en la base de datos. No se cargaron datos iniciales.");
        }
    }
}
