-- ========================
-- HOTEL
-- ========================
CREATE TABLE hotel (
                       id BIGSERIAL PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL,
                       direccion VARCHAR(255) NOT NULL,
                       ciudad VARCHAR(100) NOT NULL,
                       telefono VARCHAR(20),
                       email VARCHAR(100),
                       estrellas INTEGER CHECK (estrellas BETWEEN 1 AND 5),
                       descripcion TEXT,
                       activo BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT NOW(),
                       updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- TIPO HABITACION
-- ========================
CREATE TABLE tipo_habitacion (
                                 id BIGSERIAL PRIMARY KEY,
                                 nombre VARCHAR(50) NOT NULL UNIQUE,
                                 descripcion TEXT,
                                 capacidad_maxima INTEGER NOT NULL,
                                 precio_base NUMERIC(10,2) NOT NULL,
                                 amenities TEXT,
                                 activo BOOLEAN DEFAULT TRUE
);

-- ========================
-- HABITACION
-- ========================
CREATE TABLE habitacion (
                            id BIGSERIAL PRIMARY KEY,
                            numero VARCHAR(10) NOT NULL UNIQUE,
                            planta INTEGER NOT NULL,
                            estado VARCHAR(30) NOT NULL DEFAULT 'DISPONIBLE',
                            descripcion TEXT,
                            activo BOOLEAN DEFAULT TRUE,
                            hotel_id BIGINT NOT NULL REFERENCES hotel(id),
                            tipo_habitacion_id BIGINT NOT NULL REFERENCES tipo_habitacion(id),
                            created_at TIMESTAMP DEFAULT NOW(),
                            updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- CLIENTE
-- ========================
CREATE TABLE cliente (
                         id BIGSERIAL PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         apellidos VARCHAR(100) NOT NULL,
                         documento VARCHAR(20) NOT NULL UNIQUE,
                         tipo_documento VARCHAR(10) NOT NULL DEFAULT 'DNI',
                         nacionalidad VARCHAR(50),
                         email VARCHAR(100) UNIQUE,
                         telefono VARCHAR(20),
                         direccion VARCHAR(255),
                         fecha_nacimiento DATE,
                         cliente_frecuente BOOLEAN DEFAULT FALSE,
                         total_estancias INTEGER DEFAULT 0,
                         activo BOOLEAN DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT NOW(),
                         updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- EMPLEADO
-- ========================
CREATE TABLE empleado (
                          id BIGSERIAL PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellidos VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          rol VARCHAR(30) NOT NULL DEFAULT 'RECEPCION',
                          turno VARCHAR(20),
                          activo BOOLEAN DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT NOW(),
                          updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- RESERVA
-- ========================
CREATE TABLE reserva (
                         id BIGSERIAL PRIMARY KEY,
                         fecha_entrada DATE NOT NULL,
                         fecha_salida DATE NOT NULL,
                         num_adultos INTEGER NOT NULL DEFAULT 1,
                         num_ninos INTEGER NOT NULL DEFAULT 0,
                         precio_total NUMERIC(10,2),
                         estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
                         canal_origen VARCHAR(30) DEFAULT 'WEB',
                         observaciones TEXT,
                         cliente_id BIGINT NOT NULL REFERENCES cliente(id),
                         habitacion_id BIGINT NOT NULL REFERENCES habitacion(id),
                         empleado_id BIGINT REFERENCES empleado(id),
                         created_at TIMESTAMP DEFAULT NOW(),
                         updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- SERVICIO EXTRA
-- ========================
CREATE TABLE servicio_extra (
                                id BIGSERIAL PRIMARY KEY,
                                nombre VARCHAR(100) NOT NULL,
                                descripcion TEXT,
                                precio NUMERIC(10,2) NOT NULL,
                                activo BOOLEAN DEFAULT TRUE
);

-- ========================
-- RESERVA - SERVICIO EXTRA
-- ========================
CREATE TABLE reserva_servicio (
                                  id BIGSERIAL PRIMARY KEY,
                                  reserva_id BIGINT NOT NULL REFERENCES reserva(id),
                                  servicio_extra_id BIGINT NOT NULL REFERENCES servicio_extra(id),
                                  cantidad INTEGER DEFAULT 1,
                                  precio_unitario NUMERIC(10,2) NOT NULL
);

-- ========================
-- FACTURA
-- ========================
CREATE TABLE factura (
                         id BIGSERIAL PRIMARY KEY,
                         numero_factura VARCHAR(20) NOT NULL UNIQUE,
                         fecha_emision TIMESTAMP DEFAULT NOW(),
                         subtotal NUMERIC(10,2) NOT NULL,
                         iva NUMERIC(10,2) NOT NULL,
                         total NUMERIC(10,2) NOT NULL,
                         estado_pago VARCHAR(20) DEFAULT 'PENDIENTE',
                         reserva_id BIGINT NOT NULL REFERENCES reserva(id),
                         created_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- DATOS INICIALES
-- ========================
INSERT INTO hotel (nombre, direccion, ciudad, telefono, email, estrellas, descripcion)
VALUES ('Hotel Mallorca Paradise', 'Paseo Marítimo 45', 'Palma de Mallorca', '+34971123456', 'info@mallorcaparadise.com', 5, 'Hotel de lujo frente al mar en el corazón de Palma');

INSERT INTO tipo_habitacion (nombre, descripcion, capacidad_maxima, precio_base, amenities)
VALUES
    ('Individual', 'Habitación individual con vistas al jardín', 1, 80.00, 'WiFi, TV, Aire acondicionado'),
    ('Doble', 'Habitación doble con cama de matrimonio', 2, 120.00, 'WiFi, TV, Aire acondicionado, Minibar'),
    ('Suite', 'Suite de lujo con vistas al mar', 2, 280.00, 'WiFi, TV 4K, Aire acondicionado, Minibar, Jacuzzi, Terraza'),
    ('Familiar', 'Habitación familiar con dos camas dobles', 4, 180.00, 'WiFi, TV, Aire acondicionado, Sofá cama');

INSERT INTO servicio_extra (nombre, descripcion, precio)
VALUES
    ('Desayuno buffet', 'Desayuno buffet completo', 15.00),
    ('Parking', 'Plaza de parking cubierta por día', 12.00),
    ('Spa', 'Acceso al spa y piscina climatizada', 35.00),
    ('Traslado aeropuerto', 'Traslado ida o vuelta al aeropuerto de Palma', 25.00),
    ('Late check-out', 'Salida tardía hasta las 16:00', 30.00);

INSERT INTO empleado (nombre, apellidos, email, password, rol, turno)
VALUES
    ('Admin', 'Sistema', 'admin@mallorcaparadise.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'ADMIN', 'MAÑANA'),
    ('Pedro', 'García', 'recepcion@mallorcaparadise.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'RECEPCION', 'MAÑANA');