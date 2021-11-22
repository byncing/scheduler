package eu.byncing.scheduler.test;

import eu.byncing.scheduler.Scheduler;
import eu.byncing.scheduler.pool.Pool;

public class Test {

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        Pool pool = scheduler.pool(3);

        scheduler.runTimer(() -> {
            pool.execute(() -> System.out.println(Thread.currentThread().getName() + ": task run"));
            pool.execute(() -> System.out.println(Thread.currentThread().getName() + ": task run"));
        }, 0, 500);
    }
}