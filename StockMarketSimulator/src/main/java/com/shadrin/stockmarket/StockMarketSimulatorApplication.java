package com.shadrin.stockmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockMarketSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockMarketSimulatorApplication.class, args);
    }

}
