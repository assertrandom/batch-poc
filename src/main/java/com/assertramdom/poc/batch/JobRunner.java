package com.assertramdom.poc.batch;

import com.assertramdom.poc.batch.domain.Furniture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created on 09/05/15.
 */
@SpringBootApplication
public class JobRunner implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(JobRunner.class, args);
    }


    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public void run(String... strings) throws Exception {
        List<Furniture> results = jdbcTemplate.query("SELECT name, cost FROM furniture", new RowMapper<Furniture>() {
            @Override
            public Furniture mapRow(ResultSet rs, int row) throws SQLException {
                return new Furniture(rs.getString(1), rs.getLong(2));
            }
        });

        for (Furniture person : results) {
            System.out.println("Found <" + person + "> in the database.");
        }
    }
}
