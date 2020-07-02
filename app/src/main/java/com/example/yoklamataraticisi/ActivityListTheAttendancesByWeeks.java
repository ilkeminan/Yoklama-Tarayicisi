package com.example.yoklamataraticisi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ActivityListTheAttendancesByWeeks extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnerIntervalOfWeeks;
    RecyclerView recyclerView;
    Context context=this;
    Connection con;
    Course course;
    ArrayList<Attendance> attendances;
    ImageButton homeButton,logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_the_attendances_by_weeks);
        Intent intentListTheAttendances = getIntent();
        course = (Course) intentListTheAttendances.getSerializableExtra("course");
        spinnerIntervalOfWeeks = (Spinner) this.findViewById(R.id.spinnerIntervalOfWeeks_ListTheAttendances);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.week_intervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIntervalOfWeeks.setAdapter(adapter);
        spinnerIntervalOfWeeks.setOnItemSelectedListener(this);
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
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewAttendancesByWeeks);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AdapterListTheAttendancesByWeeks adapterListTheAttendancesByWeeks = new AdapterListTheAttendancesByWeeks(5,1,attendances,context);
        recyclerView.setAdapter(adapterListTheAttendancesByWeeks);
        homeButton = this.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListTheAttendancesByWeeks.this,ActivityChooseCourse.class);
                startActivity(intent);
            }
        });
        logoutButton = this.findViewById(R.id.imageButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListTheAttendancesByWeeks.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        AdapterListTheAttendancesByWeeks adapterListTheAttendancesByWeeks = null;
        if(text.equals("Hafta: 1-5")){
            adapterListTheAttendancesByWeeks = new AdapterListTheAttendancesByWeeks(5,1,attendances,context);
        }
        else if(text.equals("Hafta: 6-10")){
            adapterListTheAttendancesByWeeks = new AdapterListTheAttendancesByWeeks(5,6,attendances,context);
        }
        else if(text.equals("Hafta: 11-14")){
            adapterListTheAttendancesByWeeks = new AdapterListTheAttendancesByWeeks(4,11,attendances,context);
        }
        recyclerView.setAdapter(adapterListTheAttendancesByWeeks);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

