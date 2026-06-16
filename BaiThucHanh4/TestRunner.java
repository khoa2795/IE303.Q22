import dao.ProductDao;
import model.Product;

public class TestRunner {
    public static void main(String[] args) {
        try {
            ProductDao dao = new ProductDao();
            System.out.println("Products queried from database:");
            for (Product product : dao.findAll()) {
                System.out.println(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
