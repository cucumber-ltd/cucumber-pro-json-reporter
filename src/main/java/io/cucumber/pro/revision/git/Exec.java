package io.cucumber.pro.revision.git;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Exec {
    private final Path dir;

    public Exec(Path dir) {
        this.dir = dir;
    }

    public List<String> cmd(String cmd) {
        try {
            List<String> stdoutLines = new ArrayList<String>();
            Process process = Runtime.getRuntime().exec(cmd, new String[0], dir.toFile());
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            String line;
            while ((line = stdout.readLine()) != null) {
                stdoutLines.add(line);
            }
            return stdoutLines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}