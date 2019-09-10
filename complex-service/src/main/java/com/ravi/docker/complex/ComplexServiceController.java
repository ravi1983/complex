package com.ravi.docker.complex;

import lombok.Data;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

@RestController
public class ComplexServiceController {

    private final Jdbi jdbi;
    private final Jedis j;

    @Autowired
    public ComplexServiceController(Jdbi jdbi,
                                    @Value("${redis.host}") String host,
                                    @Value("${redis.port}") int port) {
        this.jdbi = jdbi;

        // Create jedis instance and publisher
        j = new Jedis(host, port);

        // Create table in postgress
        jdbi.useHandle(h -> h.execute("create table if not exists values (number INT)"));
    }

    @GetMapping("/")
    @CrossOrigin(origins = "*")
    public String test() {
        return "Working!";
    }

    @GetMapping("/values/all")
    @CrossOrigin(origins = "*")
    public List<Integer> allValues() {
        List<Integer> vals = jdbi.withHandle(h -> h.createQuery("select number from values")
                .mapTo(Integer.class)
                .list());
        return vals;
    }

    @GetMapping("/values/current")
    @CrossOrigin(origins = "*")
    public Map<String, String> currentValue() {
        return j.hgetAll("values");
    }

    @PostMapping("/values")
    @CrossOrigin(origins = "*")
    public void values(@RequestBody Request r) {
        int i = r.getIndex();

        if(i > 40) {
            throw new RuntimeException("Too high");
        }
        j.hset("values", String.valueOf(i), "Nothing yet");
        j.publish("insert", String.valueOf(i));

        jdbi.withHandle(h -> h.createUpdate("insert into values(number) VALUES(?)")
                .bind(0, i)
                .execute());
    }

    @Data
    static class Request {
        private int index;
    }
}
