package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Обработка всех исключений, связанных с работой с базой данных должна находиться в dao
public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    // Создание таблицы для User(ов)
    public void createUsersTable() {
            try (Connection conn = Util.getConnection(); Statement statement = conn.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS users " +
                        "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), last_name VARCHAR(255), age INT)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    // Удаление таблицы User(ов)
    public void dropUsersTable() {
            try (Connection conn = Util.getConnection(); Statement statement = conn.createStatement()) {
                statement.executeUpdate("DROP TABLE IF EXISTS users");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    // Добавление User в таблицу
    public void saveUser(String name, String lastName, byte age) {

            try (Connection conn = Util.getConnection(); PreparedStatement psm = conn.prepareStatement("INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)")) {
                psm.setString(1, name);
                psm.setString(2, lastName);
                psm.setByte(3, age);
                psm.executeUpdate();
                conn.close();//close connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    // Удаление User из таблицы ( id )
    public void removeUserById(long id) {

            try (Connection conn = Util.getConnection(); PreparedStatement psm = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
                psm.setLong(1, id);
                psm.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    // Получение всех User(ов) из таблицы
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

            try (Connection conn = Util.getConnection(); ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM users")) {
                while (resultSet.next()) {
                    User user = new User(
                            resultSet.getString("name"),
                            resultSet.getString("last_name"),
                            resultSet.getByte("age")
                    );
                    user.setId(resultSet.getLong("id"));
                    users.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return users;
        }

    // Очистка содержания таблицы
    public void cleanUsersTable() {
            try (Connection conn = Util.getConnection(); Statement statement = conn.createStatement()) {
                statement.executeUpdate("TRUNCATE TABLE users");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}