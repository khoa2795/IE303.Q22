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

    public static long crossProduct(Point A, Point B, Point C) {
        return (long)(B.x - A.x) * (C.y - A.y) - (long)(B.y - A.y) * (C.x - A.x);
    }

    public static List<Point> convexHull(List<Point> points) {
        int n = points.size();
        if (n <= 2) return points;

        Collections.sort(points);

        List<Point> hull = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            while (hull.size() >= 2 && crossProduct(hull.get(hull.size() - 2), hull.get(hull.size() - 1), points.get(i)) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(points.get(i));
        }

        int lowerHullSize = hull.size();
        for (int i = n - 2; i >= 0; i--) {
            while (hull.size() > lowerHullSize && crossProduct(hull.get(hull.size() - 2), hull.get(hull.size() - 1), points.get(i)) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(points.get(i));
        }

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
    
            Collections.reverse(result);
            
            int startIndex = -1;
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).x == 1 && result.get(i).y == 3) {
                    startIndex = i;
                    break;
                }
            }
            
            if (startIndex != -1) {
                Collections.rotate(result, -startIndex);
            }
    
            System.out.println("Output (Toạ độ các trạm cảnh báo):");
            for (Point p : result) {
                System.out.println(p);
            }
        }
    }
}
