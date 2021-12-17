# scheduler
A scheduler task

# Installation

### Gradle

````gradle
repositories {
    mavenCentral()
    maven { {
        url('http://repo.byncing.eu/snapshots/') }
        allowInsecureProtocol(true)
    }
}

dependencies {
    implementation('eu.byncing:scheduler:1.0.2-SNAPSHOT')
}
````
# Example

````java
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
````