package eu.byncing.scheduler;

import eu.byncing.scheduler.pool.Pool;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private final List<SchedulerTask> tasks = new ArrayList<>();

    private final List<Pool> pools = new ArrayList<>();

    private final Pool pool;

    public Scheduler(int pools) {
        this.pool = this.pool(pools);
    }

    public Scheduler() {
        this(3);
    }

    public SchedulerTask runAsync(Runnable runnable) {
        pool.execute(runnable);
        return new SchedulerTask(tasks.size(), runnable);
    }

    public SchedulerTask runTimer(Runnable runnable, int delay, int period) {
        return run(new SchedulerTask(tasks.size(), runnable), delay, period);
    }

    public SchedulerTask runTimer(Runnable runnable, int period) {
        return run(new SchedulerTask(tasks.size(), runnable), 0, period);
    }

    public SchedulerTask runDelay(Runnable runnable, int delay) {
        return run(new SchedulerTask(tasks.size(), runnable), delay, 0);
    }

    public Pool pool(int pools) {
        Pool pool = new Pool(pools);
        this.pools.add(pool);
        return pool;
    }

    public SchedulerTask run(SchedulerTask task, int delay, int period) {
        new Thread(() -> {
            long current = System.currentTimeMillis();
            task.start();
            tasks.add(task);
            while (task.isRunning()) {
                if (System.currentTimeMillis() - current > delay) {
                    if (period <= 0) {
                        if (task.isRunning()) task.run();
                        break;
                    }
                    current = System.currentTimeMillis();
                    while (task.isRunning()) {
                        if (System.currentTimeMillis() - current > period) {
                            current += period;
                            if (task.isRunning()) task.run();
                            else break;
                        }
                        blocking(1);
                    }
                    break;
                }
                blocking(1);
            }
            cancel(task.getId());
        }, "Scheduler-" + task.getId()).start();
        return task;
    }

    public static void blocking(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ignored) {
        }
    }

    public void cancel(int id) {
        SchedulerTask task = getTask(id);
        if (task == null) return;
        task.cancel();
        tasks.remove(task);
    }

    public void cancel() {
        pool.waitTasks();
        pool.stop();

        for (int i = pools.size() - 1; i >= 0; i--) {
            Pool pool = pools.get(i);
            pool.waitTasks();
            pool.stop();
        }
        pools.clear();

        for (int i = tasks.size() - 1; i >= 0; i--) cancel(i);
        tasks.clear();
    }

    public SchedulerTask getTask(int id) {
        return tasks.stream().filter(tasks -> tasks.getId() == id).findFirst().orElse(null);
    }

    public List<SchedulerTask> getTasks() {
        return tasks;
    }
}