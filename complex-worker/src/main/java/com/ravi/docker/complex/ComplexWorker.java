package com.ravi.docker.complex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;

import static java.lang.Integer.parseInt;

@Component
public class ComplexWorker {

    private final Jedis j;

    @Autowired
    public ComplexWorker(@Value("${redis.host}") String host,
                         @Value("${redis.port}") int port) {
        j = new Jedis(host, port);
    }

    @PostConstruct
    public void init() {
        System.out.println("***************** INIT of worker ****************");
        j.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                j.hset("values", message, String.valueOf(fib(parseInt(message))));
            }
        }, "insert");
    }

    private static int fib(int i) {
        if(i < 2) return 1;
        else return fib(i - 1) + fib(i - 2);
    }
}
