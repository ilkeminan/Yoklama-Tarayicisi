package com.example.yoklamataraticisi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ActivityCreateCourse extends AppCompatActivity {
    EditText editTextCourseID, editTextCourseName, editTextCourseSemester;
    ImageButton homeButton,logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        editTextCourseID = (EditText) this.findViewById(R.id.editTextCourseID_create);
        editTextCourseName = (EditText) this.findViewById(R.id.editTextCourseName_create);
        editTextCourseSemester = (EditText) this.findViewById(R.id.editTextCourseSemester_create);
        Button buttonCreateCourse = (Button) this.findViewById(R.id.buttonCreateCourse_create);
        buttonCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextCourseID.getText().toString().equals("") || editTextCourseName.getText().toString().equals("") || editTextCourseSemester.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Lütfen tüm alanları giriniz.", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        DatabaseConnector connector = new DatabaseConnector();
                        Connection con = connector.Baglanti();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String query = "select * from Course where code= '" + editTextCourseID.getText().toString() + "' and semester= '" + editTextCourseSemester.getText().toString() + "'";
                            Statement durum = con.createStatement();
                            ResultSet rs = durum.executeQuery(query);
                            if(rs.next()){
                                Toast.makeText(getApplicationContext(), "Başka bir ders kodu veya dönem seçiniz.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String insert_course = "insert into Course values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                PreparedStatement preparedStatement_Course = con.prepareStatement(insert_course);
                                preparedStatement_Course.setString(1,editTextCourseID.getText().toString());
                                preparedStatement_Course.setString(2,editTextCourseName.getText().toString());
                                preparedStatement_Course.setString(3,editTextCourseSemester.getText().toString());
                                preparedStatement_Course.setString(4,ActivityLogin.logged_in_user.getUsername());
                                preparedStatement_Course.setString(5,null);
                                preparedStatement_Course.setString(6,null);
                                preparedStatement_Course.setString(7,null);
                                preparedStatement_Course.setString(8,null);
                                preparedStatement_Course.setString(9,null);
                                preparedStatement_Course.setString(10,null);
                                preparedStatement_Course.setString(11,null);
                                preparedStatement_Course.setString(12,null);
                                preparedStatement_Course.setString(13,null);
                                preparedStatement_Course.setString(14,null);
                                preparedStatement_Course.setString(15,null);
                                preparedStatement_Course.setString(16,null);
                                preparedStatement_Course.setString(17,null);
                                preparedStatement_Course.setString(18,null);
                                preparedStatement_Course.executeUpdate();
                                Toast.makeText(getApplicationContext(),"Ders oluşturuldu.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityCreateCourse.this,ActivityChooseCourse.class);
                                startActivity(intent);
                            }
                        }
                        con.close();
                    }
                    catch(Exception e){

                    }
                }
            }
        });
        homeButton = this.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCreateCourse.this,ActivityChooseCourse.class);
                startActivity(intent);
            }
        });
        logoutButton = this.findViewById(R.id.imageButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCreateCourse.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
    }
}
