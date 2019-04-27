package com.csee5590.lab3;

public class User {
    String name;
    String email;
    String age;
    String phone;
    String university;
    String major;

    public User(String Email, String Name, String Age, String Phone, String Uni, String Major) {
        name = Name;
        email = Email;
        phone = Phone;
        age = Age;
        university = Uni;
        major = Major;
    }
    public String getEmail(){ return email;}

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAge() {
        return age;
    }

    public String getUniversity (){return university;}

    public String getMajor () {return major;}
}
