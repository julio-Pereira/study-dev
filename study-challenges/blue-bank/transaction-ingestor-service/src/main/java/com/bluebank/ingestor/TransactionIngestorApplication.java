package com.bluebank.ingestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class TransactionIngestorApplication {

    public void main(String[] args) {
         SpringApplication.run(TransactionIngestorApplication.class, args);
    }
}
