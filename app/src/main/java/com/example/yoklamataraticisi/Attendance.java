package com.example.yoklamataraticisi;

import android.graphics.Bitmap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Attendance implements java.io.Serializable {
    private Student student;
    private Course course;
    private ArrayList<String> attendance;
    private int number_of_absenteeism, number_of_fake_signatures;
    private Bitmap signature;

    public Attendance(Student student, Course course, int firstWeek, int numberOfWeeks){
        this.student = student;
        this.course = course;
        number_of_absenteeism = 0;
        number_of_fake_signatures = 0;
        signature = null;
        initializeAttendance(firstWeek, numberOfWeeks);
    }

    public Attendance(Student student, Course course){
        this.student = student;
        this.course = course;
        number_of_absenteeism = 0;
        number_of_fake_signatures = 0;
        signature = null;
        initializeAttendance(0, 0);
    }

    public Student getStudent(){
        return student;
    }

    public Course getCourse(){
        return course;
    }

    public int getNumber_of_absenteeism(){
        return number_of_absenteeism;
    }

    public int getNumber_of_fake_signatures(){
        return number_of_fake_signatures;
    }

    public Bitmap getSignature(){
        return signature;
    }

    public ArrayList<String> getAttendance(){
        return attendance;
    }

    public void initializeAttendance(int firstWeek, int numberOfWeeks){
        DatabaseConnector connector = new DatabaseConnector();
        Connection con = connector.Baglanti();
        try{
            if(con==null){
                System.out.println("Bağlantınızı Kontrol Edin");
            }
            else{
                String sorgu = "select * from Attendance where student_number= '" + getStudent().getStudent_number() + "' and course_code= '" + course.getCode() +"' and course_semester= '" + course.getSemester() + "'" ;
                Statement durum = con.createStatement();
                ResultSet rs = durum.executeQuery(sorgu);
                if(rs.next()){
                    //Record exists
                    attendance = new ArrayList<String>();
                    for(int i = 0; i < 14; i++){
                        if(i<firstWeek-1 || i>=firstWeek-1+numberOfWeeks){
                            String string_attendance = rs.getString("week"+Integer.toString(i+1));
                            string_attendance = string_attendance.replace(" ","");
                            string_attendance = string_attendance.replace("\n","");
                            attendance.add(string_attendance);
                        }
                        else{
                            attendance.add("true");
                        }
                    }
                    determine_numbers();
                }
                else{
                    //No record
                    attendance = new ArrayList<String>();
                    for(int i = 0; i < 14; i++){
                        attendance.add("true");
                    }
                }
            }
        }
        catch (Exception e){

        }
    }

    public int absenteeism(int week){
        attendance.set(week, "false");
        determine_numbers();
        return number_of_absenteeism;
    }

    public void changeAbsent(int week){
        attendance.set(week, "true");
        determine_numbers();
    }

    public void determine_fake_signatures(int week) {
        attendance.set(week, "fake");
        determine_numbers();
    }

    public void determine_numbers(){
        number_of_absenteeism = 0;
        number_of_fake_signatures = 0;
        for(int i = 0; i < 14; i++){
            if(attendance.get(i).equals("false")){
                number_of_absenteeism++;
            }
            else if(attendance.get(i).equals("fake")){
                number_of_fake_signatures++;
            }
        }
    }

    public void setSignature(Bitmap signature){
        this.signature = signature;
    }
}
