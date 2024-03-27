package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            // Создание таблицы users
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(64), " +
                    "lastname VARCHAR(64), " +
                    "age TINYINT)").executeUpdate();
            transaction.commit();
            logger.log(Level.INFO, "Таблица users успешно создана");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при создании таблицы users", e);
            rollbackTransaction();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            // Удаление таблицы users, если она существует
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
            logger.log(Level.INFO, "Таблица users успешно удалена");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при удалении таблицы users", e);
            rollbackTransaction();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            // Сохранение пользователя в базе данных
            session.save(new User(name, lastName, age));
            transaction.commit();
            logger.log(Level.INFO, "Пользователь успешно добавлен: " + name + " " + lastName + ", возраст: " + age);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при добавлении пользователя", e);
            rollbackTransaction();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            // Удаление пользователя по id
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                logger.log(Level.INFO, "Пользователь с id " + id + " успешно удален");
            }
            transaction.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при удалении пользователя", e);
            rollbackTransaction();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSession()) {
            // Получение всех пользователей из базы данных
            return session.createQuery("FROM User", User.class).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при получении всех пользователей", e);
            rollbackTransaction();
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            // Очистка таблицы пользователей
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
            logger.log(Level.INFO, "Таблица пользователей успешно очищена");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при очистке таблицы пользователей", e);
            rollbackTransaction();
        }
    }

    // Метод для отката транзакции
    private void rollbackTransaction() {
        try {
            Util.getSession().getTransaction().rollback();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Ошибка при откате транзакции", ex);
        }
    }
}
