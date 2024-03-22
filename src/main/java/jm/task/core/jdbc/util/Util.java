package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
    В данном классе устанавливается соединение с БД, задаются константы передаваемые в DriverManager.
    Если происходит SQLException при попытке установить соединение, он будет перехвачен,
    выведено сообщение об ошибке, и исключение будет проброшено дальше для обработки в других частях программы.
 */

public class Util {
    // JDBC URL, имя пользователя и пароль для подключения к базе данных
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    // Метод для установки соединения с базой данных
    public static Connection getConnection() throws SQLException {
        try  {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            return connection;
        } catch (SQLException e) {
            System.err.println("Не удалось установить соединение с базой данных.");
            throw e; // Пробрасываем исключение дальше
        }
    }
}

