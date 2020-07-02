package com.example.yoklamataraticisi;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivitySetTheDates extends AppCompatActivity {
    Connection con;
    Course course;
    EditText editTextWeek, editTextDate, editTextClock;
    CheckBox checkBox, checkBoxReminder;
    Button buttonEditDates, buttonShowTheDates;
    String [] dates;
    ImageButton homeButton,logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_the_dates);
        Intent intentListTheAttendances = getIntent();
        course = (Course) intentListTheAttendances.getSerializableExtra("course");
        editTextWeek = (EditText) this.findViewById(R.id.editTextWeek);
        editTextDate = (EditText) this.findViewById(R.id.editTextDate);
        editTextDate.setKeyListener(null);
        editTextClock = (EditText) this.findViewById(R.id.editTextClock);
        editTextClock.setKeyListener(null);
        checkBox = (CheckBox) this.findViewById(R.id.checkBoxDate);
        checkBox.setChecked(true);
        checkBoxReminder = (CheckBox) this.findViewById(R.id.checkBoxReminder);
        final Context context = this;
        ImageButton imageButtonCalendar = (ImageButton) findViewById(R.id.imageButtonCalendar);
        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar takvim = Calendar.getInstance();
                final int yil = takvim.get(Calendar.YEAR);
                final int ay = takvim.get(Calendar.MONTH);
                final int gun = takvim.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month+1;
                                editTextDate.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, yil, ay, gun);
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Tamam", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });
        ImageButton imageButtonClock = (ImageButton) this.findViewById(R.id.imageButtonClock);
        imageButtonClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar takvim = Calendar.getInstance();
                final int saat = takvim.get(Calendar.HOUR_OF_DAY);
                final int dakika = takvim.get(Calendar.MINUTE);
                TimePickerDialog tpd = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                editTextClock.setText(hourOfDay+":"+minute);
                            }
                        }, saat,dakika,true);
                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE,"Tamam",tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE,"İptal",tpd);
                tpd.show();
            }
        });
        buttonEditDates = (Button) this.findViewById(R.id.buttonEditDates);
        buttonEditDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextWeek.getText().toString().equals("") && !editTextDate.getText().toString().equals("") && !editTextClock.getText().toString().equals("")){
                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date selectedDate = new Date();
                    try {
                        selectedDate = dateFormat.parse(editTextDate.getText().toString() + " "+ editTextClock.getText().toString());
                    }
                    catch (ParseException e) {

                    }
                    int selectedWeek = Integer.parseInt(editTextWeek.getText().toString());
                    try {
                        DatabaseConnector connector = new DatabaseConnector();
                        con = connector.Baglanti();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(checkBox.isChecked() == true){
                                int j = 0;
                                dates = new String[14];
                                long lengthOfADay = 1000*60*60*24;
                                long lengthOfFiveHours = 1000*60*60*5;
                                for(int i=0;i<14;i++){
                                    Date date = new Date(selectedDate.getTime()+((i-(selectedWeek-1))* 7 * lengthOfADay));
                                    String strDate = dateFormat.format(date);
                                    dates[i] = strDate;
                                    if(checkBoxReminder.isChecked() == true){
                                        if(strDate != null && (i%4 == 3 || i == 13)){
                                            Calendar calSet = Calendar.getInstance();
                                            Date fiveHoursLater = new Date(date.getTime()+lengthOfFiveHours);
                                            calSet.setTime(fiveHoursLater);
                                            if (calSet.after(Calendar.getInstance())) {
                                                setAlarm(calSet,j);
                                            }
                                            j++;
                                        }
                                    }
                                }
                                String query_course = "update Course set date1=?, date2=?, date3=?, date4=?, date5=?, date6=?, date7=?, date8=?, date9=?, date10=?, date11=?, date12=?, date13=?, date14=? where code=? and semester=?";
                                PreparedStatement preparedStatement_Course = con.prepareStatement(query_course);
                                preparedStatement_Course.setString(1,dates[0]);
                                preparedStatement_Course.setString(2,dates[1]);
                                preparedStatement_Course.setString(3,dates[2]);
                                preparedStatement_Course.setString(4,dates[3]);
                                preparedStatement_Course.setString(5,dates[4]);
                                preparedStatement_Course.setString(6,dates[5]);
                                preparedStatement_Course.setString(7,dates[6]);
                                preparedStatement_Course.setString(8,dates[7]);
                                preparedStatement_Course.setString(9,dates[8]);
                                preparedStatement_Course.setString(10,dates[9]);
                                preparedStatement_Course.setString(11,dates[10]);
                                preparedStatement_Course.setString(12,dates[11]);
                                preparedStatement_Course.setString(13,dates[12]);
                                preparedStatement_Course.setString(14,dates[13]);
                                preparedStatement_Course.setString(15,course.getCode());
                                preparedStatement_Course.setString(16,course.getSemester());
                                preparedStatement_Course.executeUpdate();
                                con.close();
                                Toast.makeText(getApplicationContext(),"Tarihler düzenlendi.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivitySetTheDates.this,ActivityChooseCourse.class);
                                startActivity(intent);
                            }
                            else{
                                Date date = new Date(selectedDate.getTime());
                                String strDate = dateFormat.format(date);
                                if(checkBoxReminder.isChecked() == true){
                                    if(strDate != null){
                                        long lengthOfFiveHours = 1000*60*60*5;
                                        Calendar calSet = Calendar.getInstance();
                                        Date fiveHoursLater = new Date(date.getTime()+lengthOfFiveHours);
                                        calSet.setTime(fiveHoursLater);
                                        if (calSet.after(Calendar.getInstance())) {
                                            setAlarm(calSet,0);
                                        }
                                    }
                                }
                                String query_course = "update Course set date"+Integer.toString(selectedWeek)+"=? where code=? and semester=?";
                                PreparedStatement preparedStatement_Course = con.prepareStatement(query_course);
                                preparedStatement_Course.setString(1,dateFormat.format(selectedDate));
                                preparedStatement_Course.setString(2,course.getCode());
                                preparedStatement_Course.setString(3,course.getSemester());
                                preparedStatement_Course.executeUpdate();
                                con.close();
                                Toast.makeText(getApplicationContext(),"Tarih düzenlendi.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivitySetTheDates.this,ActivityChooseCourse.class);
                                startActivity(intent);
                            }
                        }
                    }
                    catch (SQLException e) {

                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Lütfen tüm alanları doldurunuz.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonShowTheDates = (Button) this.findViewById(R.id.buttonShowTheDates);
        buttonShowTheDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseConnector connector = new DatabaseConnector();
                    con = connector.Baglanti();
                    if (con == null) {
                        Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dates = new String[14];
                        String query_course = "select * from Course where code= '" + course.getCode() +"' and semester= '" + course.getSemester() + "'";
                        Statement statement_course = con.createStatement();
                        ResultSet rs_course = statement_course.executeQuery(query_course);
                        if(rs_course.next()){
                            for(int i=0;i<14;i++){
                                dates[i] = rs_course.getString("date"+Integer.toString(i+1));
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetTheDates.this);
                            builder.setTitle("Tarihler");
                            builder.setMessage("Hafta 1: "+dates[0]+"\n"+"Hafta 2: "+dates[1]+"\n"+"Hafta 3: "+dates[2]+"\n"+"Hafta 4: "+dates[3]+"\n"+"Hafta 5: "+dates[4]+"\n"+"Hafta 6: "+dates[5]+"\n"+"Hafta 7: "+dates[6]+"\n"+"Hafta 8: "+dates[7]+"\n"+"Hafta 9: "+dates[8]+"\n"+"Hafta 10: "+dates[9]+"\n"+"Hafta 11: "+dates[10]+"\n"+"Hafta 12: "+dates[11]+"\n"+"Hafta 13: "+dates[12]+"\n"+"Hafta 14: "+dates[13]);
                            builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                            builder.show();
                        }
                        con.close();
                    }
                }
                catch(Exception e){

                }
            }
        });
        homeButton = this.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySetTheDates.this,ActivityChooseCourse.class);
                startActivity(intent);
            }
        });
        logoutButton = this.findViewById(R.id.imageButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySetTheDates.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
    }

    public void setAlarm(Calendar alarmCalendar,int i){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastReceiverReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
        }
        else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
        }
    }
}
