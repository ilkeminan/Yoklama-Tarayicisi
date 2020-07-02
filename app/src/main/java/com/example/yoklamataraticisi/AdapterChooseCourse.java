package com.example.yoklamataraticisi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterChooseCourse extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Course> courses;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewCourseID, textViewCourseName, textViewCourseSemester;
        public View layout;
        public ViewHolder(View v){
            super(v);
            layout=v;
            textViewCourseID = (TextView) v.findViewById(R.id.textViewCourseID);
            textViewCourseName = (TextView) v.findViewById(R.id.textViewCourseName);
            textViewCourseSemester = (TextView) v.findViewById(R.id.textViewCourseSemester);
        }
    }
    public AdapterChooseCourse(ArrayList<Course> myDataset, Context context){
        courses = myDataset;
        this.context = context;
    }
    public AdapterChooseCourse.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recyclerview_choose_course ,parent,false);
        AdapterChooseCourse.ViewHolder vh= new AdapterChooseCourse.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final AdapterChooseCourse.ViewHolder vh = (AdapterChooseCourse.ViewHolder) viewHolder;
        vh.textViewCourseID.setText(courses.get(i).getCode());
        vh.textViewCourseName.setText(courses.get(i).getName());
        vh.textViewCourseSemester.setText(courses.get(i).getSemester());
        ((ViewHolder) viewHolder).textViewCourseID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChooseCourseAction.class);
                for(Course aCourse : courses){
                    if(aCourse.getCode().equals(vh.textViewCourseID.getText().toString()) && aCourse.getSemester().equals(vh.textViewCourseSemester.getText().toString())){
                        intent.putExtra("course", aCourse);
                    }
                }
                context.startActivity(intent);
            }
        });
        ((ViewHolder) viewHolder).textViewCourseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChooseCourseAction.class);
                for(Course aCourse : courses){
                    if(aCourse.getCode().equals(vh.textViewCourseID.getText().toString()) && aCourse.getSemester().equals(vh.textViewCourseSemester.getText().toString())){
                        intent.putExtra("course", aCourse);
                    }
                }
                context.startActivity(intent);
            }
        });
        ((ViewHolder) viewHolder).textViewCourseSemester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChooseCourseAction.class);
                for(Course aCourse : courses){
                    if(aCourse.getCode().equals(vh.textViewCourseID.getText().toString()) && aCourse.getSemester().equals(vh.textViewCourseSemester.getText().toString())){
                        intent.putExtra("course", aCourse);
                    }
                }
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return courses.size();
    }
}
