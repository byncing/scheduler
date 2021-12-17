package eu.byncing.scheduler.test;

import eu.byncing.scheduler.Scheduler;
import eu.byncing.scheduler.pool.Pool;

public class Test {

    public static void main(String[] args) {
        //create a scheduler with 3 pools
        Scheduler scheduler = new Scheduler();

        //make a async task
        scheduler.runAsync(() -> System.out.println("async task"));

        //create 3 pools
        Pool pool = scheduler.pool(3);

        scheduler.runTimer(() -> {
            pool.execute(() -> System.out.println(Thread.currentThread().getName() + ": task run"));
            pool.execute(() -> System.out.println(Thread.currentThread().getName() + ": task run"));
        }, 0, 500);

        //close the scheduler on 5 seconds!
        scheduler.runDelay(scheduler::cancel, 5000);
    }
}