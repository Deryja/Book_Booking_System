package com.example.book_booking_system_version3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class BookBookingSystemVersion3Application {

    public static void main(String[] args) {
        SpringApplication.run(BookBookingSystemVersion3Application.class, args);
    }

}
