package com.example.letslearnjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


public class MyProfileActivity extends AppCompatActivity {
    private EditText name, email, phone;
    private LinearLayout editB, button_layout;
    private Button cancelB, saveB;
    private TextView profileText;
    private String nameStr, phoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Менің Профилім");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.mp_name);
        email = findViewById(R.id.mp_email);
        phone = findViewById(R.id.mp_phone);
        profileText = findViewById(R.id.profile_text);
        editB = findViewById(R.id.editB);
        cancelB = findViewById(R.id.cancelB);
        saveB = findViewById(R.id.saveB);
        button_layout = findViewById(R.id.button_layout);

        disableEditing();

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEditing();
            }
        });

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    saveData();
                }
            }
        });
    }

    private void enableEditing() {
        name.setEnabled(true);  // Изменено на true
        email.setEnabled(true);  // Изменено на true
        phone.setEnabled(true);  // Изменено на true

        button_layout.setVisibility(View.VISIBLE);
    }


    private void disableEditing() {
        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);

        button_layout.setVisibility(View.GONE);

        name.setText(DbQuery.myProfile.getName());
        email.setText(DbQuery.myProfile.getEmail());

        if(DbQuery.myProfile.getPhone() != null)
            phone.setText(DbQuery.myProfile.getPhone());

        String profileName = DbQuery.myProfile.getName();

        profileText.setText(profileName.toUpperCase().substring(0,1));
    }

    private boolean validate(){
        nameStr = name.getText().toString();
        phoneStr = phone.getText().toString();

        if (nameStr.isEmpty()){
            name.setError("Аты бос болуы мүмкін емес");
            return false;
        }
        if (phoneStr.isEmpty()){
            phone.setError("Аты бос болуы мүмкін емес");
            return false;
        }
        return true;
    }

    private void saveData(){
        if (phoneStr.isEmpty())
            phoneStr = null;

        DbQuery.saveProfileData(nameStr, phoneStr, new MyCompletedListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this, "Профиль сәтті жаңартылды", Toast.LENGTH_SHORT).show();
                disableEditing();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this, "Профильді сақтау қате өтті", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            MyProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}