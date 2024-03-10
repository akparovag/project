package com.example.letslearnjava;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ScoreActivity extends AppCompatActivity {
    private TextView scoreTV, correctQTV, wrongQTV, unattemptedQTV;
    private BottomNavigationView bottomNavigationView;
    Button leaderB, reAttemptB, viewAnsB;
    private int finalScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        loadData();

        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                startActivity(intent);
            }
        });



        saveResult();
    }




    private void init(){
        scoreTV = findViewById(R.id.score);  //количество очков текст
        correctQTV = findViewById(R.id.correctQ);   //правильно ответил на вопоросы, колическо
        wrongQTV = findViewById(R.id.wrongQ);       //неправильно ответил на вопоросы, колическо
        unattemptedQTV = findViewById(R.id.un_attemptedQ);   //количество не отвеченных вопрос
        viewAnsB = findViewById(R.id.view_answerB);      //кнопко назад на главный экран
        bottomNavigationView = findViewById(R.id.mobile_navigation);

    }

    private void loadData(){
        int correctQ = 0, wrongQ = 0, unattemptQ = 0;

        for (int i=0; i < DbQuery.g_quesList.size(); i++){
            if (DbQuery.g_quesList.get(i).getSelectedAns() == -1){
                unattemptQ ++;
            }else{
                if(DbQuery.g_quesList.get(i).getSelectedAns() == DbQuery.g_quesList.get(i).getCorrectAns()){
                    correctQ++;
                }else {
                    wrongQ++;
                }
            }
        }
        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptedQTV.setText(String.valueOf(unattemptQ));

        finalScore = (correctQ*100)/DbQuery.g_quesList.size();
        scoreTV.setText(String.valueOf(finalScore));
    }

    private void saveResult() {
        DbQuery.saveResult(finalScore, new MyCompletedListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ScoreActivity.this, "Okk", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this, "қате", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}