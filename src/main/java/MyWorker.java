import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

abstract class MyWorker {
    private AtomicBoolean stop;
    private Consumer<? super Integer> action;
    private Predicate<? super Integer> filter;
    private String name;

    public MyWorker(String name, AtomicBoolean stop, Consumer<? super Integer> action, Predicate<? super Integer> filter) {
        this.stop = stop;
        this.action = action;
        this.name = name;
        this.filter = filter;
    }

    abstract Stream<String> getLines(String name) throws IOException;

    boolean work() throws IOException {
        Stream<String> lines = getLines(name);

        lines.parallel()
                .filter(s -> !stop.get())
                .forEach(s -> Arrays.stream(s.split("\\s"))
                        .map(s1 -> {
                            try {
                                return Integer.parseInt(s1);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input data " + s1);
                                stop.set(true);
                                throw e;
                            }
                        })
                        .filter(filter)
                        .forEach(action));
        return true;
    }
}
