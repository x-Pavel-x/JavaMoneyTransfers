package com.example.transfers.classes.exceptions;

public class NonexistentUserException extends Exception{
    public NonexistentUserException(String message){
        super(message);
        this.printStackTrace();
    }
}