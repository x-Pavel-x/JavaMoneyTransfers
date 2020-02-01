package com.example.transfers;

import com.example.transfers.classes.databases.h2.H2Connection;
import com.example.transfers.classes.databases.h2.H2Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

@SpringBootApplication
public class TransfersApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(TransfersApplication.class, args);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		H2Database h2Database = context.getBean("h2DatabaseBean", H2Database.class);
		h2Database.fillStartDatabase();
}
}
