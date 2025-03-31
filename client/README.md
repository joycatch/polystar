# Running the Client

The client connects to one or more remote servers specified in `application.yml`. 
Each server is expected to expose a text file over a raw socket. 
Example configuration for the `application.yml` file:

```
servers:
  - host: localhost
    port: 9001
  - host: localhost
    port: 9002
```

Once the servers have been configured in the `application.yml` simply run:

```bash
mvn spring-boot:run
```