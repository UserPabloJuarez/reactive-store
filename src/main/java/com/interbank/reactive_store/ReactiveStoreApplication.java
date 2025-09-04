package com.interbank.reactive_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ReactiveStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveStoreApplication.class, args);
	}

}
