package com.example.yoklamataraticisi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterListTheAttendancesByWeeks extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private ArrayList<Attendance> attendances;
    private int numberOfWeeks, firstWeek;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewStudentNumber;
        public ImageView imageViewWeek1;
        public ImageView imageViewWeek2;
        public ImageView imageViewWeek3;
        public ImageView imageViewWeek4;
        public ImageView imageViewWeek5;
        public View layout;
        public ViewHolder(View v){
            super(v);
            layout=v;
            textViewStudentNumber = (TextView) v.findViewById(R.id.textViewStudentNumber_LTABW);
            imageViewWeek1 = (ImageView) v.findViewById(R.id.imageViewWeek1_LTABW);
            imageViewWeek2 = (ImageView) v.findViewById(R.id.imageViewWeek2_LTABW);
            imageViewWeek3 = (ImageView) v.findViewById(R.id.imageViewWeek3_LTABW);
            imageViewWeek4 = (ImageView) v.findViewById(R.id.imageViewWeek4_LTABW);
            imageViewWeek5 = (ImageView) v.findViewById(R.id.imageViewWeek5_LTABW);
        }
    }
    public AdapterListTheAttendancesByWeeks(int numberOfWeeks, int firstWeek, ArrayList<Attendance> myDataset, Context context){
        this.numberOfWeeks = numberOfWeeks;
        this.firstWeek = firstWeek;
        attendances = myDataset;
        this.context = context;
    }
    public AdapterListTheAttendancesByWeeks.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recyclerview_attendances_by_weeks,parent,false);
        AdapterListTheAttendancesByWeeks.ViewHolder vh= new AdapterListTheAttendancesByWeeks.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final AdapterListTheAttendancesByWeeks.ViewHolder vh = (AdapterListTheAttendancesByWeeks.ViewHolder) viewHolder;
        vh.textViewStudentNumber.setText(attendances.get(i).getStudent().getStudent_number());
        if(attendances.get(i).getAttendance().get(0+firstWeek-1).equals("false")){
            vh.imageViewWeek1.setImageResource(R.drawable.minus);
        }
        else if(attendances.get(i).getAttendance().get(0+firstWeek-1).equals("fake")){
            vh.imageViewWeek1.setImageResource(R.drawable.question);
        }
        else{
            vh.imageViewWeek1.setImageResource(R.drawable.plus);
        }
        if(attendances.get(i).getAttendance().get(1+firstWeek-1).equals("false")){
            vh.imageViewWeek2.setImageResource(R.drawable.minus);
        }
        else if(attendances.get(i).getAttendance().get(1+firstWeek-1).equals("fake")){
            vh.imageViewWeek2.setImageResource(R.drawable.question);
        }
        else{
            vh.imageViewWeek2.setImageResource(R.drawable.plus);
        }
        if(attendances.get(i).getAttendance().get(2+firstWeek-1).equals("false")){
            vh.imageViewWeek3.setImageResource(R.drawable.minus);
        }
        else if(attendances.get(i).getAttendance().get(2+firstWeek-1).equals("fake")){
            vh.imageViewWeek3.setImageResource(R.drawable.question);
        }
        else{
            vh.imageViewWeek3.setImageResource(R.drawable.plus);
        }
        if(attendances.get(i).getAttendance().get(3+firstWeek-1).equals("false")){
            vh.imageViewWeek4.setImageResource(R.drawable.minus);
        }
        else if(attendances.get(i).getAttendance().get(3+firstWeek-1).equals("fake")){
            vh.imageViewWeek4.setImageResource(R.drawable.question);
        }
        else{
            vh.imageViewWeek4.setImageResource(R.drawable.plus);
        }
        if(numberOfWeeks == 5){
            if(attendances.get(i).getAttendance().get(4+firstWeek-1).equals("false")){
                vh.imageViewWeek5.setImageResource(R.drawable.minus);
            }
            else if(attendances.get(i).getAttendance().get(4+firstWeek-1).equals("fake")){
                vh.imageViewWeek5.setImageResource(R.drawable.question);
            }
            else{
                vh.imageViewWeek5.setImageResource(R.drawable.plus);
            }
        }
        else{
            vh.imageViewWeek5.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return attendances.size();
    }
}

