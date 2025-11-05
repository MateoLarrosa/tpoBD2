# Sistema de Registro de Procesos y Solicitudes

## Descripción General

Este módulo implementa un sistema completo de registro y auditoría de procesos para el trabajo práctico de Bases de Datos II. Cada vez que un usuario realiza una consulta o operación en el sistema, se registra automáticamente en MongoDB para:

- **Auditoría**: Saber qué consultas hace cada usuario
- **Facturación**: Base para cobrar por servicios (cada consulta tiene un costo asociado)
- **Estadísticas**: Tipos de consultas más usadas, usuarios más activos
- **Historial**: Cumplir con requisitos de "Historial de Ejecución" del TP

## Arquitectura

### Entidades Principales

#### 1. **Proceso** (Catálogo de Servicios)
Representa un servicio disponible en el sistema.

**Atributos:**
- `id`: Identificador único
- `nombre`: Nombre del proceso (ej: "CONSULTA_MEDICIONES_ZONA")
- `descripcion`: Descripción detallada
- `tipoProceso`: Tipo (CONSULTA, INFORME, GESTION, MENSAJERIA, AUTENTICACION)
- `costo`: Precio por usar ese proceso

**Ejemplos:**
```
- CONSULTA_MEDICIONES_ZONA: Costo $10
- INFORME_MAXIMAS_MINIMAS: Costo $50
- REGISTRAR_MEDICION: Costo $20
```

#### 2. **SolicitudProceso** (Ejecución de Servicio)
Representa cada vez que un usuario ejecuta un proceso.

**Atributos:**
- `id`: Identificador único
- `usuarioId`: Usuario que realizó la solicitud
- `procesoId`: Proceso ejecutado (referencia al catálogo)
- `fechaSolicitud`: Cuándo se solicitó
- `estado`: PENDIENTE, COMPLETADO, ERROR
- `parametros`: Datos específicos (Map<String, Object>)
- `resultado`: Descripción del resultado
- `tiempoEjecucionMs`: Duración de la operación
- `fechaCompletado`: Cuándo se completó

#### 3. **EstadoSolicitud** (Enum)
- `PENDIENTE`: Solicitud creada pero no completada
- `COMPLETADO`: Ejecutada correctamente
- `ERROR`: Falló la ejecución

## Componentes del Sistema

### Capa de Datos (Repositories)
- **ProcesoRepository**: Gestiona el catálogo de procesos en MongoDB
- **SolicitudProcesoRepository**: Gestiona las solicitudes de usuarios en MongoDB

### Capa de Negocio (Services)
- **ProcesoService**: 
  - Gestiona el catálogo de procesos
  - Inicializa procesos predefinidos
  - Patrón Singleton

- **SolicitudProcesoService**:
  - Crea y actualiza solicitudes
  - Calcula costos totales
  - Genera estadísticas
  - Patrón Singleton

### Capa de Presentación (Controllers)
- **ProcesoController**: Interfaz para gestionar procesos
- **SolicitudProcesoController**: Interfaz para gestionar solicitudes

### Integración
- **MedicionesController**: Registra automáticamente cada consulta de mediciones

## Uso del Sistema

### 1. Inicialización del Catálogo

Al iniciar la aplicación por primera vez:

```java
ProcesoController procesoController = ProcesoController.getInstance();
procesoController.inicializarCatalogo();
```

Esto crea los siguientes procesos predefinidos:
- CONSULTA_MEDICIONES_ZONA ($10)
- CONSULTA_MEDICIONES_PAIS ($10)
- CONSULTA_MEDICIONES_CIUDAD ($10)
- INFORME_MAXIMAS_MINIMAS ($50)
- INFORME_PROMEDIOS ($50)
- INFORME_ALERTAS ($40)
- REGISTRAR_MEDICION ($20)
- ALTA_SENSOR ($15)
- MODIFICAR_SENSOR ($12)
- BAJA_SENSOR ($10)
- CONSULTA_SENSORES ($5)
- ENVIAR_MENSAJE ($8)
- LEER_MENSAJES ($5)
- CREAR_USUARIO ($15)
- INICIAR_SESION ($5)

### 2. Registro Automático en Consultas

Cuando un usuario realiza una consulta, se registra automáticamente:

```java
// Establecer el usuario actual
MedicionesController controller = MedicionesController.getInstance();
controller.setUsuarioActual(usuarioId);

// La consulta se registra automáticamente
List<MedicionesPorZona> mediciones = controller.obtenerMedicionesPorZonaYRango(
    "ZONA_SUR", "TEMPERATURA", fechaInicio, fechaFin
);
```

El sistema internamente:
1. Captura el tiempo de inicio
2. Ejecuta la consulta
3. Crea una SolicitudProceso con estado COMPLETADO
4. Registra parámetros, resultado y tiempo de ejecución

### 3. Consultar Historial de un Usuario

