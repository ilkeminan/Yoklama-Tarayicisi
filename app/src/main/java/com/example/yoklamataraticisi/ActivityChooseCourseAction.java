package com.example.yoklamataraticisi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ActivityChooseCourseAction extends AppCompatActivity {
    Course course;
    Button buttonScanAttendanceList, buttonListTheAttendances, buttonShowStatisticalData, buttonSetTheDates;
    ImageButton homeButton,logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course_action);
        Intent intentChooseCourseAction = getIntent();
        course = (Course) intentChooseCourseAction.getSerializableExtra("course");
        buttonScanAttendanceList = (Button) this.findViewById(R.id.buttonScanAttendanceList);
        buttonScanAttendanceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScanAttendanceList = new Intent(ActivityChooseCourseAction.this, MainActivity.class);
                intentScanAttendanceList.putExtra("course", course);
                startActivity(intentScanAttendanceList);
            }
        });
        buttonListTheAttendances = (Button) this.findViewById(R.id.buttonListTheAttendances);
        buttonListTheAttendances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentListTheAttendances = new Intent(ActivityChooseCourseAction.this, ActivityListTheAttendances.class);
                intentListTheAttendances.putExtra("course", course);
                startActivity(intentListTheAttendances);
            }
        });
        buttonShowStatisticalData = (Button) this.findViewById(R.id.buttonShowTheStatistics);
        buttonShowStatisticalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShowStatisticalData = new Intent(ActivityChooseCourseAction.this, ActivityShowStatisticalData.class);
                intentShowStatisticalData.putExtra("course", course);
                startActivity(intentShowStatisticalData);
            }
        });
        buttonSetTheDates = (Button) this.findViewById(R.id.buttonSetTheDates);
        buttonSetTheDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSetTheDates = new Intent(ActivityChooseCourseAction.this, ActivitySetTheDates.class);
                intentSetTheDates.putExtra("course", course);
                startActivity(intentSetTheDates);
            }
        });
        homeButton = this.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityChooseCourseAction.this,ActivityChooseCourse.class);
                startActivity(intent);
            }
        });
        logoutButton = this.findViewById(R.id.imageButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityChooseCourseAction.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
    }
}
