package eu.byncing.scheduler.pool;

import java.util.concurrent.BlockingQueue;

public class PoolRunnable implements Runnable {

    private Thread thread;
    private BlockingQueue<Runnable> queue;
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