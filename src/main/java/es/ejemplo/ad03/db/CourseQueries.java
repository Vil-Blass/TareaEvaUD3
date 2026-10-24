package es.ejemplo.ad03.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class CourseQueries {
    private static final String COURSE_OCCUPANCY_SQL = """
            SELECT c.id_curso AS id, c.titulo, c.fecha, COUNT(m.dni) AS matriculados,
                   a.capacidad, a.nombre AS aula, a.edificio
            FROM cursos c
            JOIN aulas a ON a.id_aula = c.id_aula
            LEFT JOIN matriculas m ON m.id_curso = c.id_curso
            GROUP BY c.id_curso, c.titulo, c.fecha, a.capacidad, a.nombre, a.edificio
            ORDER BY c.fecha, c.id_curso
            """;

    private static final String AVAILABLE_SEATS_SQL = """
            SELECT c.id_curso AS id, c.titulo, a.capacidad - COUNT(m.dni) AS plazas_libres
            FROM cursos c
            JOIN aulas a ON a.id_aula = c.id_aula
            LEFT JOIN matriculas m ON m.id_curso = c.id_curso
            GROUP BY c.id_curso, c.titulo, a.capacidad, c.fecha
            ORDER BY c.fecha, c.id_curso
            """;

    private static final String COURSE_NAMES_SQL =
            "SELECT id_curso AS id, titulo, fecha FROM cursos ORDER BY fecha, id_curso";

    private CourseQueries() {
    }

    public static void printOccupancy(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(COURSE_OCCUPANCY_SQL)) {
            System.out.printf("%-4s %-32s %-12s %12s %10s %-16s %s%n",
                    "ID", "Curso", "Fecha", "Matriculados", "Capacidad", "Aula", "Edificio");
            while (result.next()) {
                System.out.printf("%-4d %-32s %-12s %12d %10d %-16s %s%n",
                        result.getInt("id"),
                        result.getString("titulo"),
                        result.getDate("fecha"),
                        result.getInt("matriculados"),
                        result.getInt("capacidad"),
                        result.getString("aula"),
                        result.getString("edificio"));
            }
        }
    }

    public static void printAvailableSeats(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(AVAILABLE_SEATS_SQL)) {
            System.out.printf("%-4s %-40s %12s%n", "ID", "Curso", "Plazas libres");
            while (result.next()) {
                System.out.printf("%-4d %-40s %12d%n",
                        result.getInt("id"),
                        result.getString("titulo"),
                        result.getInt("plazas_libres"));
            }
        }
    }

    public static void printCourseNames(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(COURSE_NAMES_SQL)) {
            System.out.printf("%-4s %-40s %s%n", "ID", "Curso", "Fecha");
            while (result.next()) {
                System.out.printf("%-4d %-40s %s%n",
                        result.getInt("id"),
                        result.getString("titulo"),
                        result.getDate("fecha"));
            }
        }
    }

    public static boolean exists(Connection connection, int courseId) throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT 1 FROM cursos WHERE id_curso = ?")) {
            statement.setInt(1, courseId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }
}
