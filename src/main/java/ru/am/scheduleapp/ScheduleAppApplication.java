package ru.am.scheduleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ScheduleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleAppApplication.class, args);
	}

}
