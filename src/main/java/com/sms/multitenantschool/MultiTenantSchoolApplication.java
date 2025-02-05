package com.sms.multitenantschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiTenantSchoolApplication {

    public static void main(String[] args) {
        System.out.println("Welcome to Tenants");
        SpringApplication.run(MultiTenantSchoolApplication.class, args);
    }
//   System.out.println("Welcome muiti tenants school");

}
