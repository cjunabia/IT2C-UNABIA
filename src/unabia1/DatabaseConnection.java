package unabia1;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:coffee_shop.db";  // Path to SQLite database

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createTableSQL = 
            "CREATE TABLE IF NOT EXISTS purchase_history (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "coffee_type TEXT NOT NULL, " +
            "size TEXT NOT NULL, " +
            "add_ons TEXT, " +
            "total_price REAL NOT NULL, " +
            "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ");";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
     
    public static void clearDatabase() {
    String deleteQuery = "DELETE FROM purchase_history";
    try (Connection connection = DatabaseConnection.getConnection();
         Statement statement = connection.createStatement()) {
        statement.executeUpdate(deleteQuery);
        System.out.println("Database cleared.");
    } catch (SQLException e) {
        System.out.println("Error clearing the database: " + e.getMessage());
    }
}
public static void resetDatabase() {
    String dropQuery = "DROP TABLE IF EXISTS purchase_history";
    String createQuery = 
        "CREATE TABLE IF NOT EXISTS purchase_history (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "coffee_type TEXT NOT NULL, " +
        "size TEXT NOT NULL, " +
        "add_ons TEXT, " +
        "total_price REAL NOT NULL, " +
        "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");";

    try (Connection connection = DatabaseConnection.getConnection();
         Statement statement = connection.createStatement()) {
        // Drop the table first
        statement.executeUpdate(dropQuery);
        // Recreate the table
        statement.executeUpdate(createQuery);
        System.out.println("Database reset.");
    } catch (SQLException e) {
        System.out.println("Error resetting the database: " + e.getMessage());
    }
}
private static void initializeCoffeeMenuInDatabase() {
    String[] coffees = {"Espresso", "Americano", "Latte", "Cappuccino", "Mocha"};
    double[] prices = {2.50, 3.00, 3.50, 3.75, 4.00};

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "INSERT OR IGNORE INTO coffee_menu (name, price) VALUES (?, ?)")) {

        for (int i = 0; i < coffees.length; i++) {
            preparedStatement.setString(1, coffees[i]);
            preparedStatement.setDouble(2, prices[i]);
            preparedStatement.executeUpdate();
        }

    } catch (SQLException e) {
        System.out.println("Error initializing coffee menu in database: " + e.getMessage());
    }
}

}
