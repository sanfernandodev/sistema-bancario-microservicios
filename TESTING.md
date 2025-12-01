# 🧪 Guía de Testing

## Pruebas Unitarias

### Cliente-Persona Service

#### Ejecutar las pruebas

```bash
cd cliente-persona-service
mvn test
```

#### Pruebas incluidas en `ClienteServiceTest.java`

1. **testCrearClienteExitosamente**
   - Verifica creación exitosa de un cliente
   - Valida que se genere número de cliente único
   - Confirma que el estado sea verdadero

2. **testCrearClienteConIdentificacionDuplicada**
   - Verifica que no se puede crear cliente con identificación duplicada
   - Lanza excepción `ClienteAlreadyExistsException`

3. **testObtenerClientePorIdExitosamente**
   - Obtiene un cliente por su ID
   - Verifica que los datos sean correctos

4. **testObtenerClientePorIdNoEncontrado**
   - Verifica que lanza excepción cuando cliente no existe
   - Lanza `ClienteNotFoundException`

5. **testActualizarClienteExitosamente**
   - Actualiza datos de un cliente existente
   - Valida que los cambios se guarden

6. **testEliminarClienteExitosamente**
   - Elimina un cliente de la base de datos
   - Verifica que se llamó al método delete

7. **testCambiarEstadoCliente**
   - Cambia el estado de activo a inactivo
   - Verifica persistencia del cambio

## Pruebas de Integración

### Ejecutar todas las pruebas

```bash
mvn test
```

### Casos de Prueba Manuales

#### 1. Crear Cliente y Obtener

```bash
# Crear cliente
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test Cliente",
    "genero": "M",
    "edad": 30,
    "identificacion": "1111111111",
    "direccion": "Test Street",
    "telefono": "0999999999",
    "contrasena": "Test123"
  }'

# Obtener cliente por ID (reemplazar ID)
curl http://localhost:8080/api/clientes/1
```

#### 2. Crear Cuenta y Realizar Movimientos

```bash
# Crear cuenta
curl -X POST http://localhost:8081/api/cuentas \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "777666",
    "tipoCuenta": "Ahorros",
    "saldoInicial": 3000,
    "clienteId": 1
  }'

# Registrar depósito
curl -X POST "http://localhost:8081/api/movimientos/registrar?cuentaId=1&tipoMovimiento=Deposito&valor=500"

# Registrar retiro
curl -X POST "http://localhost:8081/api/movimientos/registrar?cuentaId=1&tipoMovimiento=Retiro&valor=200"
```

#### 3. Validar Saldo no Disponible (F3)

```bash
# Intentar retiro con saldo insuficiente
curl -X POST "http://localhost:8081/api/movimientos/registrar?cuentaId=1&tipoMovimiento=Retiro&valor=10000"

# Esperado: Error 400 "Saldo no disponible"
```

#### 4. Generar Reporte de Estado de Cuenta (F4)

```bash
curl -X GET "http://localhost:8081/api/reportes/estado-cuenta?clienteId=1&fechaInicio=2024-01-01&fechaFin=2024-12-31"
```

Respuesta esperada:
```json
{
  "clienteId": 1,
  "fechaInicio": "2024-01-01",
  "fechaFin": "2024-12-31",
  "fechaReporte": "2024-12-01T15:30:45",
  "cuentas": [
    {
      "numeroCuenta": "478758",
      "tipoCuenta": "Ahorros",
      "saldoInicial": 2000,
      "saldoActual": 1925,
      "estado": true,
      "movimientos": [
        {
          "fecha": "2024-12-01T10:15:30",
          "tipo": "Retiro",
          "valor": 75,
          "saldo": 1925,
          "descripcion": "Retiro de 75"
        }
      ]
    }
  ]
}
```

## 🧫 Datos de Prueba

Los siguientes datos se insertan automáticamente al inicializar:

### Clientes

| ID | Nombre | Identificación | Estado |
|----|--------|---|---|
| 1 | Jose Lema | 1234567890 | Activo |
| 2 | Marianela Montalvo | 0987654321 | Activo |
| 3 | Juan Osorio | 1122334455 | Activo |

### Cuentas

| Número | Tipo | Saldo Inicial | Cliente |
|--------|------|---|---|
| 478758 | Ahorros | 2000 | Jose Lema |
| 225487 | Corriente | 100 | Marianela |
| 495878 | Ahorros | 0 | Juan |
| 496825 | Ahorros | 540 | Marianela |
| 585545 | Corriente | 1000 | Jose Lema |

## ✅ Checklist de Pruebas

- [ ] Service 1 se inicia en puerto 8080
- [ ] Service 2 se inicia en puerto 8081
- [ ] Base de datos se inicializa con datos de prueba
- [ ] CRUD de clientes funciona
- [ ] CRUD de cuentas funciona
- [ ] Registro de movimientos actualiza saldo
- [ ] Validación de saldo funciona
- [ ] Reporte de estado de cuenta se genera
- [ ] Pruebas unitarias pasan
- [ ] Endpoints responden con JSON válido
- [ ] Errores devuelven código HTTP correcto
- [ ] Transacciones se revierten en caso de error

## 🐛 Troubleshooting

### Error: "Connection refused"
- Verificar que Docker está corriendo
- Verificar que los puertos no están en uso

### Error: "Saldo no disponible" inesperado
- Verificar saldo actual de la cuenta
- Revisar que el movimiento anterior se haya registrado

### Base de datos vacía
- Verificar que BaseDatos.sql está en la raíz del proyecto
- Reiniciar Docker: `docker-compose down && docker-compose up -d`

## 📊 Métricas de Cobertura

Para generar reporte de cobertura:

```bash
mvn clean test jacoco:report
# Reporte en: target/site/jacoco/index.html
```

## 🔄 Ciclo Completo de Testing

```bash
# 1. Limpiar e iniciar Docker
docker-compose down -v
docker-compose up -d

# 2. Esperar a que se inicialicen los servicios (30 segundos)
sleep 30

# 3. Ejecutar pruebas unitarias
cd cliente-persona-service
mvn test

cd ../cuenta-movimiento-service
mvn test

# 4. Probar manualmente con Postman
# Importar: Postman_Collection.json

# 5. Verificar logs
docker-compose logs -f
```
