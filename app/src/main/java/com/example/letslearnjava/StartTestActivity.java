package com.example.letslearnjava;

import static com.example.letslearnjava.DbQuery.g_catList;
import static com.example.letslearnjava.DbQuery.g_testList;
import static com.example.letslearnjava.DbQuery.loadlessons;
import static com.example.letslearnjava.DbQuery.loadquestions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartTestActivity extends AppCompatActivity {

    private TextView catName, testNo, totalQ, bestScore;
    private Button startTestB;
    private ImageView backB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        init();

        loadquestions(new MyCompletedListener() {
            @Override
            public void onSuccess() {
                setData();
            }
            @Override
            public void onFailure() {
                Toast.makeText(StartTestActivity.this, "error bro",
                        Toast.LENGTH_SHORT).show();
            }
        });

        loadlessons(new MyCompletedListener() {
            @Override
            public void onSuccess() {
                setData();
            }
            @Override
            public void onFailure() {
                Toast.makeText(StartTestActivity.this, "error bro",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        catName.setText(g_testList.get(DbQuery.g_selected_test_index).getTestID());
        // testNo.setText(String.valueOf(DbQuery.g_selected_test_index + 1) + " сабақ");
        totalQ.setText(String.valueOf(DbQuery.g_quesList.size()));
        bestScore.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()));

    }

    private void init(){
        catName = findViewById(R.id.st_cat_name);
       // testNo = findViewById(R.id.st_test_no);
        totalQ = findViewById(R.id.st_total_ques);
        bestScore = findViewById(R.id.st_best_score);
        startTestB = findViewById(R.id.start_testB);
        backB = findViewById(R.id.st_backB);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTestActivity.this.finish();
            }
        });

        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartTestActivity.this, LessonsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}