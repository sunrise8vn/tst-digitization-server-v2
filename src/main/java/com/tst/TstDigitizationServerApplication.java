package com.tst;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class TstDigitizationServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(TstDigitizationServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TstDigitizationServerApplication.class, args);
        logger.info("\uD83D\uDE80 TST Digitization Server Application Started........");
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        logger.info("\uD83D\uDE80 Date in UTC: " + new Date());
    }

}
