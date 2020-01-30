package com.example.transfers;

import com.example.transfers.classes.User;
import com.example.transfers.classes.databases.h2.H2Database;
import com.example.transfers.classes.exceptions.NegativeBalanceException;
import com.example.transfers.classes.exceptions.NegativeTransferValue;
import com.example.transfers.classes.exceptions.NonexistentUserException;
import com.example.transfers.classes.exceptions.SelfTransferException;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransfersApplicationTests {
	private static Logger log = Logger.getLogger(TransfersApplicationTests.class.getName());
	static {
		try(FileInputStream ins = new FileInputStream("src\\main\\resources\\log.properties")) {
			LogManager.getLogManager().readConfiguration(ins);
		}
		catch (IOException ex)
		{
			log.log(Level.SEVERE, "Exception: ", ex);
		}
	}
	@BeforeEach
		void setUp() throws SQLException {
		H2Database.getInstance().fillStartDatabase();
	}
	@AfterEach
		void tearDown() throws SQLException{
		H2Database.getInstance().dropTable();
	}
	@Autowired
	private MockMvc mockMvc;
	@Test
	void testGet() throws Exception{
        log.log(Level.INFO, "testGet start");
	this.mockMvc.perform(get("/transfer"))
			.andDo(print())
			.andExpect(status().isOk());
        log.log(Level.INFO, "testGet over");
}
	@Test
	void testPatch() throws Exception{
        log.log(Level.INFO, "testPatch start");
	MockHttpServletRequestBuilder builder =
			patch("/transfer")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.accept(MediaType.APPLICATION_JSON)
					.characterEncoding("UTF-8")
					.content("{\n" +
							"\t\"idReceiver\": \"3\",\n" +
							"\t\"idSender\": \"2\",\n" +
							"\t\"money\": \"500\"\n" +
							"}");
		this.mockMvc.perform(builder)
				.andDo(print())
				.andExpect(status().isOk());
        log.log(Level.INFO, "testPatch over");
	}
	@Test
	void testTransfer() throws SQLException, NegativeBalanceException, SelfTransferException, NegativeTransferValue, NonexistentUserException {
        log.log(Level.INFO, "testTransfer start");
	ArrayList<User> actualUsers = new ArrayList();
	ArrayList<User> expectedUsers = H2Database.getInstance().transfer(1, 4, 500);
	User user = new User("name4", "surname4", 4, 500);
		actualUsers.add(user);
	user = new User("name1", "surname1", 1, 1500);
		actualUsers.add(user);
	Assert.assertEquals(expectedUsers, actualUsers);
        log.log(Level.INFO, "testTransfer over");
	}
	@Test
	void testNegativeBalanceException() throws SQLException, SelfTransferException, NegativeTransferValue, NonexistentUserException {
        log.log(Level.INFO, "testNegativeBalanceException start");
	    NegativeBalanceException negativeBalanceException = null;
		try{
			H2Database.getInstance().transfer(1, 4, 5000000);
		}catch (NegativeBalanceException ex){
			negativeBalanceException = ex;
            log.log(Level.WARNING, "Exception: ", ex);
		}
		Assert.assertNotNull(negativeBalanceException);
        log.log(Level.INFO, "testNegativeBalanceException over");
	}
	@Test
	void testSelfTransferException() throws SQLException, NegativeBalanceException, NegativeTransferValue, NonexistentUserException {
        log.log(Level.INFO, "testSelfTransferException start");
	    SelfTransferException selfTransferException = null;
		try{
			H2Database.getInstance().transfer(1, 1, 100);
		}catch (SelfTransferException ex){
			selfTransferException = ex;
            log.log(Level.WARNING, "Exception: ", ex);
		}
		Assert.assertNotNull(selfTransferException);
        log.log(Level.INFO, "testSelfTransferException over");
	}
	@Test
	void testNegativeTransferValue() throws SQLException, NegativeBalanceException, SelfTransferException, NonexistentUserException {
        log.log(Level.INFO, "testNegativeTransferValue start");
	    NegativeTransferValue negativeTransferValue = null;
		try{
			H2Database.getInstance().transfer(1, 4, -500);
		}catch (NegativeTransferValue ex){
			negativeTransferValue = ex;
            log.log(Level.WARNING, "Exception: ", ex);
		}
		Assert.assertNotNull(negativeTransferValue);
        log.log(Level.INFO, "testNegativeTransferValue over");
	}
	@Test
	void NonexistentUserException() throws SQLException, NegativeBalanceException, SelfTransferException, NegativeTransferValue {
        log.log(Level.INFO, "NonexistentUserException start");
	    NonexistentUserException nonexistentUserException1 = null;
		NonexistentUserException nonexistentUserException2 = null;
		NonexistentUserException nonexistentUserException3 = null;
		try{
			H2Database.getInstance().transfer(1, 8, 50);
		}catch (NonexistentUserException ex){
			nonexistentUserException1 = ex;
            log.log(Level.WARNING, "Exception: ", ex);
		}
		try{
			H2Database.getInstance().transfer(7, 2, 50);
		}catch (NonexistentUserException ex){
			nonexistentUserException2 = ex;
            log.log(Level.WARNING, "Exception: ", ex);
		}
		try{
			H2Database.getInstance().transfer(9, 7, 50);
		}catch (NonexistentUserException ex){
			nonexistentUserException3 = ex;
            log.log(Level.WARNING, "Exception: ", ex);
		}
		Assert.assertNotNull(nonexistentUserException1);
		Assert.assertNotNull(nonexistentUserException2);
		Assert.assertNotNull(nonexistentUserException3);
        log.log(Level.INFO, "NonexistentUserException over");
	}
}
