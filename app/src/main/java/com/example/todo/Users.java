package com.example.todo;

public class Users {

    private String email;
    private String password;
    private String userID;
    private todo TodO;

    public Users(){

    }

    public Users(String email_in,String pass_in,String userid){
        this.email=email_in;
        this.password=pass_in;
        this.userID=userid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
