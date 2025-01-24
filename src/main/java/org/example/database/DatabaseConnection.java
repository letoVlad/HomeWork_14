package org.example.database;

import java.math.BigInteger;
import java.sql.*;

public class DatabaseConnection {
    private final DatabaseConfig config = new DatabaseConfig();

    public void saveData(String methodName, String resultMethod) {
        try (var connection = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            var insertQuery = "INSERT INTO cache (method_name, result_method) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, methodName);
                preparedStatement.setString(2, resultMethod);
                preparedStatement.executeUpdate();
                System.out.println("Данные успешно добавлены в таблицу!");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении данных: " + e.getMessage());
        }
    }

    public BigInteger findDataByMethodName(String methodName) {
        BigInteger result = null;

        try (var connection = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword())) {

            var selectQuery = "SELECT result_method FROM cache WHERE method_name = ?";
            try (var preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, methodName);

                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String resultString = resultSet.getString("result_method");
                        result = new BigInteger(resultString);
                        System.out.println("Данные найдены: " + result);
                    } else {
                        System.out.println("Данные не найдены для method_name: " + methodName);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}

