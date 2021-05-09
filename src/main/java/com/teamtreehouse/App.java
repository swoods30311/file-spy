package com.teamtreehouse;

import org.apache.tika.Tika;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class App {
    public static final String FILE_TYPE = "text/csv";
    public static final String DIR_TO_WATCH = "/Users/sandywoods/Downloads/tmp";

    public static void main(String[] args) throws Exception {
        Path dir = Paths.get(DIR_TO_WATCH);
        Tika tika = new Tika();
        WatchService watchService = FileSystems.getDefault().newWatchService();
        dir.register(watchService, ENTRY_CREATE);

        WatchKey key;
        do {
            key = watchService.take();

            key.pollEvents().stream().filter(e -> {
                Path fileName = (Path) e.context();
                String type = tika.detect(fileName.toString());
                return FILE_TYPE.equals(type);
            }).forEach(e ->
                    System.out.printf("File Found: %s%n", e.context())
            );
        } while (key.reset());
    }
}
