import java.util.Random;

public class Bai2 {
    public static void main(String[] args) {
        Random random = new Random();

        long totalPoints = 10000000;
        long pointsInside = 0;

        for (long i = 0; i < totalPoints; i++) {
            double x = (2 * random.nextDouble()) - 1;
            double y = (2 * random.nextDouble()) - 1;

            if (x * x + y * y <= 1) {
                pointsInside++;
            }
        }

        double piApprox = 4.0 * pointsInside / totalPoints;

        System.out.println("Giá trị xấp xỉ của PI là: " + piApprox);
        System.out.println("Giá trị thực tế của Math.PI: " + Math.PI);
    }
}
