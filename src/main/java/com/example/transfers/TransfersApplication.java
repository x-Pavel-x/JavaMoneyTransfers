package com.example.transfers;

import com.example.transfers.classes.databases.h2.H2Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

//
@SpringBootApplication
public class TransfersApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(TransfersApplication.class, args);
		H2Database.getInstance().fillStartDatabase();
}
}
