# Spring Boot Redis Connection Issue Reproduction (3.4.x+)

This project demonstrates an issue where Spring Boot's Redis auto-configuration fails to apply values defined in `application.yml` when using `LettuceConnectionFactory`.

---

## Problem Description

- `application.yml` explicitly sets `spring.redis.host=redis`
- However, `LettuceConnectionFactory()` attempts to connect to `localhost:6379`
- Only works if we manually inject host/port via `@Value`
- Occurs on Spring Boot **3.4.0** (also reproduced on 3.5.0)

### Expected

Spring Boot should respect `application.yml` and connect to the proper Redis host (`redis`)

### Actual Behavior

Connection attempt is made to `localhost:6379`, resulting in:

io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: localhost/127.0.0.1:6379

## How to Reproduce

### 1. Run with Docker

```bash
docker-compose up --build
```

Redis container starts as expected
App fails with localhost:6379 connection error

### Note
In the actual GitHub commit, the @Value fields are commented out to reproduce the issue.
If you uncomment these lines, the Redis connection will work correctly, proving that the problem is related to when or how the Spring configuration properties are applied.

``` java
@Configuration
public class RedisConfig {

    // 💡 These are commented out in the committed version to simulate the bug.
    // Uncommenting them will cause the application to connect properly to Redis (using `spring.redis.host`).

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }
}
```

## Conclusion
This behavior suggests LettuceConnectionFactory might be initializing before configuration properties are bound — or auto-config is being bypassed unexpectedly.

Please let us know if further minimal tweaks are needed — or if this should be considered an internal misconfiguration
