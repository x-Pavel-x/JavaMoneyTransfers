package com.example.transfers.classes;

import java.util.Objects;

public class User {
    String name;
    String surname;
    Integer id;
    Long balance;
    public User (String name, String surname, int id, long balance){
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.balance = balance;
    }
    public long getBalance(){
        return balance;
    }
   public String getName(){
        return name;
   }
   public String getSurname(){
        return surname;
   }
    public int getId() {
        return id;
    }
    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                surname.equals(user.surname) &&
                id.equals(user.id) &&
                balance.equals(user.balance);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, surname, id, balance);
    }
}
