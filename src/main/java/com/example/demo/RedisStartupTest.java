package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisStartupTest implements CommandLineRunner {

    private final RedisConnectionFactory factory;

    public RedisStartupTest(RedisConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Try Access Redis");
        RedisConnection connection = factory.getConnection();
        System.out.println("Redis Access Success: " + connection.ping());
    }
}
