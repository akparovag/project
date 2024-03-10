package com.example.letslearnjava;

import static com.example.letslearnjava.DbQuery.NOT_VISITED;
import static com.example.letslearnjava.DbQuery.UNANSWERED;
import static com.example.letslearnjava.DbQuery.g_catList;
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

import com.example.letslearnjava.Adapters.QuestionsAdapter;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView questionsView;
    private TextView tvQuesID, catNameTV;
    private ImageButton prevQuesB, nextQuesB;
    private Button exit_B;
    private int quesID;
    private ImageView backB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();

        QuestionsAdapter quesAdapter = new QuestionsAdapter(g_quesList);
        questionsView.setAdapter(quesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);
        
        setSnapHelper();

        setClickListeners();

        exit_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init(){
        questionsView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);       //показатель на какой страницы
        catNameTV = findViewById(R.id.qa_catName);     // имя урока
        prevQuesB = findViewById(R.id.prev_quesB);
        nextQuesB = findViewById(R.id.next_quesB);
        exit_B = findViewById(R.id.exitB);
        backB = findViewById(R.id.getOut);

        quesID = 0;
        tvQuesID.setText("1/" + String.valueOf(g_quesList.size()));
        catNameTV.setText(g_catList.get(g_selected_cat_index).getName());

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionsActivity.this.finish();
            }
        });
    }



    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                if(g_quesList.get(quesID).getStatus() == NOT_VISITED){
                    g_quesList.get(quesID).setStatus(UNANSWERED);
                }

                tvQuesID.setText(String.valueOf(quesID + 1) + "/" + String.valueOf(g_quesList.size()));


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
                if(quesID > 0){
                    questionsView.smoothScrollToPosition(quesID - 1);
                }
            }
        });

        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quesID < g_quesList.size() - 1){
                    questionsView.smoothScrollToPosition(quesID + 1);
                }

            }
        });
    }

}

















