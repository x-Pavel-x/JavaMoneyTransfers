package com.example.transfers.classes.exceptions;
public class NegativeBalanceException extends Exception{
    public NegativeBalanceException(String message){
        super(message);
        this.printStackTrace();
    }
}