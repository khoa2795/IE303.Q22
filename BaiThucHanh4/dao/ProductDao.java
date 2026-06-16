package dao;

import db.Database;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    public List<Product> findAll() throws SQLException {
        String sql = """
                SELECT id, name, brand, description, price, quantity, image_path
                FROM products
                ORDER BY id
                """;
        List<Product> products = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(map(resultSet));
            }
        }

        return products;
    }

    private Product map(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("brand"),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                resultSet.getInt("quantity"),
                resultSet.getString("image_path"));
    }
}
