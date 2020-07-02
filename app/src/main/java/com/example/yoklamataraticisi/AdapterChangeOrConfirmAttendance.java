package com.example.yoklamataraticisi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterChangeOrConfirmAttendance extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private ArrayList<Attendance> attendances;
    private int numberOfWeeks, firstWeek;
    private Bitmap bitmapAttendanceList;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewStudentNumber;
        public ImageView imageViewWeek1;
        public ImageView imageViewWeek2;
        public ImageView imageViewWeek3;
        public ImageView imageViewWeek4;
        public ImageView imageViewAttendanceList;
        public View layout;
        public ViewHolder(View v){
            super(v);
            layout=v;
            textViewStudentNumber = (TextView) v.findViewById(R.id.textViewStudentNumber);
            imageViewWeek1 = (ImageView) v.findViewById(R.id.imageViewWeek1);
            imageViewWeek2 = (ImageView) v.findViewById(R.id.imageViewWeek2);
            imageViewWeek3 = (ImageView) v.findViewById(R.id.imageViewWeek3);
            imageViewWeek4 = (ImageView) v.findViewById(R.id.imageViewWeek4);
            imageViewAttendanceList = (ImageView) v.findViewById(R.id.imageViewAttendanceList);
        }
    }
    public AdapterChangeOrConfirmAttendance(int numberOfWeeks, int firstWeek, ArrayList<Attendance> myDataset, Bitmap bitmapAttendanceList, Context context){
        this.numberOfWeeks = numberOfWeeks;
        this.firstWeek = firstWeek;
        attendances = myDataset;
        this.bitmapAttendanceList = bitmapAttendanceList;
        this.context = context;
    }
    public AdapterChangeOrConfirmAttendance.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.cardview_confirm_attendance,parent,false);
        ViewHolder vh= new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final ViewHolder vh = (ViewHolder) viewHolder;
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
        if(numberOfWeeks >= 2){
            if(attendances.get(i).getAttendance().get(1+firstWeek-1).equals("false")){
                vh.imageViewWeek2.setImageResource(R.drawable.minus);
            }
            else if(attendances.get(i).getAttendance().get(1+firstWeek-1).equals("fake")){
                vh.imageViewWeek2.setImageResource(R.drawable.question);
            }
            else{
                vh.imageViewWeek2.setImageResource(R.drawable.plus);
            }
        }
        else{
            vh.imageViewWeek2.setVisibility(View.GONE);
        }
        if(numberOfWeeks >= 3){
            if(attendances.get(i).getAttendance().get(2+firstWeek-1).equals("false")){
                vh.imageViewWeek3.setImageResource(R.drawable.minus);
            }
            else if(attendances.get(i).getAttendance().get(2+firstWeek-1).equals("fake")){
                vh.imageViewWeek3.setImageResource(R.drawable.question);
            }
            else{
                vh.imageViewWeek3.setImageResource(R.drawable.plus);
            }
        }
        else{
            vh.imageViewWeek3.setVisibility(View.GONE);
        }
        if(numberOfWeeks == 4){
            if(attendances.get(i).getAttendance().get(3+firstWeek-1).equals("false")){
                vh.imageViewWeek4.setImageResource(R.drawable.minus);
            }
            else if(attendances.get(i).getAttendance().get(3+firstWeek-1).equals("fake")){
                vh.imageViewWeek4.setImageResource(R.drawable.question);
            }
            else{
                vh.imageViewWeek4.setImageResource(R.drawable.plus);
            }
        }
        else{
            vh.imageViewWeek4.setVisibility(View.GONE);
        }
        if(i == getItemCount()-1){
            vh.imageViewAttendanceList.setImageBitmap(bitmapAttendanceList);
            vh.imageViewAttendanceList.setVisibility(View.VISIBLE);
        }
        else{
            vh.imageViewAttendanceList.setVisibility(View.GONE);
        }
        ((ViewHolder) viewHolder).imageViewWeek1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendances.get(i).getAttendance().get(0+firstWeek-1).equals("false")){
                    vh.imageViewWeek1.setImageResource(R.drawable.plus);
                    attendances.get(i).changeAbsent(0+firstWeek-1);
                }
                else if(attendances.get(i).getAttendance().get(0+firstWeek-1).equals("fake")){
                    vh.imageViewWeek1.setImageResource(R.drawable.minus);
                    attendances.get(i).absenteeism(0+firstWeek-1);
                }
                else{
                    vh.imageViewWeek1.setImageResource(R.drawable.question);
                    attendances.get(i).determine_fake_signatures(0+firstWeek-1);
                }
            }
        });
        ((ViewHolder) viewHolder).imageViewWeek2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendances.get(i).getAttendance().get(1+firstWeek-1).equals("false")){
                    vh.imageViewWeek2.setImageResource(R.drawable.plus);
                    attendances.get(i).changeAbsent(1+firstWeek-1);
                }
                else if(attendances.get(i).getAttendance().get(1+firstWeek-1).equals("fake")){
                    vh.imageViewWeek2.setImageResource(R.drawable.minus);
                    attendances.get(i).absenteeism(1+firstWeek-1);
                }
                else{
                    vh.imageViewWeek2.setImageResource(R.drawable.question);
                    attendances.get(i).determine_fake_signatures(1+firstWeek-1);
                }
            }
        });
        ((ViewHolder) viewHolder).imageViewWeek3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendances.get(i).getAttendance().get(2+firstWeek-1).equals("false")){
                    vh.imageViewWeek3.setImageResource(R.drawable.plus);
                    attendances.get(i).changeAbsent(2+firstWeek-1);
                }
                else if(attendances.get(i).getAttendance().get(2+firstWeek-1).equals("fake")){
                    vh.imageViewWeek3.setImageResource(R.drawable.minus);
                    attendances.get(i).absenteeism(2+firstWeek-1);
                }
                else{
                    vh.imageViewWeek3.setImageResource(R.drawable.question);
                    attendances.get(i).determine_fake_signatures(2+firstWeek-1);
                }
            }
        });
        ((ViewHolder) viewHolder).imageViewWeek4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendances.get(i).getAttendance().get(3+firstWeek-1).equals("false")){
                    vh.imageViewWeek4.setImageResource(R.drawable.plus);
                    attendances.get(i).changeAbsent(3+firstWeek-1);
                }
                else if(attendances.get(i).getAttendance().get(3+firstWeek-1).equals("fake")){
                    vh.imageViewWeek4.setImageResource(R.drawable.minus);
                    attendances.get(i).absenteeism(3+firstWeek-1);
                }
                else{
                    vh.imageViewWeek4.setImageResource(R.drawable.question);
                    attendances.get(i).determine_fake_signatures(3+firstWeek-1);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return attendances.size();
    }
}
