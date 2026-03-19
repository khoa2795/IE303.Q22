import java.util.*;

public class Bai4 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            scanner.useDelimiter("[\\s,]+");

            System.out.println("Nhập n (số lượng phần tử) và k (tổng mục tiêu):");
            if (!scanner.hasNextInt()) return;
            int n = scanner.nextInt();
            int k = scanner.nextInt();

            int[] a = new int[n];
            System.out.println("Nhập dãy số:");
            for (int i = 0; i < n; i++) {
                if (scanner.hasNextInt()) {
                    a[i] = scanner.nextInt();
                }
            }

            int[] dp = new int[k + 1];
            Arrays.fill(dp, -1);
            dp[0] = 0;

            boolean[][] trace = new boolean[n + 1][k + 1];

            for (int i = 1; i <= n; i++) {
                int val = a[i - 1];
                for (int s = k; s >= val; s--) {
                    if (dp[s - val] != -1) {
                        if (dp[s - val] + 1 > dp[s]) {
                            dp[s] = dp[s - val] + 1;
                            trace[i][s] = true;
                        }
                    }
                }
            }

            System.out.println("Output (Dãy con dài nhất có tổng bằng " + k + "):");
            if (dp[k] != -1) {
                List<Integer> result = new ArrayList<>();
                int currentSum = k;

                for (int i = n; i >= 1; i--) {
                    if (trace[i][currentSum]) {
                        result.add(a[i - 1]);
                        currentSum -= a[i - 1];
                    }
                }

                Collections.reverse(result);

                for (int i = 0; i < result.size(); i++) {
                    System.out.print(result.get(i));
                    if (i < result.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            } else {
                System.out.println("Không tìm thấy dãy con nào có tổng bằng " + k);
            }
        }
    }
}
