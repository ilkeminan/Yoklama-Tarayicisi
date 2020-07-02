package com.example.yoklamataraticisi;

public class Student implements java.io.Serializable {
    private String student_number, name, surname;

    public Student(String student_number, String name, String surname){
        this.student_number = student_number;
        this.name = name;
        this.surname = surname;
    }

    public String getStudent_number(){
        return student_number;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public void setStudent_number(String student_number){
        this.student_number = student_number;
    }
}
