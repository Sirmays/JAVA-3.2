package Server;


import java.sql.*;
import java.util.*;

public class AuthenticationService {
    public List<Entry> findAll() {
        Connection connection = DBConnection.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            List<Entry> numbers = new ArrayList<>();
            while (resultSet.next()) {
                numbers.add(
                        new Entry(
                                resultSet.getString("name"),
                                resultSet.getString("login"),
                                resultSet.getString("password")
                        )
                );
            }
            return numbers;

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }



    public Optional<Entry> findEntryByCredentials(String login, String password) {

        return findAll().stream()
                .filter(entry -> entry.getLogin().equals(login) && entry.getPassword().equals(password))
                .findFirst();

    }




    public static class Entry {
        private String name;
        private String login;
        private String password;



        public Entry(String name, String login, String password) {
            this.name = name;
            this.login = login;
            this.password = password;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    ", name='" + name + '\'' +
                    ", login='" + login + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }


        public String getName() {
            return name;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }



        public void setName(String name) {this.name = name; }

        public void setLogin(String login) {this.login = login;}

        public void setPassword(String password) {this.password = password;}


    }

    }


