package com.example.yoklamataraticisi;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ActivityChooseCourse extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context=this;
    ArrayList<Course> courses;
    Connection con;
    FloatingActionButton buttonCreateCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);
        courses = new ArrayList<Course>();
        try {
            DatabaseConnector connector = new DatabaseConnector();
            con = connector.Baglanti();
            if (con == null) {
                Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
            }
            else {
                String query_course = "select * from Course where instructor_username= '" + ActivityLogin.logged_in_user.getUsername() +"'";
                Statement statement_course = con.createStatement();
                ResultSet rs_course = statement_course.executeQuery(query_course);
                while(rs_course.next()){
                    String code = rs_course.getString("code");
                    String name = rs_course.getString("name");
                    String semester = rs_course.getString("semester");
                    Course course = new Course(code, name, semester, ActivityLogin.logged_in_user);
                    courses.add(course);
                }
                con.close();
            }
        }
        catch(Exception e){

        }
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewChooseCourse);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AdapterChooseCourse adapterChooseCourse = new AdapterChooseCourse(courses,context);
        recyclerView.setAdapter(adapterChooseCourse);
        buttonCreateCourse = (FloatingActionButton) this.findViewById(R.id.buttonCreateCourse);
        buttonCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityChooseCourse.this,ActivityCreateCourse.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityChooseCourse.this, ActivityLogin.class);
        startActivity(intent);
    }
}
