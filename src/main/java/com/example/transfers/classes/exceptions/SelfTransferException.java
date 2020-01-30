package com.example.transfers.classes.exceptions;
public class SelfTransferException extends Exception {
    public SelfTransferException(String message){
        super(message);
        this.printStackTrace();
    }
}
