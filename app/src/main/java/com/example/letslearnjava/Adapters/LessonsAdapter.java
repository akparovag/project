package com.example.letslearnjava.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letslearnjava.Models.LessonModel;
import com.example.letslearnjava.R;

import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {
    private List<LessonModel> lessonsList;
    public LessonsAdapter(List<LessonModel> lessonsList) {
        this.lessonsList = lessonsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leson_item_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LessonsAdapter.ViewHolder holder, int position) {
         holder.setData(position);
    }
    @Override
    public int getItemCount() {
        return lessonsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lesson;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lesson = itemView.findViewById(R.id.tv_lesson);
        }
        private void setData(final int pos){
            // Retrieve lesson text from Firestore
            String lessonText = lessonsList.get(pos).getLesson();

            // Replace "\\n" with actual newline characters
            String formattedLessonText = lessonText.replace("\\n", "\n");

            // Set formatted lesson text in TextView
            lesson.setText(formattedLessonText);
        }
    }
}
