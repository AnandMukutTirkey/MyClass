package com.myclass.myclass;

/**
 * Created by anand on 16/01/18.
 */

public class TeacherInfo {
    String email,name,id,department,subject,type,imageurl;

    public TeacherInfo() {
    }

    public TeacherInfo(String email, String name, String id, String department, String subject, String type,String imageurl) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.department = department;
        this.subject = subject;
        this.type = type;
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {return type;}

    public String getImageurl(){return imageurl;}

    public String getEmail() {
        return email;
    }
}
