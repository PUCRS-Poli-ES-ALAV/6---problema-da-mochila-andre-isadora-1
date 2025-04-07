import java.util.Arrays;

public class ProgramacaoDinamicaNano {
    
    
    public static int fiboRec(int n) {
        if (n <= 1) return n;
        return fiboRec(n - 1) + fiboRec(n - 2);
    }
    
    public static int fiboDP(int n) {
        if (n <= 1) return n;
        
        int[] f = new int[n + 1];
        f[0] = 0;
        f[1] = 1;
        
        for (int i = 2; i <= n; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }
        
        return f[n];
    }
    
    
    public static int knapsackDP(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= capacity; j++) {
                if (weights[i - 1] <= j) {
                    dp[i][j] = Math.max(dp[i - 1][j], 
                                      values[i - 1] + dp[i - 1][j - weights[i - 1]]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        
        return dp[n][capacity];
    }
    
    
    public static int editDistanceDP(String A, String B) {
        int m = A.length();
        int n = B.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) dp[i][0] = dp[i - 1][0] + 1;
        for (int j = 1; j <= n; j++) dp[0][j] = dp[0][j - 1] + 1;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (A.charAt(i - 1) == B.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], 
                                          Math.min(dp[i][j - 1], 
                                                  dp[i - 1][j - 1]));
                }
            }
        }
        
        return dp[m][n];
    }
    
    private static void measurePreciseTime(String algorithmName, Runnable task, int iterations) {
        long totalTime = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        
        for (int i = 0; i < 3; i++) {
            task.run();
        }
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            task.run();
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            
            totalTime += duration;
            if (duration < minTime) minTime = duration;
            if (duration > maxTime) maxTime = duration;
        }
        
        double avgTime = totalTime / (double) iterations;
        
        System.out.printf("%s:%n", algorithmName);
        System.out.printf("  Média: %6.2f ns | Min: %d ns | Max: %d ns%n", 
                         avgTime, minTime, maxTime);
        System.out.printf("  Média: %6.3f μs | Min: %.3f μs | Max: %.3f μs%n%n", 
                         avgTime/1000.0, minTime/1000.0, maxTime/1000.0);
    }
    
    public static void main(String[] args) {
        // Configuração
        
        final int runs = 100;    
        
        System.out.println("=== Testes de Desempenho (nanossegundos) ===\n");
        
        // Testes Fibonacci
        System.out.println("1. Algoritmos Fibonacci (n=16):");
        measurePreciseTime("Fibonacci Recursivo", () -> fiboRec(16), runs);
        measurePreciseTime("Fibonacci DP", () -> fiboDP(16), runs);
        
        System.out.println("2. Problema da Mochila:");
        int[] weights = {10, 20, 30};
        int[] values = {60, 100, 120};
        measurePreciseTime("Mochila DP (3 itens)", 
                          () -> knapsackDP(weights, values, 50), runs);
        
        System.out.println("3. Distância de Edição:");
        String s1 = "Casablanca";
        String s2 = "Portentoso";
        measurePreciseTime("Distância Edição DP", 
                          () -> editDistanceDP(s1, s2), runs);
    }
}