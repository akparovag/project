package com.example.letslearnjava;

import static com.example.letslearnjava.DbQuery.NOT_VISITED;
import static com.example.letslearnjava.DbQuery.UNANSWERED;
import static com.example.letslearnjava.DbQuery.g_catList;
import static com.example.letslearnjava.DbQuery.g_lessonList;
import static com.example.letslearnjava.DbQuery.g_quesList;
import static com.example.letslearnjava.DbQuery.g_selected_cat_index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letslearnjava.Adapters.LessonsAdapter;

public class LessonsActivity extends AppCompatActivity {
    private RecyclerView lessonsView;
    private TextView tvLesonID, catNameTV;
    private ImageButton prevQuesB, nextQuesB;
    private Button startTestB;
    private ImageView backB;
    private int lesonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        init();

        LessonsAdapter lessonsAdapter = new LessonsAdapter(DbQuery.g_lessonList); // Получаем текст из Firestore
        lessonsView.setAdapter(lessonsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        lessonsView.setLayoutManager(layoutManager);

        setSnapHelper();
        setClickListeners();

        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonsActivity.this, QuestionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init(){
        lessonsView = findViewById(R.id.lessons_view);
        tvLesonID = findViewById(R.id.tv_lesonID);
        catNameTV = findViewById(R.id.qa_catName); // show category name
        prevQuesB = findViewById(R.id.prev_quesB); // prev lesson
        nextQuesB = findViewById(R.id.next_quesB); // show you next lesson
        startTestB = findViewById(R.id.startTest_B); // start test activity
        backB = findViewById(R.id.getOut); // back to main activity
        lesonID = 0;

        tvLesonID.setText("1/" + String.valueOf(g_lessonList.size()));
        catNameTV.setText(g_catList.get(g_selected_cat_index).getName());

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LessonsActivity.this.finish();
            }
        });
    }
    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(lessonsView);

        lessonsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                lesonID = recyclerView.getLayoutManager().getPosition(view);
                tvLesonID.setText(String.valueOf(lesonID + 1) + "/" + String.valueOf(g_lessonList.size()));
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    private void setClickListeners(){
        prevQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lesonID > 0){
                    lessonsView.smoothScrollToPosition(lesonID - 1);
                }
            }
        });
        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lesonID < g_quesList.size() - 1){
                    lessonsView.smoothScrollToPosition(lesonID + 1);
                }

            }
        });
    }
}