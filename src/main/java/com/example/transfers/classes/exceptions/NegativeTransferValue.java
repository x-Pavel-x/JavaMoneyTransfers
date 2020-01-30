package com.example.transfers.classes.exceptions;
public class NegativeTransferValue extends Exception{
    public NegativeTransferValue(String message){
        super(message);
        this.printStackTrace();
    }
}