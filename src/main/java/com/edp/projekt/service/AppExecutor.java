package com.edp.projekt.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutor {
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public static void shutdown() {
        executor.shutdown();
        scheduler.shutdown();
    }
}
