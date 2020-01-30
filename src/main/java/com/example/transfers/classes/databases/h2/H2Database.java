package com.example.transfers.classes.databases.h2;

import com.example.transfers.classes.User;
import com.example.transfers.classes.databases.Transferable;
import com.example.transfers.classes.exceptions.NegativeBalanceException;
import com.example.transfers.classes.exceptions.NegativeTransferValue;
import com.example.transfers.classes.exceptions.NonexistentUserException;
import com.example.transfers.classes.exceptions.SelfTransferException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class H2Database implements Transferable {
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
        private static H2Database instance;
        private Connection db;
        private H2Database() throws SQLException{
            log.log(Level.INFO, "Create new H2 database");
            this.db = H2Connection.getInstance().getH2Connection();
        }
        public static H2Database getInstance() throws SQLException{
            if(instance == null) {
                instance = new H2Database();
            }
            return instance;
        }
        public void fillStartDatabase() {
                try (Statement dataQuery = db.createStatement()) {
                    log.log(Level.INFO, "Start filling database");
                    dataQuery.execute("CREATE TABLE users (name VARCHAR(20), surname VARCHAR(20), id INT, balance BIGINT)");
                    dataQuery.execute("INSERT INTO users VALUES ('name1','surname1',1,1000)");
                    dataQuery.execute("INSERT INTO users VALUES ('name2','surname2',2,1000)");
                    dataQuery.execute("INSERT INTO users VALUES ('name3','surname3',3,1000)");
                    dataQuery.execute("INSERT INTO users VALUES ('name4','surname4',4,1000)");
                    log.log(Level.INFO, "filling database over");
                }catch(SQLException ex){
                    log.log(Level.SEVERE, "Exception: ", ex);
                }
            }
        public ArrayList<User> showAllUsers(){
            log.log(Level.INFO, "Show all Users");
            ArrayList<User> allUsers = new ArrayList();
            try (Statement dataQuery = db.createStatement()) {
            ResultSet resultSet = dataQuery.executeQuery("SELECT * FROM users ORDER BY id");
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),resultSet.getString("surname"),
                     resultSet.getInt("id"),resultSet.getLong("balance"));
                allUsers.add(user);
            }
            }catch(SQLException ex){
                log.log(Level.SEVERE, "Exception: ", ex);
            }
        return allUsers;
        }
    @Override
        public ArrayList<User> transfer(int idReceiver, int idSender,long money) throws NegativeBalanceException, SelfTransferException, NegativeTransferValue, NonexistentUserException {
        log.log(Level.INFO, "Start transfer ");
            if(idReceiver == idSender){
                throw new SelfTransferException("Transfer cannot be made between the same user");
            }
            if(money<0){
                throw new NegativeTransferValue("Incorrect transfer value (less than 0)");
            }
        ArrayList<User> usersAfterTransfer = new ArrayList();
        try (Statement dataQuery = db.createStatement()) {

            ResultSet resultSet = dataQuery.executeQuery("SELECT id FROM users");
            HashSet<Integer> idFromDatabase = new HashSet();
            while (resultSet.next()){
                idFromDatabase.add(resultSet.getInt("id"));
            }
            if(!idFromDatabase.contains(idReceiver) || !idFromDatabase.contains(idSender)){
                throw new NonexistentUserException("User does not exist");
            }
            String sqlUpdate1 = " UPDATE users SET balance = ";
            String sqlUpdate2 = " WHERE id = ";
            String sqlSelect = "SELECT * FROM users WHERE id = ";
            String sqlTransaction = "BEGIN TRANSACTION;";
            long receiverBalance = 0;
            long senderBalance = 0;
            resultSet = dataQuery.executeQuery(sqlSelect + idSender);
            while (resultSet.next()) {
               senderBalance = resultSet.getLong("balance") - money;
               if(senderBalance<0){
                   throw new NegativeBalanceException("Not enough money for transfer");
               }
            }
            resultSet = dataQuery.executeQuery(sqlSelect + idReceiver);
            while (resultSet.next()) {
                receiverBalance = resultSet.getLong("balance") + money;
            }
            sqlTransaction += sqlUpdate1 + senderBalance + sqlUpdate2 + idSender + ";" +
                    sqlUpdate1 + receiverBalance + sqlUpdate2 + idReceiver + "; COMMIT;";
            dataQuery.execute(sqlTransaction);
            resultSet = dataQuery.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                if(resultSet.getInt("id") == idReceiver || resultSet.getInt("id" ) == idSender){
                User user = new User(resultSet.getString("name"),resultSet.getString("surname"),
                        resultSet.getInt("id"),resultSet.getLong("balance"));
                usersAfterTransfer.add(user);
                }
            }
        }catch(SQLException ex){
            log.log(Level.SEVERE, "Exception: ", ex);
        }
        log.log(Level.INFO, "Transfer over");
            return usersAfterTransfer;
        }
        public void dropTable(){
            log.log(Level.INFO, "Delete table 'users' ");
            try (Statement dataQuery = db.createStatement()) {
                dataQuery.execute("DROP TABLE users");
            }catch(SQLException ex){
                log.log(Level.SEVERE, "Exception: ", ex);
            }
        }

}
