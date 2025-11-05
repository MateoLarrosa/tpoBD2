# Sistema de Facturación y Pagos

## 📋 Descripción General

El sistema de facturación permite generar facturas automáticas basadas en los procesos ejecutados por los usuarios, gestionar su pago y mantener un historial completo de facturación.

---

## 🏗️ Arquitectura

### **Modelos**

#### **1. Factura**
```java
- String id                          // ID único de MongoDB
- String usuarioId                   // Usuario al que pertenece
- LocalDate fechaEmision             // Fecha de creación
- List<String> procesosFacturadosIds // IDs de SolicitudProceso incluidos
- EstadoFactura estado               // PENDIENTE, PAGADA, VENCIDA
- double montoTotal                  // Suma de costos de procesos
- LocalDate fechaVencimiento         // Fecha límite de pago
```

**Métodos de negocio:**
- `agregarProcesoFacturado(String solicitudId)` - Añade un proceso a la factura
- `marcarComoPagada()` - Cambia estado a PAGADA
- `marcarComoVencida()` - Cambia estado a VENCIDA
- `estaVencida()` - Verifica si pasó la fecha de vencimiento

#### **2. Pago**
```java
- String id              // ID único de MongoDB
- String facturaId       // Factura a la que pertenece
- LocalDate fechaPago    // Fecha del pago
- double montoPagado     // Monto pagado
- String metodoPago      // Método utilizado
```

#### **3. EstadoFactura (Enum)**
```java
PENDIENTE  // Factura emitida pero no pagada
PAGADA     // Factura pagada completamente
VENCIDA    // Factura con fecha de vencimiento pasada
```

---

## 🗄️ Capa de Datos (Repositories)

### **FacturaRepository** (Singleton)
Colección MongoDB: `facturas`

**Métodos principales:**
- `save(Factura)` - Crea nueva factura
- `findById(String)` - Busca por ID
- `findByUsuarioId(String)` - Todas las facturas de un usuario
- `findByEstado(EstadoFactura)` - Filtra por estado
- `findByUsuarioIdYEstado(String, EstadoFactura)` - Combinación de ambos
- `findFacturasVencidas()` - Facturas pendientes con fecha pasada
- `findByUsuarioIdYRangoFechas(String, LocalDate, LocalDate)` - Facturas en período

### **PagoRepository** (Singleton)
Colección MongoDB: `pagos`

**Métodos principales:**
- `save(Pago)` - Registra nuevo pago
- `findById(String)` - Busca por ID
- `findByFacturaId(String)` - Obtiene el pago de una factura
- `findByMetodoPago(String)` - Filtra por método de pago
- `findByRangoFechas(LocalDate, LocalDate)` - Pagos en período
- `calcularMontoTotalPagado(LocalDate, LocalDate)` - Suma total en rango

---

## 🧠 Capa de Negocio (Services)

### **FacturaService** (Singleton)

#### **Generación de Facturas**

**1. Factura de todos los procesos completados:**
```java
Factura generarFacturaParaUsuario(String usuarioId, int diasVencimiento)
```
- Obtiene todas las solicitudes completadas del usuario
- Calcula el costo total sumando los costos de cada proceso
- Crea la factura con vencimiento en X días

**2. Factura por período:**
```java
Factura generarFacturaParaUsuarioPorPeriodo(
    String usuarioId, 
    LocalDate fechaInicio, 
    LocalDate fechaFin, 
    int diasVencimiento
)
```
- Similar al anterior pero filtra por rango de fechas
- Útil para facturación mensual/trimestral

#### **Gestión de Facturas**

```java
void marcarFacturaComoPagada(String facturaId)           // Marca como pagada
void actualizarFacturasVencidas()                        // Actualiza estados vencidos
List<Factura> obtenerFacturasPorUsuario(String)          // Todas del usuario
List<Factura> obtenerFacturasPendientesPorUsuario(String) // Solo pendientes
List<Factura> obtenerFacturasPagadasPorUsuario(String)   // Solo pagadas
List<Factura> obtenerFacturasVencidasPorUsuario(String)  // Solo vencidas
```

#### **Estadísticas y Cálculos**

```java
double calcularMontoAdeudadoPorUsuario(String usuarioId)
// Retorna suma de facturas pendientes + vencidas

double[] obtenerEstadisticasFacturacionPorUsuario(String usuarioId)
// Retorna [total, pendientes, pagadas, vencidas, montoTotal, montoAdeudado]

List<SolicitudProceso> obtenerDetalleProcesosDeLaFactura(String facturaId)
// Lista detallada de procesos incluidos en la factura
```

