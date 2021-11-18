package eu.byncing.scheduler.test;

import eu.byncing.scheduler.Scheduler;

public class Test {

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        scheduler.runTimer(() -> System.out.println("tick scheduler"), 1000);
    }
}