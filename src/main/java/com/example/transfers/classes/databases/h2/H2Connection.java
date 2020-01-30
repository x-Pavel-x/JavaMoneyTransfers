package com.example.transfers.classes.databases.h2;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class H2Connection {
    private static Logger log = Logger.getLogger(H2Connection.class.getName());
    static {
        try(FileInputStream ins = new FileInputStream("src\\main\\resources\\log.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
        }
        catch (IOException ex)
        {
            log.log(Level.SEVERE, "Exception: ", ex);
        }
    }
    private static H2Connection instance;
    private H2Connection(){}
    public static H2Connection getInstance(){
        if(instance == null) {
            instance = new H2Connection();
        }
        return instance;
    }
    public Connection getH2Connection() throws SQLException {
        log.log(Level.INFO, "Create new connection to H2 ");
        return DriverManager.getConnection("jdbc:h2:mem:");
    }
}
