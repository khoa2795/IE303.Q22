import java.util.*;

public class Bai4 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Thiết lập bộ phân tách là dấu cách, tab, dấu chấm câu hoặc xuống dòng
            // Set delimiter to whitespace or punctuation (for handling commas)
            scanner.useDelimiter("[\\s,]+");

            // Đọc n (số lượng phần tử) và k (tổng mục tiêu)
            // Read n (number of elements) and k (target sum)
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

            // Tìm dãy con (subarray) dài nhất có tổng bằng k
            // Find the longest contiguous subarray with sum k
            int[] dp = new int[k + 1];
            Arrays.fill(dp, -1);
            dp[0] = 0;

            // trace[i][s] = true nếu phần tử thứ i (trong mảng a) được chọn để tạo ra tổng s
            // trace[i][s] = true if item i was chosen to achieve sum s
            boolean[][] trace = new boolean[n + 1][k + 1];

            // Duyệt qua từng phần tử của mảng a
            // Iterate through each element of array a
            for (int i = 1; i <= n; i++) {
                int val = a[i - 1];
                // Duyệt ngược từ k về val để đảm bảo mỗi phần tử chỉ được dùng một lần cho mỗi tổng
                // Iterate backwards from k to val (Knapsack 0/1 logic)
                for (int s = k; s >= val; s--) {
                    if (dp[s - val] != -1) {
                        if (dp[s - val] + 1 > dp[s]) {
                            dp[s] = dp[s - val] + 1;
                            trace[i][s] = true;
                        }
                    }
                }
            }

            // Truy vết để in kết quả
            // Backtrack to print result
            System.out.println("Output (Dãy con dài nhất có tổng bằng " + k + "):");
            if (dp[k] != -1) {
                List<Integer> result = new ArrayList<>();
                int currentSum = k;

                // Truy vết ngược từ cuối về đầu
                // Backtrack from end to start
                for (int i = n; i >= 1; i--) {
                    // Nếu tại bước i, tổng currentSum được tạo thành nhờ chọn phần tử thứ i
                    // If at step i, sum currentSum was achieved by choosing item i
                    if (trace[i][currentSum]) {
                        // Kiểm tra xem liệu trạng thái trước đó (i-1, currentSum - val) có tồn tại không
                        // Check if previous state (i-1, currentSum - val) exists
                        // Thực ra trace[i][s] đã đảm bảo điều này rồi
                        result.add(a[i - 1]);
                        currentSum -= a[i - 1];
                    }
                }

                // Kết quả đang ở thứ tự ngược (từ cuối mảng về đầu), đảo lại để đúng thứ tự xuất hiện
                // Result is in reverse order (from end to start), reverse to match appearance order
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
