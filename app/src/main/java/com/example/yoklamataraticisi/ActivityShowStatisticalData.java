package com.example.yoklamataraticisi;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ActivityShowStatisticalData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnerStatistics, spinnerIntervalOfWeeks;
    BarChart barChart;
    LineChart lineChart;
    Connection con;
    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistical_data);
        Intent intentListTheAttendances = getIntent();
        course = (Course) intentListTheAttendances.getSerializableExtra("course");
        spinnerStatistics = (Spinner) this.findViewById(R.id.spinnerStatistics);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.statistics, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatistics.setAdapter(adapter);
        spinnerStatistics.setOnItemSelectedListener(this);

        spinnerIntervalOfWeeks = (Spinner) this.findViewById(R.id.spinnerIntervalOfWeeks);
        adapter = ArrayAdapter.createFromResource(this, R.array.week_intervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIntervalOfWeeks.setAdapter(adapter);
        spinnerIntervalOfWeeks.setOnItemSelectedListener(this);

        lineChart = (LineChart) findViewById(R.id.linechart);
        fillTheChart_Weeks(1,5);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerStatistics){
            String text = parent.getItemAtPosition(position).toString();
            barChart.clear();
            if(text.equals("Haftalara Göre Devam")){
                spinnerIntervalOfWeeks.setVisibility(View.VISIBLE);
                if(spinnerIntervalOfWeeks.getSelectedItem().toString().equals("Hafta: 1-5")){
                    fillTheChart_Weeks(1,5);
                }
                else if(spinnerIntervalOfWeeks.getSelectedItem().toString().equals("Hafta: 6-10")){
                    fillTheChart_Weeks(6,10);
                }
                else if(spinnerIntervalOfWeeks.getSelectedItem().toString().equals("Hafta: 11-14")){
                    fillTheChart_Weeks(11,14);
                }
            }
            else if(text.equals("Derslere Göre Devam")){
                spinnerIntervalOfWeeks.setVisibility(View.VISIBLE);
                if(spinnerIntervalOfWeeks.getSelectedItem().toString().equals("Hafta: 1-5")){
                    fillTheChart_Courses(1,5);
                }
                else if(spinnerIntervalOfWeeks.getSelectedItem().toString().equals("Hafta: 6-10")){
                    fillTheChart_Courses(6,10);
                }
                else if(spinnerIntervalOfWeeks.getSelectedItem().toString().equals("Hafta: 11-14")){
                    fillTheChart_Courses(11,14);
                }
            }
            else if(text.equals("Haftalık Değişim")){
                fillTheLineChart();
                spinnerIntervalOfWeeks.setVisibility(View.GONE);
            }
        }
        else{
            String text = parent.getItemAtPosition(position).toString();
            barChart.clear();
            if(spinnerStatistics.getSelectedItem().toString().equals("Haftalara Göre Devam")){
                if(text.equals("Hafta: 1-5")){
                    fillTheChart_Weeks(1,5);
                }
                else if(text.equals("Hafta: 6-10")){
                    fillTheChart_Weeks(6,10);
                }
                else if(text.equals("Hafta: 11-14")){
                    fillTheChart_Weeks(11,14);
                }
            }
            else if(spinnerStatistics.getSelectedItem().toString().equals("Derslere Göre Devam")){
                if(text.equals("Hafta: 1-5")){
                    fillTheChart_Courses(1,5);
                }
                else if(text.equals("Hafta: 6-10")){
                    fillTheChart_Courses(6,10);
                }
                else if(text.equals("Hafta: 11-14")){
                    fillTheChart_Courses(11,14);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void fillTheChart_Weeks(int firstWeek, int lastWeek){
        lineChart.setVisibility(View.GONE);
        barChart = (BarChart) findViewById(R.id.barchart);
        barChart.setVisibility(View.VISIBLE);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setFitBars(true);
        barChart.setDrawGridBackground(true);
        barChart.getDescription().setEnabled(false);

        int [] devam = new int[14];
        int [] devamsiz = new int[14];
        int [] sahte_imza = new int[14];
        try {
            DatabaseConnector connector = new DatabaseConnector();
            con = connector.Baglanti();
            if (con == null) {
                Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
            }
            else {
                int j = 0;
                for(int i = firstWeek-1; i < lastWeek; i++){
                    String query_true = "select count(*) as truecount from Attendance where course_code= '" + course.getCode() +"' and course_semester= '" + course.getSemester() + "' and week"+Integer.toString(i+1)+"='true'";
                    Statement statement_true = con.createStatement();
                    ResultSet rs_true = statement_true.executeQuery(query_true);
                    if(rs_true.next()){
                        devam[j] = rs_true.getInt("truecount");
                    }
                    String query_false = "select count(*) as falsecount from Attendance where course_code= '" + course.getCode() +"' and course_semester= '" + course.getSemester() + "' and week"+Integer.toString(i+1)+"='false'";
                    Statement statement_false = con.createStatement();
                    ResultSet rs_false = statement_false.executeQuery(query_false);
                    if(rs_false.next()){
                        devamsiz[j] = rs_false.getInt("falsecount");
                    }
                    String query_fake = "select count(*) as fakecount from Attendance where course_code= '" + course.getCode() +"' and course_semester= '" + course.getSemester() + "' and week"+Integer.toString(i+1)+"='fake'";
                    Statement statement_fake = con.createStatement();
                    ResultSet rs_fake = statement_fake.executeQuery(query_fake);
                    if(rs_fake.next()){
                        sahte_imza[j] = rs_fake.getInt("fakecount");
                    }
                    j++;
                }
                con.close();
            }
        }
        catch(Exception e){

        }

        int stop;
        if(lastWeek % 5 == 0){
            stop = 5;
        }
        else{
            stop = lastWeek % 5;
        }
        ArrayList<BarEntry> barEntriesDevam = new ArrayList<>();
        for(int i = 0; i < stop; i++){
            barEntriesDevam.add(new BarEntry(i+1,devam[i]));
        }
        BarDataSet barDataSetDevam = new BarDataSet(barEntriesDevam, "Devam");
        barDataSetDevam.setColors(Color.GREEN);
        ArrayList<BarEntry> barEntriesDevamsiz = new ArrayList<>();
        for(int i = 0; i < stop; i++){
            barEntriesDevamsiz.add(new BarEntry(i+1,devamsiz[i]));
        }
        BarDataSet barDataSetDevamsiz = new BarDataSet(barEntriesDevamsiz, "Devamsız");
        barDataSetDevamsiz.setColors(Color.RED);
        ArrayList<BarEntry> barEntriesSahteImza = new ArrayList<>();
        for(int i = 0; i < stop; i++){
            barEntriesSahteImza.add(new BarEntry(i+1,sahte_imza[i]));
        }
        BarDataSet barDataSetSahteImza = new BarDataSet(barEntriesSahteImza, "Sahte İmza");
        barDataSetSahteImza.setColors(Color.BLUE);
        BarData data = new BarData(barDataSetDevam, barDataSetDevamsiz, barDataSetSahteImza);

        float groupSpace = 0.1f;
        float barSpace = 0.02f;
        float barWidth = 0.28f;
        barChart.setData(data);
        data.setBarWidth(barWidth);
        barChart.groupBars(1, groupSpace, barSpace);

        String [] weeks = new String[6];
        weeks[0] = null;
        for(int i=firstWeek; i<=lastWeek; i++){
            if(firstWeek == 1){
                weeks[i] = "Hafta " + Integer.toString(i);
            }
            else if(firstWeek == 6){
                weeks[i-5] = "Hafta " + Integer.toString(i);
            }
            else if(firstWeek == 11){
                weeks[i-10] = "Hafta " + Integer.toString(i);
            }
        }
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(weeks));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(data.getXMax() + 1);
    }

    public void fillTheChart_Courses(int firstWeek, int lastWeek){
        lineChart.setVisibility(View.GONE);
        barChart = (BarChart) findViewById(R.id.barchart);
        barChart.setVisibility(View.VISIBLE);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setFitBars(true);
        barChart.setDrawGridBackground(true);
        barChart.getDescription().setText("Derslerin haftalara göre ortalama devam durumları");
        barChart.getDescription().setEnabled(true);

        int number_of_courses = 0;
        String [] course_names = new String[50];
        float [] devam = new float[50];
        float [] devamsiz = new float[50];
        float [] sahte_imza = new float[50];
        try {
            DatabaseConnector connector = new DatabaseConnector();
            con = connector.Baglanti();
            if (con == null) {
                Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
            }
            else {
                int j = 0;
                String query_course = "select * from Course where instructor_username= '" + ActivityLogin.logged_in_user.getUsername() +"'";
                Statement statement_course = con.createStatement();
                ResultSet rs_course = statement_course.executeQuery(query_course);
                while(rs_course.next()){
                    String course_code = rs_course.getString("code");
                    course_names[j] =  rs_course.getString("name");
                    String semester = rs_course.getString("semester");
                    float avg_true = 0, avg_false = 0, avg_fake = 0;
                    for(int i = firstWeek-1; i < lastWeek; i++){
                        String query_true = "select count(*) as truecount from Attendance where course_code= '" + course_code +"' and course_semester= '" + semester + "' and week"+Integer.toString(i+1)+"='true'";
                        Statement statement_true = con.createStatement();
                        ResultSet rs_true = statement_true.executeQuery(query_true);
                        if(rs_true.next()){
                            int truecount = rs_true.getInt("truecount");
                            avg_true = avg_true + truecount;
                        }
                        String query_false = "select count(*) as falsecount from Attendance where course_code= '" + course_code +"' and course_semester= '" + semester + "' and week"+Integer.toString(i+1)+"='false'";
                        Statement statement_false = con.createStatement();
                        ResultSet rs_false = statement_false.executeQuery(query_false);
                        if(rs_false.next()){
                            int falsecount = rs_false.getInt("falsecount");
                            avg_false = avg_false + falsecount;
                        }
                        String query_fake = "select count(*) as fakecount from Attendance where course_code= '" + course_code +"' and course_semester= '" + semester + "' and week"+Integer.toString(i+1)+"='fake'";
                        Statement statement_fake = con.createStatement();
                        ResultSet rs_fake = statement_fake.executeQuery(query_fake);
                        if(rs_fake.next()){
                            int fakecount = rs_fake.getInt("fakecount");
                            avg_fake = avg_fake + fakecount;
                        }
                    }
                    avg_true = avg_true / (float)(lastWeek - firstWeek + 1);
                    avg_false = avg_false / (float)(lastWeek - firstWeek + 1);
                    avg_fake = avg_fake / (float)(lastWeek - firstWeek + 1);
                    devam[j] = avg_true;
                    devamsiz[j] = avg_false;
                    sahte_imza[j] = avg_fake;
                    j++;
                }
                number_of_courses = j;
                con.close();
            }
        }
        catch(Exception e){

        }

        ArrayList<BarEntry> barEntriesDevam = new ArrayList<>();
        for(int i = 0; i < number_of_courses; i++){
            barEntriesDevam.add(new BarEntry(i+1,devam[i]));
        }
        BarDataSet barDataSetDevam = new BarDataSet(barEntriesDevam, "Devam");
        barDataSetDevam.setColors(Color.GREEN);
        ArrayList<BarEntry> barEntriesDevamsiz = new ArrayList<>();
        for(int i = 0; i < number_of_courses; i++){
            barEntriesDevamsiz.add(new BarEntry(i+1,devamsiz[i]));
        }
        BarDataSet barDataSetDevamsiz = new BarDataSet(barEntriesDevamsiz, "Devamsız");
        barDataSetDevamsiz.setColors(Color.RED);
        ArrayList<BarEntry> barEntriesSahteImza = new ArrayList<>();
        for(int i = 0; i < number_of_courses; i++){
            barEntriesSahteImza.add(new BarEntry(i+1,sahte_imza[i]));
        }
        BarDataSet barDataSetSahteImza = new BarDataSet(barEntriesSahteImza, "Sahte İmza");
        barDataSetSahteImza.setColors(Color.BLUE);
        BarData data = new BarData(barDataSetDevam, barDataSetDevamsiz, barDataSetSahteImza);

        float groupSpace = 0.1f;
        float barSpace = 0.02f;
        float barWidth = 0.28f;
        barChart.setData(data);
        data.setBarWidth(barWidth);
        barChart.groupBars(1, groupSpace, barSpace);

        String [] courses = new String[50];
        courses[0] = null;
        for(int i=1; i<=number_of_courses; i++){
            courses[i] = course_names[i-1];
        }
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(courses));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(data.getXMax() + 1);
    }

    public void fillTheLineChart(){
        barChart.setVisibility(View.GONE);
        lineChart.setVisibility(View.VISIBLE);
        lineChart.getDescription().setEnabled(false);

        int [] devam = new int[14];
        int [] devamsiz = new int[14];
        int [] sahte_imza = new int[14];
        try {
            DatabaseConnector connector = new DatabaseConnector();
            con = connector.Baglanti();
            if (con == null) {
                Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
            }
            else {
                int j = 0;
                for(int i = 0; i < 14; i++){
                    String query_true = "select count(*) as truecount from Attendance where course_code= '" + course.getCode() +"' and course_semester= '" + course.getSemester() + "' and week"+Integer.toString(i+1)+"='true'";
                    Statement statement_true = con.createStatement();
                    ResultSet rs_true = statement_true.executeQuery(query_true);
                    if(rs_true.next()){
                        devam[j] = rs_true.getInt("truecount");
                    }
                    String query_false = "select count(*) as falsecount from Attendance where course_code= '" + course.getCode() +"' and course_semester= '" + course.getSemester() + "' and week"+Integer.toString(i+1)+"='false'";
                    Statement statement_false = con.createStatement();
                    ResultSet rs_false = statement_false.executeQuery(query_false);
                    if(rs_false.next()){
                        devamsiz[j] = rs_false.getInt("falsecount");
                    }
                    String query_fake = "select count(*) as fakecount from Attendance where course_code= '" + course.getCode() +"' and course_semester= '" + course.getSemester() + "' and week"+Integer.toString(i+1)+"='fake'";
                    Statement statement_fake = con.createStatement();
                    ResultSet rs_fake = statement_fake.executeQuery(query_fake);
                    if(rs_fake.next()){
                        sahte_imza[j] = rs_fake.getInt("fakecount");
                    }
                    j++;
                }
                con.close();
            }
        }
        catch(Exception e){

        }
        int stop = 13;
        while (stop >= 0 && devamsiz[stop] == 0 && sahte_imza[stop] == 0){
            stop--;
        }
        ArrayList<Entry> dataValuesDevam = new ArrayList<Entry>();
        for(int i = 0; i <= stop; i++){
            dataValuesDevam.add(new Entry(i+1,devam[i]));
        }
        LineDataSet lineDataSetDevam = new LineDataSet(dataValuesDevam,"Devam");
        ArrayList<Entry> dataValuesDevamsiz = new ArrayList<Entry>();
        for(int i = 0; i <= stop; i++){
            dataValuesDevamsiz.add(new Entry(i+1,devamsiz[i]));
        }
        LineDataSet lineDataSetDevamsiz = new LineDataSet(dataValuesDevamsiz,"Devamsız");
        ArrayList<Entry> dataValuesSahteImza = new ArrayList<Entry>();
        for(int i = 0; i <= stop; i++){
            dataValuesSahteImza.add(new Entry(i+1,sahte_imza[i]));
        }
        LineDataSet lineDataSetSahteImza = new LineDataSet(dataValuesSahteImza,"Sahte İmza");
        lineDataSetDevam.setColor(Color.GREEN);
        lineDataSetDevamsiz.setColor(Color.RED);
        lineDataSetSahteImza.setColor(Color.BLUE);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSetDevam);
        dataSets.add(lineDataSetDevamsiz);
        dataSets.add(lineDataSetSahteImza);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }

}