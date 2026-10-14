CREATE DATABASE gestion_citas;
\c gestion_citas;
--Creacion de la base de datos

DROP TABLE IF EXISTS cita_tecnica;
DROP TABLE IF EXISTS cliente;
--Eliminamos las tabals por si existen

CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    nombres VARCHAR(80) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

--Creamos la tabla cliente

CREATE TABLE cita_tecnica (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    tipo_servicio VARCHAR(80) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    
    -- Llave foránea hacia la tabla cliente
    CONSTRAINT fk_cita_cliente 
        FOREIGN KEY (cliente_id) 
        REFERENCES cliente(id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
        
    -- Restricción de estados permitidos según requerimientos (RF07)
    CONSTRAINT chk_estado_cita 
        CHECK (estado IN ('Pendiente', 'En proceso', 'Finalizada', 'Cancelada'))
);

--Creamos la tabla cita_tecnica

-- Búsqueda parcial e insensible a mayúsculas/minúsculas por nombres/apellidos
CREATE INDEX idx_cliente_busqueda ON cliente (LOWER(nombres), LOWER(apellidos), LOWER(correo));

-- Filtros de citas por estado y fecha
CREATE INDEX idx_cita_filtros ON cita_tecnica (estado, fecha);

--Datos de Prueba
-- Inserción de Clientes
INSERT INTO cliente (nombres, apellidos, telefono, correo, activo) VALUES
('Raúl', 'Valverde', '8888-1111', 'raul.valverde@uamv.edu.ni', TRUE),
('Kellys', 'Bellanger', '8888-2222', 'kellys.bellanger@uamv.edu.ni', TRUE),
('Nora', 'Obregón', '8888-3333', 'nora.obregon@uamv.edu.ni', TRUE),
('Steven', 'Flores', '8888-4444', 'steven.flores@uamv.edu.ni', TRUE),
('Carlos', 'Mendoza', '8555-9999', 'carlos.mendoza@gmail.com', TRUE);

-- Inserción de Citas Técnicas asociadas
INSERT INTO cita_tecnica (cliente_id, fecha, hora, tipo_servicio, descripcion, estado) VALUES
(1, '2026-10-14', '09:00:00', 'Mantenimiento Preventivo', 'Limpieza general de PC de escritorio y cambio de pasta térmica', 'Pendiente'),
(2, '2026-10-14', '10:30:00', 'Reparación de Hardware', 'Diagnóstico de fallo en fuente de poder e instalación de RAM', 'En proceso'),
(3, '2026-10-15', '14:00:00', 'Instalación de Software', 'Instalación y activación de SO Windows 11 Pro y Suite de Oficina', 'Pendiente'),
(4, '2026-10-12', '11:00:00', 'Diagnóstico de Red', 'Revisión y configuración de puntos de red local en oficina', 'Finalizada');

SELECT 'Clientes registrados:' AS Info, COUNT(*) AS Total FROM cliente;
SELECT 'Citas registradas:' AS Info, COUNT(*) AS Total FROM cita_tecnica;
