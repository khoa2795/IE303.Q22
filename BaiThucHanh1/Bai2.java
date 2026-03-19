import java.util.Random;

public class Bai2 {
    public static void main(String[] args) {
        Random random = new Random();

        // 1. Số lần lặp để xấp xỉ (càng lớn càng chính xác)
        long totalPoints = 10000000;
        long pointsInside = 0;

        // 2. Thực hiện mô phỏng Monte Carlo trong hình vuông đơn vị [-1, 1]
        for (long i = 0; i < totalPoints; i++) {
            // Tạo toạ độ ngẫu nhiên x, y trong khoảng [-1, 1]
            // random.nextDouble() trả về [0.0, 1.0)
            // 2 * random.nextDouble() trả về [0.0, 2.0)
            // trừ đi 1 sẽ được [-1.0, 1.0)
            double x = (2 * random.nextDouble()) - 1;
            double y = (2 * random.nextDouble()) - 1;

            // Kiểm tra điểm có nằm trong hình tròn đơn vị không: x^2 + y^2 <= 1
            if (x * x + y * y <= 1) {
                pointsInside++;
            }
        }

        // 3. Tính giá trị PI xấp xỉ
        // Diện tích hình vuông bao quanh (cạnh 2) = 2 * 2 = 4
        // Diện tích hình tròn đơn vị = pi * r^2 = pi * 1^2 = pi
        // Tỉ lệ: (Diện tích hình tròn) / (Diện tích hình vuông) ~ pointsInside / totalPoints
        // => pi / 4 ~ pointsInside / totalPoints
        // => pi ~ 4 * (pointsInside / totalPoints)

        double piApprox = 4.0 * pointsInside / totalPoints;

        System.out.println("Giá trị xấp xỉ của PI là: " + piApprox);
        System.out.println("Giá trị thực tế của Math.PI: " + Math.PI);
    }
}
