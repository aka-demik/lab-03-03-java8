import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class URLWorker extends MyWorker {

    public URLWorker(String name, AtomicBoolean stop, Consumer<? super Integer> action, Predicate<? super Integer> filter) {
        super(name, stop, action, filter);
    }

    @Override
    Stream<String> getLines(String name) throws IOException {
        return new BufferedReader(new InputStreamReader(new URL(name).openStream())).lines();

    }
}
