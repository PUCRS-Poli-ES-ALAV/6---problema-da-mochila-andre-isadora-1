import java.util.function.Supplier;

public class ProgramacaoDinamicaRelatorio {

    static int iterationCounter = 0;

    // FIBO-REC
    public static int fiboRec(int n) {
        iterationCounter++;
        if (n <= 1) return n;
        return fiboRec(n - 1) + fiboRec(n - 2);
    }

    // FIBO DP
    public static int fiboDP(int n) {
        iterationCounter = 0;
        if (n <= 1) return n;

        int[] f = new int[n + 1];
        f[0] = 0;
        f[1] = 1;

        for (int i = 2; i <= n; i++) {
            iterationCounter++;
            f[i] = f[i - 1] + f[i - 2];
        }

        return f[n];
    }

    // FIBO MEMOIZED
    public static int memoizedFibo(int n) {
        iterationCounter = 0;
        int[] f = new int[n + 1];
        for (int i = 0; i <= n; i++) f[i] = -1;
        return lookupFibo(f, n);
    }

    public static int lookupFibo(int[] f, int n) {
        iterationCounter++;
        if (f[n] >= 0) return f[n];
        if (n <= 1) f[n] = n;
        else f[n] = lookupFibo(f, n - 1) + lookupFibo(f, n - 2);
        return f[n];
    }

    // MOCHILA RECURSIVA
    public static int mochilaRec(int[] w, int[] v, int cap, int n) {
        iterationCounter++;
        if (n == 0 || cap == 0) return 0;
        if (w[n - 1] > cap)
            return mochilaRec(w, v, cap, n - 1);
        else
            return Math.max(v[n - 1] + mochilaRec(w, v, cap - w[n - 1], n - 1),
                            mochilaRec(w, v, cap, n - 1));
    }

    // MOCHILA DP
    public static int mochilaDP(int[] weights, int[] values, int cap) {
        iterationCounter = 0;
        int n = weights.length;
        int[][] dp = new int[n + 1][cap + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= cap; j++) {
                iterationCounter++;
                if (weights[i - 1] <= j) {
                    dp[i][j] = Math.max(dp[i - 1][j],
                            values[i - 1] + dp[i - 1][j - weights[i - 1]]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[n][cap];
    }

    private static void runAndPrint(String algoritmo, String entrada, Supplier<Object> task, int runs) {
        long totalTime = 0;
        long totalMemory = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        Object result = null;

        Runtime runtime = Runtime.getRuntime();

        for (int i = 0; i < 3; i++) task.get(); // aquecimento

        for (int i = 0; i < runs; i++) {
            System.gc();
            long beforeMem = runtime.totalMemory() - runtime.freeMemory();
            long start = System.nanoTime();
            result = task.get();
            long end = System.nanoTime();
            long afterMem = runtime.totalMemory() - runtime.freeMemory();
            long usedMem = afterMem - beforeMem;
            totalMemory += usedMem;
            long duration = end - start;
            totalTime += duration;
            minTime = Math.min(minTime, duration);
            maxTime = Math.max(maxTime, duration);
        }

        double avgTime = totalTime / (double) runs / 1_000_000.0;
        double avgMem = totalMemory / (double) runs / 1024.0;

        System.out.printf("%-18s | %-6s | %-25s | %-10d | %-15.5f | %-16.3f%n",
                algoritmo, entrada, result.toString(), iterationCounter, avgTime, avgMem);
    }

    public static void main(String[] args) {
        int runs = 10;
        int[] inputs = {4, 8, 16, 32};
        int[] largeInputs = {128, 1000, 10_000};

        System.out.printf("%-18s | %-6s | %-25s | %-10s | %-15s | %-16s%n",
                "Algoritmo", "n", "Resultado", "Iterações", "Tempo Médio (ms)", "Memória Média (KB)");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (int n : inputs) {
            int finalN = n;
            runAndPrint("FiboRec", String.valueOf(n), () -> fiboRec(finalN), runs);
            runAndPrint("FiboDP", String.valueOf(n), () -> fiboDP(finalN), runs);
            runAndPrint("MemoFibo", String.valueOf(n), () -> memoizedFibo(finalN), runs);
        }

        for (int n : largeInputs) {
            int finalN = n;
            runAndPrint("FiboDP", String.valueOf(n), () -> fiboDP(finalN), runs);
            runAndPrint("MemoFibo", String.valueOf(n), () -> memoizedFibo(finalN), runs);
        }

        // Teste mochila
        int[] pesos = {10, 20, 30};
        int[] valores = {60, 100, 120};
        int capacidade = 50;

        runAndPrint("MochilaRecursiva", "3 itens", () -> mochilaRec(pesos, valores, capacidade, pesos.length), runs);
        runAndPrint("MochilaDP", "3 itens", () -> mochilaDP(pesos, valores, capacidade), runs);
    }
}
