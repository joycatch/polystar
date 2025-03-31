package com.elisapolystar.codetest.server.socket;

import com.elisapolystar.codetest.server.config.FileServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class FileSocketServer implements SmartLifecycle {

    private final FileServerProperties properties;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;
    private volatile boolean running = false;

    public FileSocketServer(FileServerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void start() {
        executor.submit(() -> {
            try (ServerSocket server = new ServerSocket(properties.port())) {
                this.serverSocket = server;
                this.running = true;
                log.info("Socket server started on port " + properties.port());

                while (!Thread.currentThread().isInterrupted()) {
                    Socket client = server.accept();
                    log.info("Accepted connection from {}", client.getRemoteSocketAddress());
                    executor.submit(new ServerWorker(client, properties.filePath()));
                }
            } catch (IOException e) {
                if (running) {
                    log.error("Server error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void stop() {
        this.running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            executor.shutdownNow();
        } catch (IOException e) {
            log.error("Error shutting down server: " + e.getMessage());
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }
}