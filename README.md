# 🏦 Sistema Bancario - Microservicios

Sistema completo de gestión bancaria implementado con arquitectura de microservicios en Spring Boot.

## 📋 Descripción

Este proyecto implementa un sistema bancario con dos microservicios independientes:

1. **Cliente-Persona Service** (Puerto 8080)
   - Gestión de Personas
   - CRUD de Clientes
   - Validación de identidades únicas

2. **Cuenta-Movimiento Service** (Puerto 8081)
   - Gestión de Cuentas Bancarias
   - Registro de Movimientos (Depósitos/Retiros)
   - Validación de Saldo
   - Reportes de Estado de Cuenta

## 🎯 Funcionalidades Implementadas

- **F1**: CRUD completo (Clientes, Cuentas, Movimientos)
- **F2**: Registro de movimientos con actualización de saldo
- **F3**: Validación de saldo disponible (Saldo no disponible)
- **F4**: Reportes de estado de cuenta por rango de fechas
- **F5**: Pruebas unitarias implementadas
- **F6**: Pruebas de integración
- **F7**: Despliegue en Docker con Docker Compose

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **MySQL 8.0**
- **RabbitMQ 3.12** (para comunicación asincrónica)
- **Maven 3.9**
- **Docker & Docker Compose**

## 📁 Estructura del Proyecto

```
sistema-bancario-microservicios/
├── cliente-persona-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/banksystem/cliente/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Persona.java
│   │   │   │   │   └── Cliente.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── ClienteRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   └── ClienteService.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── ClienteController.java
│   │   │   │   ├── exception/
│   │   │   │   └── ClientePersonaServiceApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── ClienteServiceTest.java
│   └── pom.xml
│
├── cuenta-movimiento-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/banksystem/cuenta/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Cuenta.java
│   │   │   │   │   └── Movimiento.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── CuentaRepository.java
│   │   │   │   │   └── MovimientoRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── CuentaService.java
│   │   │   │   │   └── MovimientoService.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── CuentaController.java
│   │   │   │   │   ├── MovimientoController.java
│   │   │   │   │   └── ReporteController.java
│   │   │   │   ├── exception/
│   │   │   │   └── CuentaMovimientoServiceApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
├── BaseDatos.sql          # Script SQL con datos de prueba
├── docker-compose.yml     # Configuración Docker
├── Dockerfile.cliente     # Dockerfile para cliente service
├── Dockerfile.cuenta      # Dockerfile para cuenta service
├── .gitignore
└── README.md
```

## 🚀 Guía de Instalación

### Requisitos Previos

- Docker y Docker Compose instalados
- Java 17+
- Maven 3.9+
- Git

### Instalación Local

1. **Clonar el repositorio**
```bash
git clone https://github.com/sanfernandodev/sistema-bancario-microservicios
cd sistema-bancario-microservicios
```

2. **Compilar ambos microservicios**
```bash
cd cliente-persona-service
mvn clean package

cd ../cuenta-movimiento-service
mvn clean package
```

3. **Ejecutar con Docker Compose**
```bash
cd ..
docker-compose up -d
```

Esto levantará:
- MySQL en puerto 3306
- RabbitMQ en puerto 5672 (UI en 15672)
- Cliente-Persona Service en puerto 8080
- Cuenta-Movimiento Service en puerto 8081

## 📡 API Endpoints

### Cliente-Persona Service (8080)

```
GET    /api/clientes                          - Obtener todos los clientes
GET    /api/clientes/activos                  - Obtener clientes activos
GET    /api/clientes/{id}                     - Obtener por ID
GET    /api/clientes/identificacion/{id}     - Obtener por identificación
GET    /api/clientes/numero/{numero}          - Obtener por número de cliente
GET    /api/clientes/buscar?nombre=X          - Buscar por nombre
POST   /api/clientes                          - Crear cliente
PUT    /api/clientes/{id}                     - Actualizar cliente
PATCH  /api/clientes/{id}/estado              - Cambiar estado
DELETE /api/clientes/{id}                     - Eliminar cliente
```

### Cuenta-Movimiento Service (8081)

