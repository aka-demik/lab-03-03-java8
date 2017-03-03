import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        AtomicInteger sum = new AtomicInteger(0);
        AtomicBoolean stop = new AtomicBoolean(false);
        Consumer<? super Integer> consumer = i -> {
            int currValue = sum.addAndGet(i);
            System.out.println("Current value " + currValue);
        };

        Predicate<? super Integer> filter = i -> (i > 0) && ((i & 1) == 0);


        Arrays.stream(args).forEach(s -> {
            MyWorker worker;
            if (s.startsWith("http://") || s.startsWith("https://")) {
                worker = new URLWorker(s, stop, consumer, filter);
            } else {
                worker = new FileWorker(s, stop, consumer, filter);
            }
            pool.submit(worker::work);
        });

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);
        if (!stop.get())
            System.out.println("Result: " + sum.get());
        else
            System.out.println("Terminated");
    }
}