### **PagoService** (Singleton)

#### **Registro de Pagos**

```java
Pago registrarPago(String facturaId, double montoPagado, String metodoPago)
```
- Verifica que la factura existe
- Valida el monto
- Crea el registro de pago
- Marca la factura como PAGADA automáticamente

#### **Consultas**

```java
Pago obtenerPagoPorFactura(String facturaId)           // Pago de una factura
List<Pago> obtenerPagosPorMetodo(String metodoPago)    // Por método
List<Pago> obtenerPagosPorRangoFechas(...)             // Por período
double calcularTotalPagadoEnRango(...)                 // Suma en período
boolean facturaEstaPagada(String facturaId)            // Estado de pago
```

---

## 🎮 Capa de Presentación (Controllers & Menus)

### **FacturaController** (Singleton)
Fachada sobre FacturaService, expone todos sus métodos.

### **PagoController** (Singleton)
Fachada sobre PagoService, expone todos sus métodos.

### **MenuFacturacion** (Menú Interactivo)

**Opciones disponibles:**

1. **Generar factura mensual**
   - Pide días de vencimiento
   - Factura todos los procesos completados
   - Muestra resumen de la factura generada

2. **Ver todas mis facturas**
   - Lista completa con fechas, estados y montos

3. **Ver facturas pendientes**
   - Solo facturas sin pagar
   - Indica cuáles están vencidas
   - Muestra total pendiente

4. **Ver facturas pagadas**
   - Historial de pagos realizados
   - Incluye método y fecha de pago

5. **Ver detalle de una factura**
   - Información completa de la factura
   - Lista de procesos incluidos con costos individuales
   - Información del pago (si está pagada)

6. **Pagar una factura**
   - Lista facturas pendientes
   - Selección de método de pago:
     - Tarjeta de crédito
     - Tarjeta de débito
     - Transferencia bancaria
     - Efectivo
     - MercadoPago
   - Confirmación antes de procesar

7. **Ver estadísticas de facturación**
   - Total de facturas
   - Cantidades por estado
   - Monto total facturado
   - Monto adeudado

8. **Ver monto total adeudado**
   - Suma de facturas pendientes y vencidas
   - Alerta de facturas vencidas

---

## 🔗 Integración con el Sistema de Procesos

### **Flujo de Facturación**

```
1. Usuario ejecuta consultas (ej: mediciones por zona)
   ↓
2. MedicionesController registra SolicitudProceso automáticamente
   ↓
3. Cada SolicitudProceso tiene un costo (definido en el catálogo de Proceso)
   ↓
4. Al fin del mes, se genera una Factura agrupando todas las SolicitudProceso
   ↓
5. Usuario recibe factura con monto total y fecha de vencimiento
   ↓
6. Usuario paga la factura desde el menú
   ↓
7. Se registra el Pago y la factura cambia a estado PAGADA
```

### **Ejemplo práctico:**

```java
// Usuario ejecuta 3 consultas durante el mes:
1. Consulta mediciones por zona    → Costo: $1.50
2. Consulta mediciones por ciudad  → Costo: $2.00  
3. Alerta temperatura alta         → Costo: $3.50

// Al generar la factura mensual:
Factura {
    procesosFacturados: [solicitud1, solicitud2, solicitud3]
    montoTotal: $7.00
    estado: PENDIENTE
    fechaVencimiento: 30 días
}

// Usuario paga con tarjeta de crédito:
Pago {
    facturaId: "674..."
    montoPagado: $7.00
    metodoPago: "Tarjeta de crédito"
}

// Factura automáticamente cambia a PAGADA
```

---

## 📊 Colecciones MongoDB

### **Colección: `facturas`**
```json
{
    "_id": ObjectId("..."),
    "usuarioId": "673abc...",
    "fechaEmision": ISODate("2025-11-05"),
    "procesosFacturadosIds": ["674...", "675...", "676..."],
    "estado": "PENDIENTE",
    "montoTotal": 7.50,
    "fechaVencimiento": ISODate("2025-12-05")
}
```

### **Colección: `pagos`**
```json
{
    "_id": ObjectId("..."),
    "facturaId": "674abc...",
    "fechaPago": ISODate("2025-11-03"),
    "montoPagado": 7.50,
    "metodoPago": "Tarjeta de crédito"
}
```

---

## 🚀 Cómo Usar el Sistema

