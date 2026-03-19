import java.util.Random;
import java.util.Scanner;

public class Bai1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // 1. Nhập bán kính r từ bàn phím
        System.out.print("Nhập bán kính r: ");
        if (scanner.hasNextDouble()) {
            double r = scanner.nextDouble();

            // Số lần lặp để xấp xỉ (càng lớn càng chính xác)
            long totalPoints = 10000000; 
            long pointsInside = 0;

            // Cạnh của hình vuông bao quanh hình tròn là 2r.
            // Diện tích hình vuông = (2r) * (2r)
            // Tuy nhiên, để tuân thủ "không dùng hằng số khác", ta dùng r + r
            double side = r + r;

            // 2. Thực hiện mô phỏng Monte Carlo
            for (long i = 0; i < totalPoints; i++) {
                // Tạo toạ độ ngẫu nhiên x, y trong khoảng [-r, r]
                // random.nextDouble() trả về [0.0, 1.0)
                // side * random.nextDouble() trả về [0.0, 2r)
                // trừ đi r sẽ được [-r, r)
                double x = (random.nextDouble() * side) - r;
                double y = (random.nextDouble() * side) - r;

                // Kiểm tra điểm có nằm trong hình tròn không: x^2 + y^2 <= r^2
                if (x * x + y * y <= r * r) {
                    pointsInside++;
                }
            }

            // 3. Tính diện tích xấp xỉ
            // Tỉ lệ diện tích hình tròn / diện tích hình vuông ~ pointsInside / totalPoints
            // => Diện tích hình tròn ~ (pointsInside / totalPoints) * (side * side)
            
            double squareArea = side * side;
            double circleArea = ((double) pointsInside / totalPoints) * squareArea;

            System.out.println("Diện tích hình tròn xấp xỉ với bán kính " + r + " là: " + circleArea);
        } else {
            System.out.println("Vui lòng nhập một số hợp lệ.");
        }
        
        scanner.close();
    }
}
