package com.example.letslearnjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmailname, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmailname = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        //  нет кнопки "забыл пароль" и "вести почту"

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {
                    login();
                }
            }
        });

        // переносит на страницу для ренистрации
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }



    private boolean validateData() {
        if (loginEmailname.getText().toString().isEmpty()){
            loginEmailname.setError("E-mail сөзді енгізіңіз");
            return false;
        }
        if (loginPassword.getText().toString().isEmpty()){
            loginPassword.setError("password сөзді енгізіңіз");
            return false;
        }

        return true;
    }

    private void login() {

        mAuth.signInWithEmailAndPassword(loginEmailname.getText().toString().trim(), loginPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,"кіру сәтті өтті", Toast.LENGTH_SHORT).show();

                            DbQuery.loadData(new MyCompletedListener() {
                                @Override
                                public void onSuccess() {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(LoginActivity.this, "қате",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });


                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}