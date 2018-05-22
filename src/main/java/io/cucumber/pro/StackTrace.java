package io.cucumber.pro;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTrace {
    public static String get(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
