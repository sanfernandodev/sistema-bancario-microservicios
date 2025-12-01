-- BaseDatos.sql
-- Script para crear la base de datos del sistema bancario

CREATE DATABASE IF NOT EXISTS banco_sistema;
USE banco_sistema;

-- Tabla Persona
CREATE TABLE IF NOT EXISTS persona (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(10) NOT NULL,
    edad INT NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    dtype VARCHAR(31)
) ENGINE=InnoDB;

-- Tabla Cliente
CREATE TABLE IF NOT EXISTS cliente (
    persona_id BIGINT PRIMARY KEY,
    numero_cliente VARCHAR(50) UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (persona_id) REFERENCES persona(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla Cuenta
CREATE TABLE IF NOT EXISTS cuenta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(19,2) NOT NULL,
    saldo_disponible DECIMAL(19,2) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    cliente_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Tabla Movimiento
CREATE TABLE IF NOT EXISTS movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(19,2) NOT NULL,
    saldo DECIMAL(19,2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    descripcion VARCHAR(255),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Índices para optimizar búsquedas
CREATE INDEX idx_cliente_identificacion ON persona(identificacion);
CREATE INDEX idx_cuenta_numero ON cuenta(numero_cuenta);
CREATE INDEX idx_cuenta_cliente ON cuenta(cliente_id);
CREATE INDEX idx_movimiento_cuenta ON movimiento(cuenta_id);
CREATE INDEX idx_movimiento_fecha ON movimiento(fecha);

-- Datos de prueba
-- Insertando clientes
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono, dtype)
VALUES ('Jose Lema', 'M', 35, '1234567890', 'Otavalo sn y principal', '098254785', 'Cliente');

INSERT INTO cliente (persona_id, numero_cliente, contrasena, estado)
VALUES (1, 'CLI-001', '1234', TRUE);

INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono, dtype)
VALUES ('Marianela Montalvo', 'F', 32, '0987654321', 'Amazonas y NNUU', '097548965', 'Cliente');

INSERT INTO cliente (persona_id, numero_cliente, contrasena, estado)
VALUES (2, 'CLI-002', '5678', TRUE);

INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono, dtype)
VALUES ('Juan Osorio', 'M', 40, '1122334455', '13 junio y Equinoccial', '098874587', 'Cliente');

INSERT INTO cliente (persona_id, numero_cliente, contrasena, estado)
VALUES (3, 'CLI-003', '1245', TRUE);

-- Insertando cuentas
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id)
VALUES ('478758', 'Ahorros', 2000, 2000, TRUE, 1);

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id)
VALUES ('225487', 'Corriente', 100, 100, TRUE, 2);

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id)
VALUES ('495878', 'Ahorros', 0, 0, TRUE, 3);

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id)
VALUES ('496825', 'Ahorros', 540, 540, TRUE, 2);

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id)
VALUES ('585545', 'Corriente', 1000, 1000, TRUE, 1);

-- Insertando movimientos de prueba
INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id, descripcion)
VALUES (NOW(), 'Retiro', 575, 1425, 1, 'Retiro de 575');

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id, descripcion)
VALUES (NOW(), 'Deposito', 600, 700, 2, 'Deposito de 600');

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id, descripcion)
VALUES (NOW(), 'Deposito', 150, 150, 3, 'Deposito de 150');

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id, descripcion)
VALUES (NOW(), 'Retiro', 540, 0, 4, 'Retiro de 540');
