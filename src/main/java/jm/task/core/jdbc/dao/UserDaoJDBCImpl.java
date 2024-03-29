package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS Users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "lastName VARCHAR(255) NOT NULL," +
                "age INT NOT NULL)";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL);
            System.out.println("Таблица Users успешно создана.");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы Users:");
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String SQL = "DROP TABLE IF EXISTS Users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL);
            System.out.println("Таблица Users успешно удалена.");
        } catch (SQLException e) {
            if (e instanceof SQLNonTransientException) {
                System.err.println("Постоянная проблема при удалении таблицы.");
            } else if (e instanceof SQLIntegrityConstraintViolationException) {
                System.err.println("Нарушение ограничений целостности. Возможно, на таблицу есть ссылки из других таблиц.");
            } else {
                System.err.println("Ошибка при удалении таблицы.");
            }
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String SQL = "INSERT INTO Users (name, lastname, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("Пользователь " + name + " успешно добавлен в таблицу Users.");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении пользователя:");
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String SQL = "DELETE FROM Users WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Пользователь с ID " + id + " успешно удален из таблицы Users.");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден в таблице Users.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении пользователя по ID " + id + ":");
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String SQL = "SELECT * FROM Users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка пользователей:");
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String SQL = "DELETE FROM Users";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.executeUpdate();
            System.out.println("Содержимое таблицы Users успешно очищено.");
        } catch (SQLException e) {
            System.err.println("Ошибка при очистке таблицы Users:");
            e.printStackTrace();
        }
    }
}
