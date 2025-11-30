package com.easymeeting.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 本地命令执行工具，封装 Java 调用系统 cmd/terminal。
 */
public final class ProcessUtils {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    private ProcessUtils() {
    }

    /**
     * 执行命令，无需返回值。
     */
    public static void executeCommand(String command) {
        executeCommandWithResult(command);
    }

    /**
     * 执行命令并返回标准输出内容（包含错误输出）。
     */
    public static String executeCommandWithResult(String command) {
        try {
            Process process = new ProcessBuilder(buildShell(command))
                    .redirectErrorStream(true)
                    .start();

            String output = read(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("执行命令失败：" + command + "，输出：" + output);
            }
            return output;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("执行命令异常：" + command, e);
        }
    }

    private static String[] buildShell(String command) {
        if (OS.contains("win")) {
            return new String[]{"cmd.exe", "/c", command};
        }
        return new String[]{"/bin/bash", "-c", command};
    }

    private static String read(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            return sb.toString().trim();
        }
    }
}

