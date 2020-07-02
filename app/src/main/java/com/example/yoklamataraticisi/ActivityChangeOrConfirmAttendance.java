package com.example.yoklamataraticisi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ActivityChangeOrConfirmAttendance extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context=this;
    ImageButton imageButtonConfirm,imageButtonEmail,homeButton,logoutButton,listButton;
    ArrayList<Student> students;
    ArrayList<Attendance> attendances;
    Course course;
    int numberOfWeeks, firstWeek;
    String [] student_numbers;
    boolean record_exists;
    Connection con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_or_confirm_attendance);
        Intent intentCoCA = getIntent();
        numberOfWeeks = intentCoCA.getIntExtra("numberofweeks",1);
        firstWeek = intentCoCA.getIntExtra("firstweek",1);
        course = (Course) intentCoCA.getSerializableExtra("course");
        attendances = (ArrayList<Attendance>) intentCoCA.getSerializableExtra("attendance");
        student_numbers = new String[100];
        record_exists = false;
        boolean is_all_8digits = true;
        int j = 0;
        try {
            DatabaseConnector connector = new DatabaseConnector();
            con = connector.Baglanti();
            if (con == null) {
                Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
            }
            else {
                String query = "select * from Attendance where course_code= '" + course.getCode() +" ' and course_semester= '"+ course.getSemester() + "' order by student_number";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while(rs.next()) {
                    if(rs.getString("student_number").replace(" ","").length() != 8){
                        is_all_8digits = false;
                    }
                    student_numbers[j] = rs.getString("student_number").replace(" ","");
                    j++;
                    record_exists = true;
                }
                con.close();
            }
        }
        catch(Exception e){

        }
        for(int i = 0; i < attendances.size(); i++){    //Student numbers may be recognized wrongly.
            if(attendances.get(i).getStudent().getStudent_number().contains("L")){
                attendances.get(i).getStudent().setStudent_number(attendances.get(i).getStudent().getStudent_number().replace("L","1"));
            }
            if(attendances.get(i).getStudent().getStudent_number().contains("O")){
                attendances.get(i).getStudent().setStudent_number(attendances.get(i).getStudent().getStudent_number().replace("O","0"));
            }
            if(attendances.get(i).getStudent().getStudent_number().length() > 8){
                if(j > 0 && is_all_8digits == true){
                    attendances.get(i).getStudent().setStudent_number(student_numbers[i]);
                }
                else{
                    String student_number = attendances.get(i).getStudent().getStudent_number();
                    int start_index = student_number.indexOf("1");
                    String new_number = student_number.substring(start_index, start_index+7);
                    attendances.get(i).getStudent().setStudent_number(new_number);
                }
            }
            else if(attendances.get(i).getStudent().getStudent_number().length() < 8){
                if(j > 0 && is_all_8digits == true){
                    attendances.get(i).getStudent().setStudent_number(student_numbers[i]);
                }
            }
            attendances.get(i).determine_numbers();
        }
        Bitmap bitmapAttendanceList = null;
        if(getIntent().hasExtra("attendanceList")){
            bitmapAttendanceList = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("attendanceList"),0,getIntent().getByteArrayExtra("attendanceList").length);
        }
        recyclerView=(RecyclerView) this.findViewById(R.id.recyclerViewCoCA);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AdapterChangeOrConfirmAttendance adapterCoCA = new AdapterChangeOrConfirmAttendance(numberOfWeeks,firstWeek,attendances,bitmapAttendanceList,context);
        recyclerView.setAdapter(adapterCoCA);
        students = new ArrayList<Student>();
        imageButtonConfirm = (ImageButton) this.findViewById(R.id.imageButtonConfirm2);
        imageButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
                Intent intent=new Intent(ActivityChangeOrConfirmAttendance.this,ActivityChooseCourse.class);
                startActivity(intent);
            }
        });
        imageButtonEmail = this.findViewById(R.id.imageButtonEmail);
        imageButtonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(attendances);
            }
        });
        homeButton = this.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityChangeOrConfirmAttendance.this,ActivityChooseCourse.class);
                startActivity(intent);
            }
        });
        logoutButton = this.findViewById(R.id.imageButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityChangeOrConfirmAttendance.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
        listButton = (ImageButton) this.findViewById(R.id.imageButtonList);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
                Intent intentListTheAttendances = new Intent(ActivityChangeOrConfirmAttendance.this, ActivityListTheAttendances.class);
                intentListTheAttendances.putExtra("course", course);
                startActivity(intentListTheAttendances);
            }
        });
    }

    public void saveToDatabase(){
        students.removeAll(students);
        for (Attendance attendance : attendances){
            Student student = attendance.getStudent();
            students.add(student);
        }
        try {
            DatabaseConnector connector = new DatabaseConnector();
            con = connector.Baglanti();
            if (con == null) {
                Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
            }
            else {
                int j = 0;
                for(Student aStudent : students){
                    String query_student;
                    if(record_exists == true && aStudent.getStudent_number().equals(student_numbers[j])){
                        //Existing record
                        query_student = "update Student set name=?, surname=? where student_number=?";
                        PreparedStatement preparedStatement_forStudent = con.prepareStatement(query_student);
                        preparedStatement_forStudent.setString(1,aStudent.getName());
                        preparedStatement_forStudent.setString(2,aStudent.getSurname());
                        preparedStatement_forStudent.setString(3,aStudent.getStudent_number());
                        preparedStatement_forStudent.executeUpdate();
                    }
                    else{
                        String sorgu = "select * from Student where student_number= '" + aStudent.getStudent_number() + "'";
                        Statement durum = con.createStatement();
                        ResultSet rs = durum.executeQuery(sorgu);
                        if(rs.next()){
                            //Existing record
                            query_student = "update Student set name=?, surname=? where student_number=?";
                            PreparedStatement preparedStatement_forStudent = con.prepareStatement(query_student);
                            preparedStatement_forStudent.setString(1,aStudent.getName());
                            preparedStatement_forStudent.setString(2,aStudent.getSurname());
                            preparedStatement_forStudent.setString(3,aStudent.getStudent_number());
                            preparedStatement_forStudent.executeUpdate();
                        }
                        else{
                            //New record
                            query_student = "insert into Student values(?,?,?)";
                            PreparedStatement preparedStatement_forStudent = con.prepareStatement(query_student);
                            preparedStatement_forStudent.setString(1,aStudent.getStudent_number());
                            preparedStatement_forStudent.setString(2,aStudent.getName());
                            preparedStatement_forStudent.setString(3,aStudent.getSurname());
                            preparedStatement_forStudent.executeUpdate();
                        }
                    }
                    j++;
                }
                j = 0;
                for (Attendance attendance : attendances){
                    Student student = attendance.getStudent();
                    students.add(student);
                    String[] array_attendance = attendance.getAttendance().toArray(new String[0]);
                    String query_attendance;
                    if(record_exists == true && attendance.getStudent().getStudent_number().equals(student_numbers[j])){
                        //Existing record
                        query_attendance = "update Attendance set number_of_absenteeism=?, number_of_fake_signatures=?" ;
                        for(int i=0;i<numberOfWeeks;i++){
                            query_attendance = query_attendance + ", week" + Integer.toString(firstWeek+i) + "=?";
                        }
                        query_attendance = query_attendance + " where student_number= '" + attendance.getStudent().getStudent_number() + "' and course_code= '" + course.getCode() +"' and course_semester= '" + course.getSemester() +"'" ;
                        PreparedStatement preparedStatement_forAttendance = con.prepareStatement(query_attendance);
                        preparedStatement_forAttendance.setInt(1,attendance.getNumber_of_absenteeism());
                        preparedStatement_forAttendance.setInt(2,attendance.getNumber_of_fake_signatures());
                        for(int i=0;i<numberOfWeeks;i++){
                            preparedStatement_forAttendance.setString(3+i,array_attendance[firstWeek+i-1].toString());
                        }
                        preparedStatement_forAttendance.execute();
                    }
                    else{
                        //New record
                        query_attendance = "insert into Attendance values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement preparedStatement_forAttendance = con.prepareStatement(query_attendance);
                        preparedStatement_forAttendance.setString(1,student.getStudent_number());
                        preparedStatement_forAttendance.setString(2,attendance.getCourse().getCode());
                        preparedStatement_forAttendance.setString(3,attendance.getCourse().getSemester());
                        preparedStatement_forAttendance.setInt(4,attendance.getNumber_of_absenteeism());
                        preparedStatement_forAttendance.setInt(5,attendance.getNumber_of_fake_signatures());
                        preparedStatement_forAttendance.setString(6,array_attendance[0].toString());
                        preparedStatement_forAttendance.setString(7,array_attendance[1].toString());
                        preparedStatement_forAttendance.setString(8,array_attendance[2].toString());
                        preparedStatement_forAttendance.setString(9,array_attendance[3].toString());
                        preparedStatement_forAttendance.setString(10,array_attendance[4].toString());
                        preparedStatement_forAttendance.setString(11,array_attendance[5].toString());
                        preparedStatement_forAttendance.setString(12,array_attendance[6].toString());
                        preparedStatement_forAttendance.setString(13,array_attendance[7].toString());
                        preparedStatement_forAttendance.setString(14,array_attendance[8].toString());
                        preparedStatement_forAttendance.setString(15,array_attendance[9].toString());
                        preparedStatement_forAttendance.setString(16,array_attendance[10].toString());
                        preparedStatement_forAttendance.setString(17,array_attendance[11].toString());
                        preparedStatement_forAttendance.setString(18,array_attendance[12].toString());
                        preparedStatement_forAttendance.setString(19,array_attendance[13].toString());
                        preparedStatement_forAttendance.execute();
                    }
                    j++;
                }
                con.close();
            }
        }
        catch(Exception e){

        }
    }

    public void sendMail(List<Attendance> attendances, String password){
        String Email = null;
        String Subject = null;
        String Message = null;
        for (Attendance attendance : attendances){
            Subject = course.getName()+" Dersindeki Devamsızlık Bilginiz";
            Email = emailBul(attendance.getStudent().getStudent_number());
            Message = "Merhaba " +attendance.getStudent().getStudent_number()+ " nolu öğrencimiz, " +course.getName()+ " dersindeki devamsızlık sayınız " +attendance.getNumber_of_absenteeism()+ " olmuştur.";
            JavaMailAPI javaMailAPI = new JavaMailAPI(this,Email,Subject,Message,course.getInstructor().getEmail(),password);
            javaMailAPI.execute();
        }
    }

    public void showDialog(final List<Attendance> attendances) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("message").setTitle("Mail Onayı");

        //Setting message manually and performing action on button click
        builder.setMessage("Devamsızlık bilgilerini mail göndermek istiyor musunuz?")
                .setCancelable(false)
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getPassword(attendances);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Mail Onayı");
        alert.show();
    }

    public String emailBul(String number){
        StringBuilder email = new StringBuilder("lxxxxxxx@std.yildiz.edu.tr");

        email.setCharAt(1,number.charAt(3));
        email.setCharAt(2,number.charAt(4));
        email.setCharAt(3,number.charAt(0));
        email.setCharAt(4,number.charAt(1));
        email.setCharAt(5,number.charAt(5));
        email.setCharAt(6,number.charAt(6));
        email.setCharAt(7,number.charAt(7));

        return email.toString();
    }

    public void getPassword(final List<Attendance> attendances){
        final String[] result = new String[1];
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Lütfen Mailinizin Şifresini Giriniz");
        final EditText input = new EditText(this);
        b.setView(input);
        b.setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                result[0] = input.getText().toString();
                sendMail(attendances,result[0]);
            }
        });
        b.setNegativeButton("İptal Et", null);
        b.show();
    }
}
