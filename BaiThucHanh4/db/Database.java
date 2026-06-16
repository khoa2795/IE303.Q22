package db;

import model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public final class Database {
    private static final String JDBC_URL = "jdbc:h2:./lab4db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static final List<Product> SAMPLE_PRODUCTS = List.of(
            new Product("4DFWD PULSE SHOES", "Adidas", "This product is excluded from all promotional discounts and offers.", 160.00, 12, "images/img1.png"),
            new Product("FORUM MID SHOES", "Adidas", "Classic basketball style meets modern comfort.", 100.00, 15, "images/img2.png"),
            new Product("SUPERNOVA SHOES", "Adidas", "NMD City Stock 2 - Lightweight cushioning.", 150.00, 10, "images/img3.png"),
            new Product("ADIDAS RUNNING", "Adidas", "NMD City Stock 2 - Experience the future.", 160.00, 8, "images/img4.png"),
            new Product("ULTRABOOST 22", "Adidas", "Responsive Boost technology for energy return.", 120.00, 14, "images/img5.png"),
            new Product("4DFWD RED", "Adidas", "High-performance running shoes.", 160.00, 9, "images/img6.png"),
            new Product("STAN SMITH SHOES", "Adidas", "Timeless look, effortless style, and everyday versatility.", 95.00, 20, "images/img2.png"),
            new Product("SUPERSTAR CLASSIC", "Adidas", "The iconic shell-toe shoe that started it all.", 100.00, 18, "images/img4.png")
    );

    static {
        try {
            init();
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    private static void init() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS products (
                        id IDENTITY PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        brand VARCHAR(255) NOT NULL,
                        description CLOB,
                        price DECIMAL(12,2) NOT NULL,
                        quantity INT NOT NULL DEFAULT 0,
                        image_path VARCHAR(500) DEFAULT ''
                    )
                    """);
            seedIfEmpty(connection);
        }
    }

    private static void seedIfEmpty(Connection connection) throws SQLException {
        try (PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM products");
             ResultSet resultSet = countStatement.executeQuery()) {
            resultSet.next();
            if (resultSet.getLong(1) > 0) {
                return;
            }
        }

        String insertSql = """
                INSERT INTO products(name, brand, description, price, quantity, image_path)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            for (Product product : SAMPLE_PRODUCTS) {
                insertStatement.setString(1, product.getName());
                insertStatement.setString(2, product.getBrand());
                insertStatement.setString(3, product.getDescription());
                insertStatement.setDouble(4, product.getPrice());
                insertStatement.setInt(5, product.getQuantity());
                insertStatement.setString(6, product.getImagePath());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        }
    }
}
