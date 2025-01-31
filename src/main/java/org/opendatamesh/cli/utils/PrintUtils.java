package org.opendatamesh.cli.utils;

import java.io.OutputStream;
import java.io.PrintStream;

import com.google.common.base.Supplier;

public class PrintUtils {

    public static <T> T silentExecution(Supplier<T> supplier) throws Exception {
        // Redirect standard output and error to null
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                // No-op to suppress output
            }
        }));
        System.setErr(new PrintStream(new OutputStream() {
            public void write(int b) {
                // No-op to suppress output
            }
        }));

        try {
            return supplier.get();
        } finally {
            // Reset standard output and error to original streams
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }

    @FunctionalInterface
    public interface ExceptionThrowingRunnable {
        void run() throws Exception;
    }

}
