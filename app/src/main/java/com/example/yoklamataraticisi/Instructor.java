package com.example.yoklamataraticisi;

import java.util.ArrayList;

public class Instructor implements java.io.Serializable {
    private String name, surname, user_name, password, email;
    private ArrayList<Course> courses;

    public  Instructor(String name, String surname, String user_name,String password, String email){
        this.name = name;
        this.surname = surname;
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        courses = new ArrayList<Course>();
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public String getUsername(){
        return user_name;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public void addCourse(Course aCourse){
        courses.add(aCourse);
    }

    public Course chooseCourse(String courseId) {
        for(Course aCourse : courses) {
            if(aCourse.getCode().compareTo(courseId) == 0) {
                return aCourse;
            }
        }
        return null;
    }
}
