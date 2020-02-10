package com.developersbreach.bakingapp;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * AppExecutors class let us create Executors for completing tasks given by creating threads
 * on main thread or background.
 */
public class AppExecutors {

    // Create single instance
    private static final Object LOCK = new Object();
    private static AppExecutors sINSTANCE;
    // Create an object which executes any given task.
    private final Executor mMainThread;
    private final Executor mNetworkIO;

    private AppExecutors(Executor mainThread, Executor networkIO) {
        this.mMainThread = mainThread;
        this.mNetworkIO = networkIO;
    }

    /**
     * Get single instance for executors and check for any current running tasks or executor in
     * background or main thread.
     * <p>
     * We call new executor only once.
     */
    public static AppExecutors getInstance() {
        if (sINSTANCE == null) {
            synchronized (LOCK) {
                sINSTANCE = new AppExecutors(new MainThreadExecutor(), Executors.newFixedThreadPool(3));
            }
        }
        return sINSTANCE;
    }

    // This executor runs on main thread, we call this executor using handler
    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    // This executor runs on main thread.
    public Executor mainThread() {
        return mMainThread;
    }

    // This executor used to do network operations.
    public Executor networkIO() {
        return mNetworkIO;
    }
}
