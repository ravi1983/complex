package com.ravi.docker.complex;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.spring4.JdbiFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import static java.lang.String.format;

/**
 * Hello world!
 *
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class App {

    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public Jdbi jdbi(@Value("${postgress.host}") String host,
                     @Value("${postgress.port}") String port,
                     @Value("${postgress.db}") String db,
                     @Value("${postgress.user}") String user,
                     @Value("${postgress.password}") String password) throws Exception {
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(format("jdbc:postgresql://%s:%s/%s", host, port, db));
        ds.setUsername(user);
        ds.setPassword(password);

        return new JdbiFactoryBean(ds).getObject();
    }
}