```
GET    /api/cuentas                           - Obtener todas las cuentas
GET    /api/cuentas/activas                   - Obtener cuentas activas
GET    /api/cuentas/{id}                      - Obtener por ID
GET    /api/cuentas/numero/{numero}           - Obtener por número
GET    /api/cuentas/cliente/{clienteId}       - Obtener cuentas de cliente
POST   /api/cuentas                           - Crear cuenta
PUT    /api/cuentas/{id}                      - Actualizar cuenta
PATCH  /api/cuentas/{id}/estado               - Cambiar estado
DELETE /api/cuentas/{id}                      - Eliminar cuenta

GET    /api/movimientos                       - Obtener todos los movimientos
GET    /api/movimientos/cuenta/{id}           - Movimientos de una cuenta
GET    /api/movimientos/cuenta/{id}/fechas    - Por rango de fechas
GET    /api/movimientos/{id}                  - Obtener por ID
POST   /api/movimientos/registrar             - Registrar movimiento
GET    /api/movimientos/tipo/{id}             - Por tipo de movimiento

GET    /api/reportes/estado-cuenta            - Estado de cuenta (F4)
       ?clienteId=X&fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD
```

## 📊 Ejemplos de Uso

### Crear un Cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "genero": "M",
    "edad": 35,
    "identificacion": "1234567890",
    "direccion": "Calle Principal 123",
    "telefono": "0987654321",
    "contrasena": "Segura123"
  }'
```

### Crear una Cuenta

```bash
curl -X POST http://localhost:8081/api/cuentas \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "478758",
    "tipoCuenta": "Ahorros",
    "saldoInicial": 2000,
    "clienteId": 1
  }'
```

### Registrar un Movimiento

```bash
curl -X POST "http://localhost:8081/api/movimientos/registrar?cuentaId=1&tipoMovimiento=Deposito&valor=500"
```

### Obtener Reporte de Estado de Cuenta

```bash
curl -X GET "http://localhost:8081/api/reportes/estado-cuenta?clienteId=1&fechaInicio=2024-01-01&fechaFin=2024-12-31"
```

## 🧪 Pruebas

### Ejecutar Pruebas Unitarias

```bash
cd cliente-persona-service
mvn test

cd ../cuenta-movimiento-service
mvn test
```

### Importar en Postman

- Importa la colección Postman (archivo JSON) incluido en el proyecto
- Los endpoints ya estarán pre-configurados

## 🐳 Comandos Docker Útiles

```bash
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f cliente-persona-service
docker-compose logs -f cuenta-movimiento-service

# Detener servicios
docker-compose down

# Eliminar volúmenes (cuidado: elimina base de datos)
docker-compose down -v

# Reconstruir imágenes
docker-compose build --no-cache
```

## 📈 Consideraciones de Arquitectura

### Escalabilidad
- Los microservicios pueden escalarse independientemente
- La comunicación asincrónica via RabbitMQ evita acoplamiento
- Base de datos compartida (puede separarse en el futuro)

### Resiliencia
- Manejo de excepciones global
- Validaciones en todas las entradas
- Transacciones ACID en operaciones críticas

### Rendimiento
- Índices en campos de búsqueda frecuente
- Paginación en reportes (a implementar)
- Connection pooling configurado

## 🔒 Seguridad

- Validación de datos en cada endpoint
- Manejo de excepciones seguro
- Passwordes hasheadas (a mejorar con BCrypt)
- CORS configurado apropiadamente

**Mejoras recomendadas:**
- Implementar Spring Security
- Usar JWT para autenticación
- Implementar HTTPS
- Hashing de contraseñas con BCrypt

## 📝 Base de Datos

El script `BaseDatos.sql` incluye:
- Tablas: persona, cliente, cuenta, movimiento
- Índices de rendimiento
- Datos de prueba cargados automáticamente

## 🤝 Contribuir

Este proyecto es parte de una prueba técnica de DEVSU.

## 📞 Soporte

Para problemas o preguntas sobre la implementación, consulta la documentación del código o las pruebas incluidas.

---

**Estado del Proyecto**: ✅ Completado
**Última Actualización**: Diciembre 2025
