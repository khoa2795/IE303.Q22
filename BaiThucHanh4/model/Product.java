package model;

import java.util.Locale;

public class Product {
    private long id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private int quantity;
    private String imagePath;

    public Product() {
    }

    public Product(String name, String brand, String description, double price, int quantity, String imagePath) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public Product(long id, String name, String brand, String description, double price, int quantity, String imagePath) {
        this(name, brand, description, price, quantity, imagePath);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return String.format(
                Locale.US,
                "%d - %s | %s | $%.2f | x%d | %s",
                id,
                safe(name, "Unknown"),
                safe(brand, "Unknown"),
                price,
                quantity,
                safe(imagePath, "no-image"));
    }

    private String safe(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
