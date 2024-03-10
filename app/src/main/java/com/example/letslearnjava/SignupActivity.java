package com.example.letslearnjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    private String emailStr, passStr, nameStr;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()) {
                    signupNewUser();
                }
            }
        });
    }


    private boolean validate() {
        emailStr = signupEmail.getText().toString().trim();
        passStr = signupPassword.getText().toString().trim();
        nameStr = signupUsername.getText().toString().trim();

        if (emailStr.isEmpty()){
            signupEmail.setError("Электрондық пошта идентификаторын енгізіңіз");
            return false;
        }
        if (passStr.isEmpty()){
            signupPassword.setError("Құпия сөзді енгізіңіз");
            return false;
        }
        if (nameStr.isEmpty()){
            signupUsername.setError("Құпия сөзді енгізіңіз");
            return false;
        }
        return true;
    }
    private void signupNewUser(){
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SignupActivity.this,"Сәтті тіркелдіңіз",Toast.LENGTH_SHORT).show();

                            DbQuery.createUserData(emailStr, nameStr, new MyCompletedListener() {
                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new MyCompletedListener() {
                                        @Override
                                        public void onSuccess() {
                                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            SignupActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignupActivity.this, "қате",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    SignupActivity.this.finish();
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(SignupActivity.this,"бірдеңе дұрыс болмады! Кейінірек қайталап көріңіз", Toast.LENGTH_SHORT).show();

                                }
                            });

                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            SignupActivity.this.finish();

                        } else {
                            Toast.makeText(SignupActivity.this, "Аутентификация сәтсіз аяқталды.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}