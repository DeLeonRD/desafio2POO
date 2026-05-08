-- ======================================================
-- BASE DE DATOS PARA SISTEMA DE MEDIATECA
-- Proyecto Fase I - Desafío 2 POO941 G01T (Virtual)
-- ======================================================
-- Catedrático: Rafael Alexander Torres Rodriguez
-- ======================================================
-- Integrantes:
-- Kevin Alexander Cardoza Márquez CM251645
-- Francisco Adalberto De la O González DG200722
-- Francisco Alexander Rivera Gómez RG253507
-- Ricardo Balmore Aguilar Ventura AV253053
-- Ricardo Daniel De León Cruz DC251463
-- ======================================================

-- ======================================================
-- 1. LIMPIEZA INICIAL (Opcional - Descomentar si se necesita)
-- ======================================================
-- DROP DATABASE IF EXISTS mediateca;
-- CREATE DATABASE mediateca;
-- USE mediateca;

-- ======================================================
-- 2. CREACIÓN DE TABLAS
-- ======================================================

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    tipo ENUM('ADMIN', 'PROFESOR', 'ALUMNO', 'EMPLEADO') NOT NULL,
    carrera VARCHAR(100),
    telefono VARCHAR(20),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de materiales
CREATE TABLE IF NOT EXISTS material (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL,
    titulo VARCHAR(200),
    autor VARCHAR(100),
    anio_publicacion INT DEFAULT 2024,
    ubicacion VARCHAR(100),
    cantidad_total INT DEFAULT 1,
    cantidad_disponible INT DEFAULT 1
);

-- Tabla de configuración del sistema (mora)
CREATE TABLE IF NOT EXISTS configuracion (
    id_config INT PRIMARY KEY DEFAULT 1,
    max_ejemplares_prestamo INT DEFAULT 3,
    mora_por_dia DECIMAL(10,2) DEFAULT 0.50,
    mora_por_anio DECIMAL(10,2) DEFAULT 0.25
);

-- Tabla de préstamos
CREATE TABLE IF NOT EXISTS prestamos (
    id_prestamo INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_material INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_esperada DATE NOT NULL,
    fecha_devolucion_real DATE,
    estado ENUM('ACTIVO', 'DEVUELTO', 'VENCIDO') DEFAULT 'ACTIVO',
    mora_total DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_material) REFERENCES material(id) ON DELETE CASCADE
);

-- Tabla de logs de errores
CREATE TABLE IF NOT EXISTS logs_errores (
    id_log INT PRIMARY KEY AUTO_INCREMENT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nivel VARCHAR(20),
    mensaje TEXT,
    clase VARCHAR(100),
    metodo VARCHAR(100)
);

-- ======================================================
-- 2.1 NUEVAS TABLAS PARA CONFIGURACIÓN DINÁMICA
-- ======================================================

-- Tabla para tipos de materiales (dinámicos)
CREATE TABLE IF NOT EXISTS tipo_material (
    id_tipo INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    dias_prestamo INT DEFAULT 15
);

