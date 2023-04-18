package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText edEmail, edPass;
    Button btLogin;
    ProgressBar progressBar;
    TextView txtCreate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.edCreateMail);
        edPass = findViewById(R.id.edCreatePassword);

        btLogin = findViewById(R.id.btLogin);
        progressBar = findViewById(R.id.ProgressBar);

        txtCreate = findViewById(R.id.txtCreate);

        txtCreate.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, CreateUserActivity.class)));
        
        btLogin.setOnClickListener(view -> loginAccount());
    }

     void loginAccount() {
        String email = edEmail.getText().toString();
        String pass = edPass.getText().toString();


         boolean isValidated = validateData(email, pass);
         if(!isValidated){
             return;
         }
         loginAccountFirebase(email,pass);
    }

    void refresh() {
        Intent refresh = new Intent(LoginActivity.this,LoginActivity.class);
        finish();
        startActivity(refresh);

    }
    void loginAccountFirebase(String email, String pass) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(true);
                if(task.isSuccessful()){
                    //login successful
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                       // refresh();

                    }else {
                        Utility.showToast(LoginActivity.this, "Email is not verified, Please verify email again.");
                        refresh();
                    }
                }else {
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                    refresh();
                }



            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btLogin.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btLogin.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String pass) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Email is invalid");
            return false;
        }
        if (pass.length() < 6) {
            edPass.setError("Password length is invalid");
            return false;
        }

        return true;
    }
}