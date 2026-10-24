-- ============================================================
-- AD03 - Tarea evaluativa 2026-2027
-- Base de datos: dbformacion
-- MySQL 8.x / 8.4+
-- ============================================================

DROP DATABASE IF EXISTS dbformacion;
CREATE DATABASE dbformacion
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE dbformacion;

-- ------------------------------------------------------------
-- 1. Tablas
-- ------------------------------------------------------------
CREATE TABLE aulas (
    id_aula INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    edificio VARCHAR(80) NOT NULL,
    capacidad INT NOT NULL,
    CONSTRAINT chk_aulas_capacidad CHECK (capacidad > 0)
);

CREATE TABLE cursos (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(120) NOT NULL,
    fecha DATE NOT NULL,
    id_aula INT NOT NULL,
    CONSTRAINT fk_cursos_aula
        FOREIGN KEY (id_aula) REFERENCES aulas(id_aula)
);

CREATE TABLE alumnos (
    dni VARCHAR(9) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE matriculas (
    dni VARCHAR(9) NOT NULL,
    id_curso INT NOT NULL,
    fecha_matricula DATE NOT NULL,
    PRIMARY KEY (dni, id_curso),
    CONSTRAINT fk_matriculas_alumno
        FOREIGN KEY (dni) REFERENCES alumnos(dni),
    CONSTRAINT fk_matriculas_curso
        FOREIGN KEY (id_curso) REFERENCES cursos(id_curso)
);

-- ------------------------------------------------------------
-- 2. Datos de prueba
-- ------------------------------------------------------------
INSERT INTO aulas (nombre, edificio, capacidad) VALUES
('Aula Ada Lovelace', 'Edificio A', 4),
('Aula Alan Turing', 'Edificio A', 6),
('Laboratorio Grace Hopper', 'Edificio B', 5),
('Aula Margaret Hamilton', 'Edificio B', 8),
('Sala Tim Berners-Lee', 'Edificio C', 3);

INSERT INTO cursos (titulo, fecha, id_aula) VALUES
('Java y JDBC', '2026-10-05', 2),
('SQL avanzado', '2026-10-12', 1),
('Git y CI/CD', '2026-10-19', 3),
('Docker para desarrollo', '2026-10-26', 4),
('APIs REST con Spring', '2026-11-02', 5),
('Testing con JUnit', '2026-11-09', 3);

INSERT INTO alumnos (dni, nombre) VALUES
('12345678A', 'Ane Arrieta'),
('23456789B', 'Jon Bilbao'),
('34567890C', 'Maialen Castro'),
('45678901D', 'Unai Duran'),
('56789012E', 'Irati Etxeberria'),
('67890123F', 'Mikel Fernandez'),
('78901234G', 'Nerea Garcia'),
('89012345H', 'Iker Hernandez'),
('90123456J', 'Leire Ibarrola'),
('01234567K', 'Gorka Jimenez'),
('11223344L', 'June Lasa'),
('22334455M', 'Asier Mendia');

INSERT INTO matriculas (dni, id_curso, fecha_matricula) VALUES
-- Java y JDBC: 4 / 6
('12345678A', 1, '2026-09-15'),
('23456789B', 1, '2026-09-15'),
('34567890C', 1, '2026-09-16'),
('45678901D', 1, '2026-09-16'),

-- SQL avanzado: 4 / 4 (COMPLETO)
('56789012E', 2, '2026-09-17'),
('67890123F', 2, '2026-09-17'),
('78901234G', 2, '2026-09-18'),
('89012345H', 2, '2026-09-18'),

-- Git y CI/CD: 2 / 5
('90123456J', 3, '2026-09-19'),
('01234567K', 3, '2026-09-19'),

-- Docker para desarrollo: 5 / 8
('11223344L', 4, '2026-09-20'),
('22334455M', 4, '2026-09-20'),
('12345678A', 4, '2026-09-21'),
('56789012E', 4, '2026-09-21'),
('78901234G', 4, '2026-09-22'),

-- APIs REST con Spring: 2 / 3
('34567890C', 5, '2026-09-23'),
('67890123F', 5, '2026-09-23');

-- El curso 6 queda sin matrículas para comprobar que el listado
-- incluye también cursos con 0 alumnos matriculados.

-- ------------------------------------------------------------
-- 3. Función utilizada en el ejercicio 4
-- ------------------------------------------------------------
DROP FUNCTION IF EXISTS obtener_numero_matriculados;
DELIMITER $$
CREATE FUNCTION obtener_numero_matriculados(p_id_curso INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE v_total INT;

    SELECT COUNT(*)
      INTO v_total
      FROM matriculas
     WHERE id_curso = p_id_curso;

    RETURN v_total;
END$$
DELIMITER ;

-- Comprobación opcional:
-- SELECT obtener_numero_matriculados(1); -- Debe devolver 4
