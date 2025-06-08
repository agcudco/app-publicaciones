# `@Inheritance(strategy = InheritanceType.JOINED)` en JPA

La anotación `@Inheritance(strategy = InheritanceType.JOINED)` es una característica de **JPA (Java Persistence API)** que se utiliza para configurar cómo se maneja la **herencia entre entidades** en el contexto de la persistencia en bases de datos relacionales.

## ¿Qué significa?

Esta anotación le indica a JPA que utilice la **estrategia JOINED** para mapear una jerarquía de herencia de clases en la base de datos.

## Estrategia JOINED

Con esta estrategia:

- **Se crea una tabla por cada clase** en la jerarquía de herencia.
- La **tabla de la clase padre** contiene:
    - Las columnas comunes a todas las subclases.
    - Su clave primaria.
- Las **tablas de las subclases** contienen:
    - Sus propias columnas específicas.
    - Su clave primaria, que también actúa como **clave externa (foreign key)** apuntando a la tabla del padre.
- Para obtener una instancia de una subclase, **JPA realiza un JOIN** entre las tablas correspondientes del padre y la subclase.

**Ejemplo**
```
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {
    @Id
    private Long id;
    private String nombre;
    // otros campos y métodos
}

@Entity
public class Empleado extends Persona {
    private String puesto;
    // getters y setters
}

@Entity
public class Cliente extends Persona {
    private String tipoCliente;
    // getters y setters
}
```

### Estructura en la base de datos

Con la estrategia `JOINED`, se crearían **tres tablas** en la base de datos:

#### Tabla `Persona`
- `id` (PK)
- `nombre`

#### Tabla `Empleado`
- `id` (PK y FK hacia `Persona.id`)
- `puesto`

#### Tabla `Cliente`
- `id` (PK y FK hacia `Persona.id`)
- `tipoCliente`

## Ventajas

- **Flexibilidad**:  
  Cada subclase puede tener sus propios campos sin afectar a otras.

- **Normalización**:  
  Los campos comunes están únicamente en la tabla padre, lo que evita duplicados y mejora la integridad de los datos.

## Desventajas

- **Complejidad en consultas**:  
  Se requieren operaciones `JOIN` para obtener todos los datos de una subclase, lo que puede complicar las consultas.

- **Rendimiento**:  
  El uso frecuente de `JOIN`s puede impactar negativamente el rendimiento, especialmente con grandes volúmenes de datos o jerarquías complejas.

### Guía con Qwen
https://chat.qwen.ai/s/62889b64-592e-4120-8f58-9081e23131a5?fev=0.0.93