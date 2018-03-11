package com.anoop.transformpdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransformPdfApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransformPdfApplication.class, args);
	}
}
