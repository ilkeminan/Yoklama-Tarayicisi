package com.example.yoklamataraticisi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterListTheAttendances extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Attendance> attendances;
    private int number_of_scanned_weeks;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewStudentNumber, textViewNumberOfAbsenteeism, textViewNumberOfFakeSignatures;
        public View layout;
        public ViewHolder(View v){
            super(v);
            layout=v;
            textViewStudentNumber = (TextView) v.findViewById(R.id.textViewStudentNumber_attendances);
            textViewNumberOfAbsenteeism = (TextView) v.findViewById(R.id.textViewNumberOfAbsenteeism);
            textViewNumberOfFakeSignatures = (TextView) v.findViewById(R.id.textViewNumberOfFakeSignatures);
        }
    }
    public AdapterListTheAttendances(ArrayList<Attendance> myDataset, int number_of_scanned_weeks, Context context){
        attendances = myDataset;
        this.number_of_scanned_weeks = number_of_scanned_weeks;
        this.context = context;
    }
    public AdapterListTheAttendances.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recyclerview_list_the_attendances ,parent,false);
        AdapterListTheAttendances.ViewHolder vh= new AdapterListTheAttendances.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final AdapterListTheAttendances.ViewHolder vh = (AdapterListTheAttendances.ViewHolder) viewHolder;
        vh.textViewStudentNumber.setText(attendances.get(i).getStudent().getStudent_number());
        vh.textViewNumberOfAbsenteeism.setText(Integer.toString(attendances.get(i).getNumber_of_absenteeism())+"/"+Integer.toString(number_of_scanned_weeks));
        vh.textViewNumberOfFakeSignatures.setText(Integer.toString(attendances.get(i).getNumber_of_fake_signatures())+"/"+Integer.toString(number_of_scanned_weeks));
    }
    @Override
    public int getItemCount() {
        return attendances.size();
    }
}
