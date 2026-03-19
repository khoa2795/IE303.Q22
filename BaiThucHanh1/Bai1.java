import java.util.Random;
import java.util.Scanner;

public class Bai1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Nhập bán kính r: ");
        if (scanner.hasNextDouble()) {
            double r = scanner.nextDouble();

            long totalPoints = 10000000; 
            long pointsInside = 0;
            double side = r + r;

            for (long i = 0; i < totalPoints; i++) {
                double x = (random.nextDouble() * side) - r;
                double y = (random.nextDouble() * side) - r;

                if (x * x + y * y <= r * r) {
                    pointsInside++;
                }
            }

            double squareArea = side * side;
            double circleArea = ((double) pointsInside / totalPoints) * squareArea;

            System.out.println("Diện tích hình tròn xấp xỉ với bán kính " + r + " là: " + circleArea);
        } else {
            System.out.println("Vui lòng nhập một số hợp lệ.");
        }
        
        scanner.close();
    }
}
