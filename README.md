# scheduler
A scheduler task

# Installation

### Gradle

````gradle
repositories {
    mavenCentral()
    maven { url('https://byncing.eu/repository/') }
}

dependencies {
    implementation('eu.byncing:scheduler:1.0.0-SNAPSHOT')
}
````
# Example

````java
public class Test {

    public static void main(String[] args) {
        //create a scheduler instance
        Scheduler scheduler = new Scheduler();
        
        //run an async task
        scheduler.runAsync(() -> System.out.println("!This is an async task!"));
        
        //run an timer task
        scheduler.runTimer(() -> System.out.println("Tick scheduler"), 1000);
        
        //run an delay task
        scheduler.runDelay(() -> System.out.println("Delay task"), 5000);
    }
}
````
