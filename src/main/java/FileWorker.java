import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FileWorker extends MyWorker {
    public FileWorker(String name, AtomicBoolean stop, Consumer<? super Integer> action, Predicate<? super Integer> filter) {
        super(name, stop, action, filter);
    }

    @Override
    Stream<String> getLines(String name) throws IOException {
        return Files.lines(Paths.get(name));
    }
}
