-- ============================================================
-- BASE DE DATOS: ahorroperu_db
-- Cooperativa de Ahorro y Crédito AHORROPERU
-- MySQL 9.7
-- ============================================================

CREATE DATABASE IF NOT EXISTS ahorroperu_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ahorroperu_db;

-- ------------------------------------------------------------
-- TABLA: usuarios (personal administrativo y asociados)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario      INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    apellido        VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    rol             ENUM('ADMIN','ASOCIADO') NOT NULL DEFAULT 'ASOCIADO',
    activo          TINYINT(1) NOT NULL DEFAULT 1,
    fecha_registro  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- TABLA: asociados
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS asociados (
    id_asociado     INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT NOT NULL,
    dni             VARCHAR(15) NOT NULL UNIQUE,
    telefono        VARCHAR(20),
    direccion       VARCHAR(255),
    fecha_nacimiento DATE,
    estado          ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_asoc_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- TABLA: tipo_prestamo
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tipo_prestamo (
    id_tipo         INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     TEXT,
    tasa_interes    DECIMAL(5,2) NOT NULL COMMENT 'Tasa anual en porcentaje',
    plazo_max_meses INT NOT NULL,
    monto_max       DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- TABLA: solicitudes_prestamo
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS solicitudes_prestamo (
    id_solicitud        INT AUTO_INCREMENT PRIMARY KEY,
    id_asociado         INT NOT NULL,
    id_tipo             INT NOT NULL,
    monto_solicitado    DECIMAL(12,2) NOT NULL,
    plazo_meses         INT NOT NULL,
    tasa_interes        DECIMAL(5,2) NOT NULL,
    proposito           TEXT,
    estado              ENUM('PENDIENTE','APROBADO','RECHAZADO') NOT NULL DEFAULT 'PENDIENTE',
    fecha_solicitud     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_resolucion    DATETIME,
    observacion         TEXT,
    id_admin_resolucion INT,
    CONSTRAINT fk_sol_asociado FOREIGN KEY (id_asociado)
        REFERENCES asociados(id_asociado),
    CONSTRAINT fk_sol_tipo FOREIGN KEY (id_tipo)
        REFERENCES tipo_prestamo(id_tipo),
    CONSTRAINT fk_sol_admin FOREIGN KEY (id_admin_resolucion)
        REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- TABLA: prestamos (cuando la solicitud es APROBADA)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS prestamos (
    id_prestamo     INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud    INT NOT NULL UNIQUE,
    monto_aprobado  DECIMAL(12,2) NOT NULL,
    fecha_inicio    DATE NOT NULL,
    fecha_fin       DATE NOT NULL,
    estado          ENUM('VIGENTE','CANCELADO','MORA') NOT NULL DEFAULT 'VIGENTE',
    CONSTRAINT fk_prest_sol FOREIGN KEY (id_solicitud)
        REFERENCES solicitudes_prestamo(id_solicitud)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- TABLA: cuotas
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cuotas (
    id_cuota        INT AUTO_INCREMENT PRIMARY KEY,
    id_prestamo     INT NOT NULL,
    numero_cuota    INT NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    monto_capital   DECIMAL(12,2) NOT NULL,
    monto_interes   DECIMAL(12,2) NOT NULL,
    monto_total     DECIMAL(12,2) NOT NULL,
    estado          ENUM('PENDIENTE','PAGADO','VENCIDO') NOT NULL DEFAULT 'PENDIENTE',
    fecha_pago      DATETIME,
    CONSTRAINT fk_cuota_prestamo FOREIGN KEY (id_prestamo)
        REFERENCES prestamos(id_prestamo)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- TABLA: soportes (documentos adjuntos a solicitudes)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS soportes (
    id_soporte      INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud    INT NOT NULL,
    nombre_archivo  VARCHAR(255) NOT NULL,
    tipo_archivo    VARCHAR(100),
    ruta_archivo    VARCHAR(500) NOT NULL,
    fecha_subida    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sop_sol FOREIGN KEY (id_solicitud)
        REFERENCES solicitudes_prestamo(id_solicitud)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- DATOS INICIALES
-- ------------------------------------------------------------

-- Admin por defecto: admin@ahorroperu.com / Admin1234
-- Password hash SHA-256 de "Admin1234"
INSERT INTO usuarios (nombre, apellido, email, password_hash, rol) VALUES
('Administrador', 'Sistema', 'admin@ahorroperu.com',
 SHA2('Admin1234', 256), 'ADMIN');

-- Tipos de préstamo
INSERT INTO tipo_prestamo (nombre, descripcion, tasa_interes, plazo_max_meses, monto_max) VALUES
('Personal',      'Préstamo para gastos personales',           18.00, 36,  20000.00),
('Vehicular',     'Préstamo para adquisición de vehículo',     15.00, 60,  80000.00),
('Hipotecario',   'Préstamo para adquisición de vivienda',     12.00, 240, 500000.00),
('Educativo',     'Préstamo para estudios superiores',         10.00, 48,  30000.00),
('Microempresa',  'Préstamo para emprendimientos',             20.00, 24,  15000.00);

-- Asociado de prueba: juan@test.com / Test1234
INSERT INTO usuarios (nombre, apellido, email, password_hash, rol) VALUES
('Juan', 'Pérez', 'juan@test.com', SHA2('Test1234', 256), 'ASOCIADO');

INSERT INTO asociados (id_usuario, dni, telefono, direccion, fecha_nacimiento) VALUES
(2, '12345678', '999888777', 'Av. Lima 123, Lima', '1990-05-15');
