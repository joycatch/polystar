# Running the Server

To serve different files on different ports, start **two instances** of the application with different configuration values.

### 1. Run Server for `frankenstein.txt` on port `9001`

```bash
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=9001 --server.filePath=src/main/resources/frankenstein.txt"
```

### 2. Run Server for `dracula.txt` on port `9002`

```bash
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=9002 --server.filePath=src/main/resources/dracula.txt"
```