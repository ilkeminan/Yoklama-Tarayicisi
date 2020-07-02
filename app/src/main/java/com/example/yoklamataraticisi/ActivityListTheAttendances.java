package com.example.yoklamataraticisi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ActivityListTheAttendances extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context=this;
    ArrayList<Attendance> attendances;
    Connection con;
    Course course;
    ImageButton homeButton,logoutButton,imageButtonAttendanceByWeeks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_the_attendances);
        Intent intentListTheAttendances = getIntent();
        course = (Course) intentListTheAttendances.getSerializableExtra("course");
        attendances = new ArrayList<Attendance>();
        try {
            DatabaseConnector connector = new DatabaseConnector();
            con = connector.Baglanti();
            if (con == null) {
                Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
            }
            else {
                String query_attendance = "select s.student_number,name,surname from Attendance a, Student s where a.student_number = s.student_number and a.course_code= '" + course.getCode() +"' and a.course_semester= '"+ course.getSemester() +"' order by s.student_number";
                Statement statement_attendance = con.createStatement();
                ResultSet rs_attendance = statement_attendance.executeQuery(query_attendance);
                while(rs_attendance.next()){
                    Student student = new Student(rs_attendance.getString("student_number"),rs_attendance.getString("name"),rs_attendance.getString("surname"));
                    Attendance attendance = new Attendance(student,course);
                    attendances.add(attendance);
                }
                con.close();
            }
        }
        catch(Exception e){

        }
        int number_of_students = attendances.size();
        int number_of_scanned_weeks = 0;
        for(int i = 0; i < 14; i++){
            boolean is_Scanned = false;
            int j = 0;
            while(j < number_of_students && is_Scanned == false){
                if(!attendances.get(j).getAttendance().get(i).equals("true")){
                    is_Scanned = true;
                }
                j++;
            }
            if(is_Scanned == true){
                number_of_scanned_weeks++;
            }
        }
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewListTheAttendances);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AdapterListTheAttendances adapterListTheAttendances = new AdapterListTheAttendances(attendances,number_of_scanned_weeks,context);
        recyclerView.setAdapter(adapterListTheAttendances);
        homeButton = this.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListTheAttendances.this,ActivityChooseCourse.class);
                startActivity(intent);
            }
        });
        logoutButton = this.findViewById(R.id.imageButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListTheAttendances.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
        imageButtonAttendanceByWeeks = (ImageButton) this.findViewById(R.id.imageButtonAttendanceByWeeks);
        imageButtonAttendanceByWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityListTheAttendances.this,ActivityListTheAttendancesByWeeks.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });
    }
}
