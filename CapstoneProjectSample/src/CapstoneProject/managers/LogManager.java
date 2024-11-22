package CapstoneProject.managers;

import java.util.ArrayList;
import java.util.List;

public class LogManager {
    private static final List<String> logs = new ArrayList<>();

    public static void addLog(String log) {
        logs.add(log);
    }

    public static void showLogs() {
        System.out.println("\nLogs:");
        for (String log : logs) {
            System.out.println(log);
        }
    }
}

