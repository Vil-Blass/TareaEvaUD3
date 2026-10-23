package es.ejemplo.ad03.ejercicio3;

import es.ejemplo.ad03.db.CourseQueries;
import es.ejemplo.ad03.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public final class MatricularAlumno {
    private MatricularAlumno() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DbConnection.open()) {
            System.out.print("DNI (8 números y una letra): ");
            String dni = scanner.nextLine().trim();
            if (!dni.matches("[0-9]{8}[A-Za-z]")) {
                System.out.println("DNI no válido: debe tener exactamente 8 números y una letra.");
                return;
            }
            dni = dni.toUpperCase();

            connection.setAutoCommit(false);
            try {
                String studentName = findStudentName(connection, dni);
                boolean newStudent = studentName == null;
                if (newStudent) {
                    System.out.print("Nombre del nuevo alumno: ");
                    studentName = scanner.nextLine().trim();
                    if (studentName.isEmpty()) {
                        connection.rollback();
                        System.out.println("El nombre no puede quedar vacío. Operación cancelada.");
                        return;
                    }
                } else {
                    System.out.println("Alumno: " + studentName);
                }

                CourseQueries.printAvailableSeats(connection);
                System.out.print("ID del curso elegido: ");
                int courseId;
                try {
                    courseId = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException exception) {
                    connection.rollback();
                    System.out.println("El ID debe ser un número entero. Operación cancelada.");
                    return;
                }

                Integer capacity = lockCourseAndGetCapacity(connection, courseId);
                if (capacity == null) {
                    connection.rollback();
                    System.out.println("El curso indicado no existe.");
                    return;
                }

                if (isAlreadyEnrolled(connection, dni, courseId)) {
                    connection.rollback();
                    System.out.println("El alumno ya está matriculado en ese curso.");
                    return;
                }

                if (countEnrollmentsForUpdate(connection, courseId) >= capacity) {
                    connection.rollback();
                    System.out.println("El curso no tiene plazas libres.");
                    return;
                }

                if (newStudent) {
                    insertStudent(connection, dni, studentName);
                }
                insertEnrollment(connection, dni, courseId);
                connection.commit();
                System.out.println("Matrícula registrada correctamente para " + studentName + ".");
            } catch (SQLException exception) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
                throw exception;
            }
        } catch (SQLException exception) {
            System.err.println("No se pudo completar la matrícula: " + exception.getMessage());
        }
    }

    private static String findStudentName(Connection connection, String dni) throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT nombre FROM alumnos WHERE dni = ?")) {
            statement.setString(1, dni);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getString("nombre") : null;
            }
        }
    }

    private static Integer lockCourseAndGetCapacity(Connection connection, int courseId)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT a.capacidad FROM cursos c JOIN aulas a ON a.id = c.aula_id "
                        + "WHERE c.id = ? FOR UPDATE")) {
            statement.setInt(1, courseId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getInt("capacidad") : null;
            }
        }
    }

    private static boolean isAlreadyEnrolled(Connection connection, String dni, int courseId)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT dni FROM matriculas WHERE dni = ? AND curso_id = ? FOR UPDATE")) {
            statement.setString(1, dni);
            statement.setInt(2, courseId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    private static int countEnrollmentsForUpdate(Connection connection, int courseId) throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             "SELECT dni FROM matriculas WHERE curso_id = ? FOR UPDATE")) {
            statement.setInt(1, courseId);
            try (ResultSet result = statement.executeQuery()) {
                int count = 0;
                while (result.next()) {
                    count++;
                }
                return count;
            }
        }
    }

    private static void insertStudent(Connection connection, String dni, String name)
            throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO alumnos (dni, nombre) VALUES (?, ?)")) {
            statement.setString(1, dni);
            statement.setString(2, name);
            statement.executeUpdate();
        }
    }

    private static void insertEnrollment(Connection connection, String dni, int courseId)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO matriculas (dni, curso_id, fecha_matricula) VALUES (?, ?, CURRENT_DATE)")) {
            statement.setString(1, dni);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        }
    }
}
