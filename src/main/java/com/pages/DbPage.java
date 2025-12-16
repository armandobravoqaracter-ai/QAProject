package com.pages;

import com.utils.DbConnection;
import org.testng.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbPage {

    private Connection connection;
    private ResultSet resultSet;

    public void conectarDB(
            String host,
            String port,
            String database,
            String user,
            String password
    ) {
        try {
            connection = DbConnection.getConnection(
                    host, port, database, user, password
            );
        } catch (Exception e) {
            Assert.fail("Error al conectar a PostgreSQL: " + e.getMessage());
        }
    }

    public void ejecutarConsulta(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
        } catch (Exception e) {
            Assert.fail("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    public void validarCampo(String columna, String valorEsperado) {
        try {
            if (!resultSet.next()) {
                Assert.fail("La consulta no retornó resultados");
            }

            String valorReal = resultSet.getString(columna);

            Assert.assertEquals(
                    valorReal.trim(),
                    valorEsperado.trim(),
                    "El valor de la columna '" + columna + "' no coincide"
            );

        } catch (Exception e) {
            Assert.fail("Error al validar campo: " + e.getMessage());
        }
    }
}
