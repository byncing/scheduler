package eu.byncing.scheduler.pool;

import eu.byncing.scheduler.Scheduler;

import java.util.concurrent.BlockingQueue;

public class PoolRunnable implements Runnable {

    private Thread thread;
    private final BlockingQueue<Runnable> queue;
    private boolean running = false;

    public PoolRunnable(BlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        thread = Thread.currentThread();
        running = true;
        while (running) {
            try {
                Runnable runnable = queue.take();
                runnable.run();
                Scheduler.blocking(1);
            } catch (Exception ignored) {
            }
        }
    }

    public synchronized void stop() {
        running = false;
        thread.interrupt();
    }

    public Thread getThread() {
        return thread;
    }

    public BlockingQueue<Runnable> getQueue() {
        return queue;
    }

    public boolean isRunning() {
        return running;
    }
}