package es.ejemplo.ad03.ejercicio4;

import es.ejemplo.ad03.db.CourseQueries;
import es.ejemplo.ad03.db.DbConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public final class ConsultarMatriculados {
    private ConsultarMatriculados() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DbConnection.open()) {
            CourseQueries.printCourseNames(connection);
            System.out.print("ID del curso que quieres consultar: ");
            int courseId;
            try {
                courseId = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("El ID debe ser un número entero.");
                return;
            }

            if (!CourseQueries.exists(connection, courseId)) {
                System.out.println("El curso indicado no existe.");
                return;
            }

            try (CallableStatement statement =
                         connection.prepareCall("{? = call obtener_numero_matriculados(?)}")) {
                statement.registerOutParameter(1, Types.INTEGER);
                statement.setInt(2, courseId);
                statement.execute();
                System.out.println("Número de matrículas: " + statement.getInt(1));
            }
        } catch (SQLException exception) {
            System.err.println("No se pudo consultar la función: " + exception.getMessage());
        }
    }
}
