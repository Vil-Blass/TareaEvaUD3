CREATE DATABASE IF NOT EXISTS formacion
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE formacion;

CREATE TABLE IF NOT EXISTS aulas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    edificio VARCHAR(100) NOT NULL,
    capacidad INT NOT NULL CHECK (capacidad > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cursos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    fecha DATE NOT NULL,
    aula_id INT NOT NULL,
    CONSTRAINT fk_cursos_aulas FOREIGN KEY (aula_id) REFERENCES aulas(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS alumnos (
    dni CHAR(9) PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS matriculas (
    dni CHAR(9) NOT NULL,
    curso_id INT NOT NULL,
    fecha_matricula DATE NOT NULL,
    PRIMARY KEY (dni, curso_id),
    CONSTRAINT fk_matriculas_alumnos FOREIGN KEY (dni) REFERENCES alumnos(dni),
    CONSTRAINT fk_matriculas_cursos FOREIGN KEY (curso_id) REFERENCES cursos(id)
) ENGINE=InnoDB;

INSERT INTO aulas (id, nombre, edificio, capacidad)
VALUES (1, 'Aula 1', 'Edificio principal', 20)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO cursos (id, titulo, fecha, aula_id)
VALUES
    (1, 'Introducción a JDBC', '2026-11-10', 1),
    (2, 'Bases de datos avanzadas', '2026-12-15', 1)
ON DUPLICATE KEY UPDATE titulo = VALUES(titulo), fecha = VALUES(fecha), aula_id = VALUES(aula_id);

INSERT INTO alumnos (dni, nombre)
VALUES
    ('12345678Z', 'Ana García'),
    ('87654321X', 'Luis Pérez')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT IGNORE INTO matriculas (dni, curso_id, fecha_matricula)
VALUES
    ('12345678Z', 1, '2026-10-01'),
    ('87654321X', 1, '2026-10-02');

DROP FUNCTION IF EXISTS obtener_numero_matriculados;
DELIMITER //
CREATE FUNCTION obtener_numero_matriculados(p_curso_id INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total
    FROM matriculas
    WHERE curso_id = p_curso_id;
    RETURN total;
END//
DELIMITER ;
