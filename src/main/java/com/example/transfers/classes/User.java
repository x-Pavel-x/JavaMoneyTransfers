package com.example.transfers.classes;

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
    public boolean ra(User o){
        return this.id==o.id;
    }
    @Override
    public boolean equals(Object o){
        return this.hashCode() == o.hashCode();
    }
    @Override
    public int hashCode(){
        return this.name.hashCode()+this.surname.hashCode()+balance.hashCode()+id.hashCode();
    }
}
