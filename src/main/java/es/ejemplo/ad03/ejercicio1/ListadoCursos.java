package es.ejemplo.ad03.ejercicio1;

import es.ejemplo.ad03.db.CourseQueries;
import es.ejemplo.ad03.db.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;

public final class ListadoCursos {
    private ListadoCursos() {
    }

    public static void main(String[] args) {
        try (Connection connection = DbConnection.open()) {
            CourseQueries.printOccupancy(connection);
        } catch (SQLException exception) {
            System.err.println("No se pudo mostrar el listado: " + exception.getMessage());
        }
    }
}
