package com.myclass.myclass;

/**
 * Created by anand on 16/01/18.
 */

public class User {
    private String email,id,name,year,department,batch,type,imageurl,subject;

    public User(String email,  String id, String name, String year, String department, String batch, String type,String imageurl,String subject) {
        this.email = email;
        this.id = id;
        this.name = name;
        this.year = year;
        this.department = department;
        this.batch = batch;
        this.type = type;
        this.imageurl = imageurl;
        this.subject = subject;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getDepartment() {
        return department;
    }

    public String getBatch() {
        return batch;
    }

    public String getType(){return type;}

    public String getImageurl(){return imageurl;}

    public String getSubject() {
        return subject;
    }
}
