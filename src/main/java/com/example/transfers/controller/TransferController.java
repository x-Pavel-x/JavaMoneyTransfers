package com.example.transfers.controller;

import com.example.transfers.classes.User;
import com.example.transfers.classes.databases.h2.H2Database;
import com.example.transfers.classes.exceptions.NegativeBalanceException;
import com.example.transfers.classes.exceptions.NegativeTransferValue;
import com.example.transfers.classes.exceptions.NonexistentUserException;
import com.example.transfers.classes.exceptions.SelfTransferException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@RequestMapping("transfer")
public class TransferController {
    private static Logger log = Logger.getLogger(H2Database.class.getName());
    static {
        try(FileInputStream ins = new FileInputStream("src\\main\\resources\\log.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
        }
        catch (IOException ex)
        {
            log.log(Level.SEVERE, "Exception: ", ex);
        }
    }
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    H2Database h2Database = context.getBean("h2DatabaseBean", H2Database.class);
    @GetMapping
    public ArrayList<User> show() throws SQLException {

        log.log(Level.INFO, "GET request ");
        return h2Database.showAllUsers();
    }
    @PatchMapping
    public ArrayList<User> moneyTransfer(@RequestBody Map <String, String> transferInfo) {
       log.log(Level.INFO, "PATCH request ");
       ArrayList<User> result = new ArrayList();
       try{
           result = h2Database.transfer(
                Integer.parseInt(transferInfo.get("idReceiver")),
                Integer.parseInt(transferInfo.get("idSender")),
                Long.parseLong(transferInfo.get("money")));
       }catch (NegativeBalanceException | SelfTransferException | NegativeTransferValue | NonexistentUserException ex){
           log.log(Level.WARNING, "Exception: ", ex);
       }
       finally {
            return result;
       }
    }
}
