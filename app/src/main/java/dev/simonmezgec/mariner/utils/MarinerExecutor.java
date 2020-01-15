package dev.simonmezgec.mariner.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/** Class for executing tasks on a new thread. */
public class MarinerExecutor {

    private static final Object LOCK = new Object();
    private static MarinerExecutor sInstance;
    private final Executor executor;

    private MarinerExecutor(Executor executor) {
        this.executor = executor;
    }

    public static MarinerExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MarinerExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor executor() {
        return executor;
    }
}