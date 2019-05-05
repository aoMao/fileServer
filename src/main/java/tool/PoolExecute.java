package tool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolExecute {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