-- Tabla para tipos de usuarios (dinámicos)
CREATE TABLE IF NOT EXISTS tipo_usuario (
    id_tipo INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla para configuración de días de préstamo (legado)
CREATE TABLE IF NOT EXISTS configuracion_prestamo (
    id_config INT PRIMARY KEY DEFAULT 1,
    dias_libro INT DEFAULT 15,
    dias_revista INT DEFAULT 10,
    dias_cd INT DEFAULT 7,
    dias_dvd INT DEFAULT 7
);

-- ======================================================
-- 3. DATOS DE PRUEBA
-- ======================================================

-- Insertar tipos de materiales por defecto
INSERT IGNORE INTO tipo_material (nombre, dias_prestamo) VALUES 
('LIBRO', 15), ('REVISTA', 10), ('CD', 7), ('DVD', 7);

-- Insertar tipos de usuarios por defecto
INSERT IGNORE INTO tipo_usuario (nombre) VALUES 
('ADMIN'), ('PROFESOR'), ('ALUMNO'), ('EMPLEADO');

-- Insertar configuración de días de préstamo (legado)
INSERT IGNORE INTO configuracion_prestamo (id_config, dias_libro, dias_revista, dias_cd, dias_dvd) 
VALUES (1, 15, 10, 7, 7);

-- Insertar configuración del sistema
INSERT IGNORE INTO configuracion (id_config, max_ejemplares_prestamo, mora_por_dia, mora_por_anio) 
VALUES (1, 3, 0.50, 0.25);

-- Insertar usuarios de prueba
INSERT IGNORE INTO usuarios (nombre, email, contrasena, tipo, carrera) VALUES 
('Administrador', 'admin@mediateca.com', 'admin123', 'ADMIN', NULL),
('Carlos Pérez', 'carlos@mediateca.com', 'prof123', 'PROFESOR', 'Ingeniería'),
('Ana Gómez', 'ana@mediateca.com', 'alum123', 'ALUMNO', 'Medicina'),
('Ernesto Lara', 'ernesto@gmail.com', 'ernelara', 'EMPLEADO', NULL);

-- Insertar materiales de prueba
INSERT IGNORE INTO material (tipo, titulo, autor, anio_publicacion, ubicacion, cantidad_total, cantidad_disponible) VALUES
('LIBRO', 'El Quijote', 'Miguel de Cervantes', 1605, 'Estante A1', 3, 3),
('LIBRO', 'Cien años de soledad', 'Gabriel García Márquez', 1967, 'Estante A2', 2, 2),
('REVISTA', 'National Geographic', 'Varios', 2023, 'Revistero 1', 5, 5),
('LIBRO', 'Clean Code', 'Robert C. Martin', 2008, 'Estante B1', 1, 1),
('DVD', 'Inception', 'Christopher Nolan', 2010, 'Estante C1', 2, 2),
('CD', 'Thriller', 'Michael Jackson', 1982, 'Estante D1', 1, 1),
('LIBRO', 'El Principito', 'Antoine de Saint-Exupéry', 1943, 'Estante A3', 4, 4),
('REVISTA', 'Time Magazine', 'Varios', 2024, 'Revistero 2', 3, 3);

-- Insertar préstamos de prueba
INSERT IGNORE INTO prestamos (id_usuario, id_material, fecha_prestamo, fecha_devolucion_esperada, estado) VALUES 
(1, 1, '2026-05-01', '2026-04-30', 'ACTIVO'),   -- Vencido (Admin)
(2, 2, '2026-05-01', '2026-05-15', 'ACTIVO'),   -- Vigente (Profesor)
(3, 3, '2026-04-01', '2026-04-15', 'ACTIVO'),   -- Vencido (Alumno)
(4, 4, '2026-05-08', '2026-05-23', 'ACTIVO');   -- Nuevo (Empleado)

-- ======================================================
-- 4. BATERÍA DE PRUEBAS COMPLETA
-- ======================================================

SELECT '========================================' AS '';
SELECT 'INICIANDO BATERÍA DE PRUEBAS' AS '';
SELECT '========================================' AS '';

-- ------------------------------------------------------
-- PRUEBA 1: Verificar usuarios
-- ------------------------------------------------------
SELECT 'PRUEBA 1: Verificar usuarios' AS 'Prueba';
SELECT COUNT(*) AS 'Cantidad de usuarios (esperado >= 4)' FROM usuarios;

-- ------------------------------------------------------
-- PRUEBA 2: Verificar materiales
-- ------------------------------------------------------
SELECT 'PRUEBA 2: Verificar materiales' AS 'Prueba';
SELECT COUNT(*) AS 'Cantidad de materiales (esperado >= 8)' FROM material;

-- ------------------------------------------------------
-- PRUEBA 3: Verificar configuración del sistema
-- ------------------------------------------------------
SELECT 'PRUEBA 3: Verificar configuración' AS 'Prueba';
SELECT * FROM configuracion;

-- ------------------------------------------------------
-- PRUEBA 4: Verificar tipos de materiales
-- ------------------------------------------------------
SELECT 'PRUEBA 4: Verificar tipos de materiales' AS 'Prueba';
SELECT COUNT(*) AS 'Cantidad de tipos (esperado >= 4)' FROM tipo_material;

-- ------------------------------------------------------
-- PRUEBA 5: Verificar tipos de usuarios
-- ------------------------------------------------------
SELECT 'PRUEBA 5: Verificar tipos de usuarios' AS 'Prueba';
SELECT COUNT(*) AS 'Cantidad de tipos (esperado >= 4)' FROM tipo_usuario;

-- ------------------------------------------------------
-- PRUEBA 6: Verificar préstamos activos
-- ------------------------------------------------------
SELECT 'PRUEBA 6: Verificar préstamos activos' AS 'Prueba';
SELECT COUNT(*) AS 'Cantidad de préstamos activos' FROM prestamos WHERE estado = 'ACTIVO';

-- ------------------------------------------------------
-- PRUEBA 7: Calcular días de retraso
-- ------------------------------------------------------
SELECT 'PRUEBA 7: Calcular días de retraso' AS 'Prueba';
SELECT 
    p.id_prestamo,
    u.nombre AS 'Usuario',
    m.titulo AS 'Material',
    p.fecha_devolucion_esperada,
    DATEDIFF(CURDATE(), p.fecha_devolucion_esperada) AS 'Días de retraso',
    CASE 
        WHEN DATEDIFF(CURDATE(), p.fecha_devolucion_esperada) > 0 THEN 'VENCIDO'
        ELSE 'VIGENTE'
    END AS 'Estado'
FROM prestamos p
JOIN usuarios u ON p.id_usuario = u.id_usuario
JOIN material m ON p.id_material = m.id
ORDER BY 'Días de retraso' DESC;

-- ------------------------------------------------------
-- PRUEBA 8: Calcular mora según configuración
-- ------------------------------------------------------
SELECT 'PRUEBA 8: Calcular mora para prestamo más antiguo' AS 'Prueba';
SELECT 
    p.id_prestamo,
    u.nombre AS 'Usuario',
    m.titulo AS 'Material',
    m.tipo AS 'Tipo',
    m.anio_publicacion,
    (YEAR(CURDATE()) - m.anio_publicacion) AS 'Años antigüedad',
    DATEDIFF(CURDATE(), p.fecha_devolucion_esperada) AS 'Días retraso',
    c.mora_por_dia,
    c.mora_por_anio,
    ROUND((DATEDIFF(CURDATE(), p.fecha_devolucion_esperada) * c.mora_por_dia) + 
          ((YEAR(CURDATE()) - m.anio_publicacion) * c.mora_por_anio), 2) AS 'Mora calculada (USD)'
FROM prestamos p
JOIN usuarios u ON p.id_usuario = u.id_usuario
JOIN material m ON p.id_material = m.id
CROSS JOIN configuracion c
WHERE p.estado = 'ACTIVO'
AND DATEDIFF(CURDATE(), p.fecha_devolucion_esperada) > 0
ORDER BY 'Días retraso' DESC
LIMIT 1;

-- ------------------------------------------------------
-- PRUEBA 9: Verificar límite de préstamos por usuario
-- ------------------------------------------------------
SELECT 'PRUEBA 9: Préstamos activos por usuario' AS 'Prueba';
SELECT 
    u.id_usuario,
    u.nombre,
    u.tipo,
    COUNT(p.id_prestamo) AS 'Préstamos activos',
    c.max_ejemplares_prestamo AS 'Límite permitido',
    CASE 
        WHEN COUNT(p.id_prestamo) >= c.max_ejemplares_prestamo THEN 'EXCEDE LÍMITE'
        ELSE 'DENTRO DEL LÍMITE'
    END AS 'Estado'
FROM usuarios u
LEFT JOIN prestamos p ON u.id_usuario = p.id_usuario AND p.estado = 'ACTIVO'
CROSS JOIN configuracion c
GROUP BY u.id_usuario, u.nombre, u.tipo, c.max_ejemplares_prestamo;

-- ------------------------------------------------------
-- PRUEBA 10: Días de préstamo por tipo de material
-- ------------------------------------------------------
SELECT 'PRUEBA 10: Días de préstamo por tipo de material' AS 'Prueba';
SELECT nombre AS 'Tipo de material', dias_prestamo AS 'Días de préstamo' FROM tipo_material;

-- ------------------------------------------------------
-- PRUEBA 11: Distribución de usuarios por tipo
-- ------------------------------------------------------
SELECT 'PRUEBA 11: Distribución de usuarios por tipo' AS 'Prueba';
SELECT 
    tipo,
    COUNT(*) AS 'Cantidad'
FROM usuarios
GROUP BY tipo;

-- ------------------------------------------------------
-- PRUEBA 12: Distribución de materiales por tipo
-- ------------------------------------------------------
SELECT 'PRUEBA 12: Distribución de materiales por tipo' AS 'Prueba';
SELECT 
    tipo,
    COUNT(*) AS 'Cantidad'
FROM material
GROUP BY tipo;

-- ------------------------------------------------------
-- PRUEBA 13: Verificar disponibilidad de materiales
-- ------------------------------------------------------
SELECT 'PRUEBA 13: Materiales con disponibilidad' AS 'Prueba';
SELECT 
    tipo,
    SUM(cantidad_disponible) AS 'Total disponibles',
    SUM(cantidad_total) AS 'Total existentes'
FROM material
GROUP BY tipo
ORDER BY tipo;

-- ------------------------------------------------------
-- PRUEBA 14: Verificar logs (debe estar vacío inicialmente)
-- ------------------------------------------------------
SELECT 'PRUEBA 14: Verificar tabla de logs' AS 'Prueba';
SELECT COUNT(*) AS 'Cantidad de logs (esperado 0 si no hay errores)' FROM logs_errores;

-- ------------------------------------------------------
-- PRUEBA 15: Resumen completo del sistema
-- ------------------------------------------------------
SELECT '========================================' AS '';
SELECT 'RESUMEN COMPLETO DEL SISTEMA' AS '';
SELECT '========================================' AS '';

SELECT 'USUARIOS REGISTRADOS' AS 'Sección';
SELECT id_usuario, nombre, email, tipo FROM usuarios;

SELECT 'MATERIALES DISPONIBLES' AS 'Sección';
SELECT id, tipo, titulo, autor, anio_publicacion, cantidad_disponible FROM material;

SELECT 'PRÉSTAMOS ACTIVOS' AS 'Sección';
SELECT p.id_prestamo, u.nombre, m.titulo, p.fecha_prestamo, p.fecha_devolucion_esperada, p.estado
FROM prestamos p
JOIN usuarios u ON p.id_usuario = u.id_usuario
JOIN material m ON p.id_material = m.id
WHERE p.estado = 'ACTIVO';

SELECT 'TIPOS DE MATERIALES CONFIGURADOS' AS 'Sección';
SELECT * FROM tipo_material;

SELECT 'TIPOS DE USUARIOS CONFIGURADOS' AS 'Sección';
SELECT * FROM tipo_usuario;

-- ------------------------------------------------------
-- PRUEBA 16: Validación de integridad referencial
-- ------------------------------------------------------
SELECT 'PRUEBA 16: Validación de integridad referencial' AS 'Prueba';
SELECT 
    'No hay préstamos con usuarios inexistentes' AS 'Verificación'
WHERE NOT EXISTS (
    SELECT 1 FROM prestamos p LEFT JOIN usuarios u ON p.id_usuario = u.id_usuario WHERE u.id_usuario IS NULL
);

SELECT 
    'No hay préstamos con materiales inexistentes' AS 'Verificación'
WHERE NOT EXISTS (
    SELECT 1 FROM prestamos p LEFT JOIN material m ON p.id_material = m.id WHERE m.id IS NULL
);

-- ------------------------------------------------------
-- PRUEBA 17: Cálculo de días vencidos por préstamo
-- ------------------------------------------------------
SELECT 'PRUEBA 17: Días vencidos por préstamo' AS 'Prueba';
SELECT 
    p.id_prestamo,
    u.nombre,
    m.titulo,
    p.fecha_devolucion_esperada,
    GREATEST(0, DATEDIFF(CURDATE(), p.fecha_devolucion_esperada)) AS 'Días vencidos'
FROM prestamos p
JOIN usuarios u ON p.id_usuario = u.id_usuario
JOIN material m ON p.id_material = m.id
WHERE p.estado = 'ACTIVO'
ORDER BY 'Días vencidos' DESC;

-- ------------------------------------------------------
-- FIN DE LA BATERÍA DE PRUEBAS
-- ------------------------------------------------------
SELECT '========================================' AS '';
SELECT 'BATERÍA DE PRUEBAS COMPLETADA' AS '';
SELECT '========================================' AS '';
SELECT 'Todas las pruebas se ejecutaron correctamente' AS 'Mensaje';
SELECT '========================================' AS '';

-- ======================================================
-- FIN DEL SCRIPT
-- ======================================================