```java
SolicitudProcesoController solicitudController = SolicitudProcesoController.getInstance();

// Todas las solicitudes del usuario
List<SolicitudProceso> solicitudes = solicitudController.obtenerSolicitudesPorUsuario(usuarioId);

// Solo las completadas
List<SolicitudProceso> completadas = solicitudController.obtenerSolicitudesPorUsuarioYEstado(
    usuarioId, EstadoSolicitud.COMPLETADO
);

// Por rango de fechas
List<SolicitudProceso> enRango = solicitudController.obtenerSolicitudesPorUsuarioYRango(
    usuarioId, fechaInicio, fechaFin
);
```

### 4. Calcular Costos

```java
// Costo total de todas las solicitudes completadas
int costoTotal = solicitudController.calcularCostoTotalPorUsuario(usuarioId);

// Costo en un rango de fechas
int costoPeriodo = solicitudController.calcularCostoTotalPorUsuarioYRango(
    usuarioId, fechaInicio, fechaFin
);
```

### 5. Estadísticas

```java
// Retorna [total, completadas, pendientes, errores]
int[] stats = solicitudController.obtenerEstadisticasPorUsuario(usuarioId);

System.out.println("Total: " + stats[0]);
System.out.println("Completadas: " + stats[1]);
System.out.println("Pendientes: " + stats[2]);
System.out.println("Errores: " + stats[3]);
```

### 6. Usar el Menú Interactivo

```java
Scanner scanner = new Scanner(System.in);
MenuProcesos menuProcesos = new MenuProcesos(scanner);
menuProcesos.mostrar(usuarioId);
```

El menú permite:
- Ver catálogo de procesos disponibles
- Ver todas mis solicitudes
- Ver solicitudes pendientes/completadas
- Ver estadísticas de uso
- Calcular costos totales
- Consultar historial por fechas

## Flujo de Datos

```
Usuario hace consulta
    ↓
MedicionesController.setUsuarioActual(usuarioId)
    ↓
Controller ejecuta método (ej: obtenerMedicionesPorZona)
    ↓
Se captura tiempo de inicio
    ↓
Se ejecuta la operación real
    ↓
Se registra la solicitud automáticamente:
    - Busca el Proceso en el catálogo
    - Crea SolicitudProceso con parámetros
    - Guarda en MongoDB
    ↓
Se retorna el resultado al usuario
```

## Base de Datos

### Colección: `procesos`
Almacena el catálogo de procesos disponibles.

```json
{
  "_id": ObjectId("..."),
  "nombre": "CONSULTA_MEDICIONES_ZONA",
  "descripcion": "Consulta de mediciones por zona geográfica",
  "tipoProceso": "CONSULTA",
  "costo": 10
}
```

### Colección: `solicitudes_proceso`
Almacena cada ejecución de proceso.

```json
{
  "_id": ObjectId("..."),
  "usuarioId": "673a1b2c3d4e5f6g7h8i9j0k",
  "procesoId": "673b2c3d4e5f6g7h8i9j0k1l",
  "fechaSolicitud": ISODate("2025-11-05T10:30:00Z"),
  "estado": "COMPLETADO",
  "parametros": {
    "zona": "ZONA_SUR",
    "tipo": "TEMPERATURA",
    "fechaInicio": ISODate("2025-01-01"),
    "fechaFin": ISODate("2025-12-31")
  },
  "resultado": "ÉXITO - 150 registros",
  "tiempoEjecucionMs": 234,
  "fechaCompletado": ISODate("2025-11-05T10:30:00.234Z")
}
```

## Integración con Facturación

El sistema está preparado para integrarse con el módulo de facturación:

```java
// Obtener costo del mes actual para un usuario
LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
LocalDateTime finMes = LocalDateTime.now().withDayOfMonth(1).plusMonths(1).minusDays(1).withHour(23).withMinute(59);

int costoMes = solicitudController.calcularCostoTotalPorUsuarioYRango(
    usuarioId, inicioMes, finMes
);

// Este costo se puede usar para:
// 1. Crear una Factura
// 2. Debitar de la Cuenta Corriente
// 3. Generar reportes de consumo
```

## Patrones de Diseño Utilizados

1. **Singleton**: Todos los Services, Repositories y Controllers
2. **Repository Pattern**: Separación de lógica de datos
3. **Service Layer**: Lógica de negocio separada de presentación
4. **DTO/Model**: Objetos de dominio bien definidos
5. **Dependency Injection**: A través de getInstance()

## Ventajas del Sistema

✅ **Auditoría completa**: Cada operación queda registrada
✅ **Trazabilidad**: Saber quién, qué, cuándo y cómo
✅ **Facturación automática**: Base para cobro por uso
✅ **Estadísticas**: Analizar comportamiento de usuarios
✅ **Performance tracking**: Tiempo de ejecución de consultas
✅ **Error tracking**: Registra también los errores
✅ **Escalable**: Fácil agregar nuevos tipos de procesos
✅ **Mantenible**: Código limpio y bien estructurado

## Extensiones Futuras

- Agregar límites de uso por usuario/rol
- Implementar caché de procesos frecuentes
- Generar reportes automáticos de uso
- Alertas por uso excesivo
- Optimización de consultas lentas
- Dashboard de estadísticas en tiempo real
