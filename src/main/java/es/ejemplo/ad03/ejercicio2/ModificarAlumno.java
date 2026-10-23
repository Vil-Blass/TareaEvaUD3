package es.ejemplo.ad03.ejercicio2;

import es.ejemplo.ad03.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public final class ModificarAlumno {
    private ModificarAlumno() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DbConnection.open()) {
            System.out.print("DNI del alumno: ");
            String dni = scanner.nextLine().trim();
            String currentName = findName(connection, dni);

            if (currentName == null) {
                System.out.println("No existe ningún alumno con ese DNI.");
                return;
            }

            System.out.println("Nombre actual: " + currentName);
            System.out.print("Nuevo nombre (en blanco para cancelar): ");
            String newName = scanner.nextLine().trim();
            if (newName.isEmpty()) {
                System.out.println("No se han realizado cambios.");
                return;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement("UPDATE alumnos SET nombre = ? WHERE dni = ?")) {
                statement.setString(1, newName);
                statement.setString(2, dni);
                int updated = statement.executeUpdate();
                if (updated == 1) {
                    System.out.println("Nombre actualizado correctamente.");
                } else {
                    System.out.println("No se modificó ningún registro.");
                }
            }
        } catch (SQLException exception) {
            System.err.println("No se pudo modificar el alumno: " + exception.getMessage());
        }
    }

    private static String findName(Connection connection, String dni) throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT nombre FROM alumnos WHERE dni = ?")) {
            statement.setString(1, dni);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getString("nombre") : null;
            }
        }
    }
}
