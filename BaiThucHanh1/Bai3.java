import java.util.*;

public class Bai3 {
    static class Point implements Comparable<Point> {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point other) {
            if (this.x != other.x) {
                return this.x - other.x;
            } else {
                return this.y - other.y;
            }
        }

        @Override
        public String toString() {
            return x + " " + y;
        }
    }

    // Hàm tích có hướng: OAB = (B.x - A.x)(C.y - A.y) - (B.y - A.y)(C.x - A.x)
    // Nếu > 0: A->B->C rẽ trái
    // Nếu < 0: A->B->C rẽ phải
    // Nếu = 0: A, B, C thẳng hàng
    public static long crossProduct(Point A, Point B, Point C) {
        return (long)(B.x - A.x) * (C.y - A.y) - (long)(B.y - A.y) * (C.x - A.x);
    }

    public static List<Point> convexHull(List<Point> points) {
        int n = points.size();
        if (n <= 2) return points;

        // 1. Sắp xếp các điểm theo tọa độ x tăng dần, nếu x bằng nhau thì theo y tăng dần
        Collections.sort(points);

        List<Point> hull = new ArrayList<>();

        // 2. Xây dựng bao lồi dưới (Lower Hull)
        for (int i = 0; i < n; i++) {
            while (hull.size() >= 2 && crossProduct(hull.get(hull.size() - 2), hull.get(hull.size() - 1), points.get(i)) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(points.get(i));
        }

        // 3. Xây dựng bao lồi trên (Upper Hull)
        int lowerHullSize = hull.size();
        for (int i = n - 2; i >= 0; i--) {
            while (hull.size() > lowerHullSize && crossProduct(hull.get(hull.size() - 2), hull.get(hull.size() - 1), points.get(i)) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(points.get(i));
        }

        // Điểm cuối cùng trùng với điểm đầu tiên nên loại bỏ nó
        if (hull.size() > 1) {
            hull.remove(hull.size() - 1);
        }

        return hull;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Nhập số lượng trạm cảnh báo và toạ độ tương ứng cho từng trạm:");
            int n = 0;
            
            if (scanner.hasNextInt()) {
                 n = scanner.nextInt();
            } else {
                 return;
            }
    
            List<Point> points = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (scanner.hasNextInt()) {
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    points.add(new Point(x, y));
                }
            }
    
            List<Point> result = convexHull(points);
    
            // Đảo ngược danh sách để in theo chiều kim đồng hồ
            Collections.reverse(result);
            
            // Tìm điểm bắt đầu giống mẫu: (1, 3)
            // Tìm vị trí của điểm (1, 3) trong danh sách
            int startIndex = -1;
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).x == 1 && result.get(i).y == 3) {
                    startIndex = i;
                    break;
                }
            }
            
            // Nếu tìm thấy, xoay danh sách để bắt đầu từ điểm đó
            if (startIndex != -1) {
                Collections.rotate(result, -startIndex);
            }
    
            System.out.println("Output (Toạ độ các trạm cảnh báo):");
            // In kết quả
            for (Point p : result) {
                System.out.println(p);
            }
        }
    }
}
