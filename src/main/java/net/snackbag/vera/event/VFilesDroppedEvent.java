package net.snackbag.vera.event;

import java.nio.file.Path;
import java.util.List;

@FunctionalInterface
public interface VFilesDroppedEvent {
    void run(List<Path> paths);
}
