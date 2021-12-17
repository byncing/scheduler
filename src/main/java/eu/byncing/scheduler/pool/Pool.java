package eu.byncing.scheduler.pool;

import eu.byncing.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Pool {

    private final BlockingQueue<Runnable> queue;
    private final List<PoolRunnable> runnables = new ArrayList<>();

    private boolean running = true;

    public Pool(int pools) {
        this.queue = new ArrayBlockingQueue<>(pools);
        for (int i = 0; i < pools; i++) {
            PoolRunnable runnable = new PoolRunnable(queue);
            runnables.add(runnable);
            new Thread(runnable, "pool-" + i).start();
        }
    }

    public synchronized void execute(Runnable runnable) {
        if (running) queue.offer(runnable);
    }

    public synchronized void stop() {
        running = false;
        for (PoolRunnable runnable : runnables) runnable.stop();
    }

    public synchronized void waitTasks() {
        while (queue.size() > 0) Scheduler.blocking(1);
    }

    public BlockingQueue<Runnable> getQueue() {
        return queue;
    }

    public List<PoolRunnable> getRunnables() {
        return runnables;
    }

    public boolean isRunning() {
        return running;
    }
}