### **Desde el Menú Principal:**

1. Inicia sesión con tu usuario
2. Verás el menú con la nueva opción: **"Mis facturas y pagos"**
3. Selecciona la opción para acceder al submenu de facturación

### **Generar Factura Mensual:**

```
Mis facturas y pagos → Generar factura mensual
Días de vencimiento: 30
✓ Factura generada exitosamente
```

### **Pagar una Factura:**

```
Mis facturas y pagos → Pagar una factura
Seleccione factura: 1
Método de pago: Tarjeta de crédito
¿Confirmar? S
✓ Pago procesado exitosamente
```

---

## ⚙️ Características Avanzadas

### **1. Actualización Automática de Facturas Vencidas**

El sistema permite ejecutar un proceso que revisa todas las facturas pendientes y marca como vencidas aquellas que pasaron su fecha de vencimiento:

```java
facturaController.actualizarFacturasVencidas();
```

Esto podría ejecutarse:
- Diariamente mediante un cron job
- Al inicio de la aplicación
- Manualmente desde un menú administrativo

### **2. Facturación por Períodos**

Permite generar facturas específicas por mes/trimestre:

```java
// Factura de octubre 2025
facturaController.generarFacturaPorPeriodo(
    usuarioId, 
    LocalDate.of(2025, 10, 1), 
    LocalDate.of(2025, 10, 31),
    30
);
```

### **3. Estadísticas de Pagos**

Los administradores pueden ver estadísticas de métodos de pago más usados:

```java
pagoController.mostrarEstadisticasPorMetodo();
```

Salida:
```
Tarjeta de crédito: 45 pagos - Total: $1,234.50
Tarjeta de débito: 23 pagos - Total: $567.80
Transferencia: 15 pagos - Total: $890.00
...
```

---

## 🔐 Patrón Singleton

Todos los componentes siguen el patrón Singleton para mantener consistencia con el resto del sistema:

- ✅ FacturaRepository
- ✅ PagoRepository  
- ✅ FacturaService
- ✅ PagoService
- ✅ FacturaController
- ✅ PagoController

---

## 🧪 Testing Manual

### **Escenario 1: Ciclo Completo de Facturación**

1. Login como usuario
2. Ejecutar 3-5 consultas de mediciones
3. Ir a "Mis facturas y pagos"
4. Generar factura mensual (30 días vencimiento)
5. Verificar que aparece en "Facturas pendientes"
6. Ver detalle de la factura
7. Pagar la factura con tarjeta de crédito
8. Verificar que aparece en "Facturas pagadas"

### **Escenario 2: Verificación de Deudas**

1. Generar 2-3 facturas sin pagar
2. Ver "Monto total adeudado"
3. Ver "Estadísticas de facturación"
4. Pagar una factura
5. Verificar que el monto adeudado disminuye

---

## 📝 Notas Importantes

### **Relación con Procesos**

Las facturas se generan **basándose en los procesos completados**. No incluyen:
- Procesos pendientes
- Procesos con error
- Procesos sin ejecutar

### **Unicidad de Pagos**

Una factura puede tener **solo un pago asociado**. Si se requiere pagos parciales, se debería extender el modelo.

### **Métodos de Pago**

Los métodos están hardcodeados en el menú. Para un sistema productivo, deberían obtenerse de una configuración o catálogo.

---

## 🎯 Resumen de Archivos Creados/Modificados

### **Modelos:**
- ✅ `modelo/Factura.java` - Completado con todos los campos
- ✅ `modelo/Pago.java` - Completado con todos los campos
- ✅ `modelo/EstadoFactura.java` - Ya existía, sin cambios

### **Repositories:**
- ✅ `repositories/FacturaRepository.java` - Nuevo
- ✅ `repositories/PagoRepository.java` - Nuevo

### **Services:**
- ✅ `services/FacturaService.java` - Nuevo
- ✅ `services/PagoService.java` - Nuevo

### **Controllers:**
- ✅ `controladores/FacturaController.java` - Nuevo
- ✅ `controladores/PagoController.java` - Nuevo

### **Menus:**
- ✅ `menus/MenuFacturacion.java` - Nuevo
- ✅ `menus/MenuUsuario.java` - Modificado (añadida opción)

---

## ✅ Sistema Completo

El sistema de facturación está **100% integrado** con el sistema de procesos y listo para usar. Mantiene el patrón arquitectónico del proyecto (MVC + Singleton) y se integra naturalmente con los menús existentes.
