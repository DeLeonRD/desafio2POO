-- Desafío 1 - POO941 - Universidad Don Bosco
-- Sistema de gestión de materiales de una mediateca

-- Crear la base de datos
DROP DATABASE IF EXISTS mediateca;
CREATE DATABASE mediateca;
USE mediateca;
 
-- ===================================================================
-- LA TABLA PADRE 
-- TABLA: material
CREATE TABLE material (
    id_material        		INT AUTO_INCREMENT PRIMARY KEY,
    codigo_identificacion	VARCHAR(8) NOT NULL UNIQUE,
    titulo             		VARCHAR(100) NOT NULL,
    unidades_disponibles 	INT NOT NULL DEFAULT 1,
    tipo_material      		ENUM('LIBRO', 'REVISTA', 'CD_AUDIO', 'DVD') NOT NULL,
    fecha_registro     		DATETIME DEFAULT CURRENT_TIMESTAMP,
    
-- Validación: (LIB, REV, CDA, DVD + 5 dígitos)
    CONSTRAINT chk_codigo_formato CHECK (
        codigo_identificacion REGEXP '^(LIB|REV|CDA|DVD)[0-9]{5}$'
    ),
    CONSTRAINT chk_unidades CHECK (unidades_disponibles >= 0)
);

-- ===================================================================
-- TABLAS SUBCLASE HERENCIA DE LA PADRE
-- (material) -> (Escrito y Audiovisual)

-- TABLA: material_escrito
CREATE TABLE material_escrito (
    id_material         INT PRIMARY KEY,
    editorial           VARCHAR(150) NOT NULL,
    
    CONSTRAINT fk_escrito_material 
        FOREIGN KEY (id_material) REFERENCES material(id_material)
        ON DELETE CASCADE ON UPDATE CASCADE
);


-- TABLA: material_audiovisual
CREATE TABLE material_audiovisual (
    id_material         INT PRIMARY KEY,
    duracion            VARCHAR(10) NOT NULL COMMENT 'Formato HH:MM:SS',
    genero              VARCHAR(100) NOT NULL,
    
    CONSTRAINT fk_audiovisual_material 
        FOREIGN KEY (id_material) REFERENCES material(id_material)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================================
-- TABLAS 
-- (Escrito y Audiovisual)  -> (libros, revistas, cds, dvds)

-- TABLA: libro
CREATE TABLE libro (
    id_material         INT PRIMARY KEY,
    autor               VARCHAR(200) NOT NULL,
    numero_paginas      INT NOT NULL,
    isbn                VARCHAR(20),
    anio_publicacion    YEAR NOT NULL,
    
    CONSTRAINT fk_libro_escrito 
        FOREIGN KEY (id_material) REFERENCES material_escrito(id_material)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_paginas CHECK (numero_paginas > 0)
);

-- TABLA: revista
CREATE TABLE revista (
    id_material         INT PRIMARY KEY,
    periodicidad        ENUM('SEMANAL', 'QUINCENAL', 'MENSUAL', 'BIMESTRAL', 'TRIMESTRAL', 'SEMESTRAL', 'ANUAL') NOT NULL,
    fecha_publicacion   DATE NOT NULL,
    
    CONSTRAINT fk_revista_escrito 
        FOREIGN KEY (id_material) REFERENCES material_escrito(id_material)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- TABLA: cd_audio
CREATE TABLE cd_audio (
    id_material         INT PRIMARY KEY,
    artista             VARCHAR(200) NOT NULL,
    numero_canciones    INT NOT NULL,
    
    CONSTRAINT fk_cd_audiovisual 
        FOREIGN KEY (id_material) REFERENCES material_audiovisual(id_material)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_canciones CHECK (numero_canciones > 0)
);

-- TABLA: dvd
CREATE TABLE dvd (
    id_material         INT PRIMARY KEY,
    director            VARCHAR(200) NOT NULL,
    
    CONSTRAINT fk_dvd_audiovisual 
        FOREIGN KEY (id_material) REFERENCES material_audiovisual(id_material)
        ON DELETE CASCADE ON UPDATE CASCADE
);


-- ==================================================================
-- TABLA: correlativo
-- (LIB00001, REV00001, CDA00001, DVD00001)

CREATE TABLE correlativo (
    tipo        VARCHAR(3) PRIMARY KEY,
    ultimo      INT NOT NULL DEFAULT 0,
    
    CONSTRAINT chk_tipo_correlativo CHECK (tipo IN ('LIB', 'REV', 'CDA', 'DVD'))
);

-- Inicializar los correlativos
INSERT INTO correlativo (tipo, ultimo) VALUES 
    ('LIB', 0),
    ('REV', 0),
    ('CDA', 0),
    ('DVD', 0);


DELIMITER //

CREATE PROCEDURE generar_codigo(
    IN p_tipo VARCHAR(3),
    OUT p_codigo VARCHAR(8)
)
BEGIN
    DECLARE v_siguiente INT;
    
    UPDATE correlativo SET ultimo = ultimo + 1 WHERE tipo = p_tipo;
    SELECT ultimo INTO v_siguiente FROM correlativo WHERE tipo = p_tipo;
    SET p_codigo = CONCAT(p_tipo, LPAD(v_siguiente, 5, '0'));
END //

DELIMITER ;



-- DATOS DE EJEMPLO PARA LAS TABLAS

-- Ejemplo 1: Insertar un Libro
CALL generar_codigo('LIB', @codigo_libro);

INSERT INTO material (codigo_identificacion, titulo, unidades_disponibles, tipo_material)
VALUES (@codigo_libro, 'Java 7: Guía práctica', 3, 'LIBRO');

SET @id_libro = LAST_INSERT_ID();

INSERT INTO material_escrito (id_material, editorial)
VALUES (@id_libro, 'Anaya');

INSERT INTO libro (id_material, autor, numero_paginas, isbn, anio_publicacion)
VALUES (@id_libro, 'Roberto Montero Miguel', 320, '978-84-415-2988-6', 2011);

-- Ejemplo 2: Insertar una Revista
CALL generar_codigo('REV', @codigo_revista);

INSERT INTO material (codigo_identificacion, titulo, unidades_disponibles, tipo_material)
VALUES (@codigo_revista, 'National Geographic - Edición Especial', 5, 'REVISTA');

SET @id_revista = LAST_INSERT_ID();

INSERT INTO material_escrito (id_material, editorial)
VALUES (@id_revista, 'National Geographic Society');

INSERT INTO revista (id_material, periodicidad, fecha_publicacion)
VALUES (@id_revista, 'MENSUAL', '2025-12-01');

-- Ejemplo 3: Insertar un CD de Audio
CALL generar_codigo('CDA', @codigo_cd);

INSERT INTO material (codigo_identificacion, titulo, unidades_disponibles, tipo_material)
VALUES (@codigo_cd, 'Abbey Road', 2, 'CD_AUDIO');



-- // //////////

-- Ver todos los libros con su información completa
SELECT m.codigo_identificacion, m.titulo, me.editorial,
       l.autor, l.numero_paginas, l.isbn, l.anio_publicacion,
       m.unidades_disponibles
FROM material m
JOIN material_escrito me ON m.id_material = me.id_material
JOIN libro l ON me.id_material = l.id_material;


-- Buscar material por código
SELECT m.*, m.tipo_material
FROM material m
WHERE m.codigo_identificacion = 'LIB00001';

-- Listar todos los materiales disponibles
SELECT m.codigo_identificacion, m.titulo, m.tipo_material, m.unidades_disponibles
FROM material m
WHERE m.unidades_disponibles > 0
ORDER BY m.tipo_material, m.titulo;
