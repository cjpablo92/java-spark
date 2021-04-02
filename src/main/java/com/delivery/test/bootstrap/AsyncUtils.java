package com.pedidosya.test.bootstrap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class AsyncUtils {
    private static final int EXECUTOR_CORE_POOL_SIZE = 50;
    private static final int EXECUTOR_MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    private static final long EXECUTOR_KEEP_ALIVE_TIME = 60L;

    private static final int SCHEDULER_CORE_POOL_SIZE = 1;

    private static final ExecutorService executorService;
    private static final ScheduledExecutorService scheduledExecutorService;

    static {
        executorService = createExecutor();
        scheduledExecutorService = createScheduledExecutorService();
    }

    private static ExecutorService createExecutor() {
        return new ThreadPoolExecutor(EXECUTOR_CORE_POOL_SIZE, EXECUTOR_MAXIMUM_POOL_SIZE,
                EXECUTOR_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    private static ScheduledExecutorService createScheduledExecutorService() {
        return Executors.newScheduledThreadPool(SCHEDULER_CORE_POOL_SIZE);
    }

    public static <T> CompletableFuture<T> supply(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executorService);
    }

    public static CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executorService);
    }

    public static void scheduleAtFixedRate(Runnable runnable, long fixRate) {
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, fixRate, TimeUnit.MILLISECONDS);
    }

    public static void waitFor(CompletableFuture<?>... cfs) {
        waitFor(Arrays.asList(cfs));
    }

    public static <T extends CompletableFuture<?>> void waitFor(Collection<T> cfs) {
        if (cfs == null) {
            return;
        }
        CompletableFuture[] cfsArray = cfs.stream()
                .filter(Objects::nonNull)
                .map(cf -> cf.exceptionally(t -> null))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(cfsArray).join();
    }
}
