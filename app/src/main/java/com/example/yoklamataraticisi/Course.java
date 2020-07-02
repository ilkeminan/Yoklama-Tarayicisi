package com.example.yoklamataraticisi;

public class Course implements java.io.Serializable {
    private String code, name, semester;
    Instructor instructor;

    public Course(String code, String name, String semester, Instructor instructor){
        this.code = code;
        this.name = name;
        this.semester = semester;
        this.instructor = instructor;
    }

    public String getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public String getSemester(){
        return semester;
    }

    public  Instructor getInstructor(){
        return instructor;
    }
}
