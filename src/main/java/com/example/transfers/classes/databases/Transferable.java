package com.example.transfers.classes.databases;

import com.example.transfers.classes.User;
import com.example.transfers.classes.exceptions.NegativeBalanceException;
import com.example.transfers.classes.exceptions.NegativeTransferValue;
import com.example.transfers.classes.exceptions.SelfTransferException;

import java.util.ArrayList;

public interface Transferable {
public ArrayList<User> transfer(int idReceiver, int idSender, long money) throws Exception;
}
