package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TextChat chat = new TextChat();

        User admin = new Admin(chat, "Maximus");
        User u1 = new SimpleUser(chat, "Anna");
        User u2 = new SimpleUser(chat, "Alex");
        User u3 = new SimpleUser(chat, "Daniel");
        u2.setEnable(false);

        chat.setAdmin(admin);
        chat.addUser(u1);
        chat.addUser(u2);
        chat.addUser(u3);

        u1.sendMessage("Hello!");
        admin.sendMessage("Hello all!");
    }
}

abstract class User{
    private Chat chat;
    private String name;
    private boolean isEnable = true;

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public User(Chat chat, String name){
        this.chat = chat;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void sendMessage(String message){
        chat.sendMessage(message, this);
    }
    abstract void getMessage(String message);
}

class Admin extends User{
    public Admin(Chat chat, String name){
        super(chat, name);
    }

    public void getMessage(String message){
        System.out.println("Administrator " + getName() + " got message '" + message + "'");
    }
}

class SimpleUser extends User{
    public SimpleUser(Chat chat, String name){
        super(chat, name);
    }

    public void getMessage(String message){
        System.out.println("User " + getName() + " got message '" + message + "'");
    }
}

interface Chat{
    void sendMessage(String name, User user);
}

class TextChat implements Chat{
    private User admin;
    private List<User> users = new ArrayList<>();

    public void setAdmin(User admin) {
        if (admin != null && admin instanceof Admin) {
            this.admin = admin;
        } else {
            throw new RuntimeException("Not have permissions!");
        }
    }

    public void addUser(User u){
        if (admin == null){
            throw new RuntimeException("Chat doesn't have admin!");
        }
        if (u instanceof SimpleUser){
            users.add(u);
        }else{
            throw new RuntimeException("Admin can't enter to another chat!");
        }
    }

    @Override
    public void sendMessage(String message, User user) {
        if (user instanceof Admin){
            for (User u : users) {
                u.getMessage(user.getName() + ": " + message);
            }
        }
        if (user instanceof SimpleUser){
            for (User u : users){
                if (u != user && u.isEnable()){
                    u.getMessage(user.getName() + ": " + message);
                }
            }
            if (admin.isEnable()){
                admin.getMessage(user.getName() + ": " + message);
            }
        }
    }
}