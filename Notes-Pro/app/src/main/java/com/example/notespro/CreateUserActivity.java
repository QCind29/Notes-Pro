package com.example.notespro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;


public class CreateUserActivity extends AppCompatActivity {
     EditText edEmail, edPass, edCPass;
     Button btCreate;
     ProgressBar progressBar;
     TextView txtLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_activity);
        
        edEmail = findViewById(R.id.edCreateMail);
        edPass = findViewById(R.id.edCreatePassword);
        edCPass = findViewById(R.id.edCreateConfirmPassword);

        txtLogin = findViewById(R.id.txtLogin);

        progressBar = findViewById(R.id.ProgressBar);
        
        btCreate = findViewById(R.id.btCreate);
        
        btCreate.setOnClickListener(view -> createAccount());

        txtLogin.setOnClickListener(view -> finish());
        
        


    }

     void createAccount() {
        String email = edEmail.getText().toString();
        String pass = edPass.getText().toString();
        String confirmpass = edCPass.getText().toString();

        boolean isValidated = validateData(email, pass, confirmpass);
        if(!isValidated){
            return;
        }
        createAccountFirebase(email,pass);
    }

     void createAccountFirebase(String email, String pass) {
        changeInProgress(true);

         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(CreateUserActivity.this,
                 new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         changeInProgress(false);
                         if(task.isSuccessful()){
                             Utility.showToast(CreateUserActivity.this, "Successfully!, check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                         }else {
                             Utility.showToast(CreateUserActivity.this,task.getException().getLocalizedMessage());
                         }
                     }
                 });


     }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btCreate.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btCreate.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String pass, String confirmpass) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Email is invalid");
            return false;
        }
        if (pass.length() < 6) {
            edPass.setError("Password length is invalid");
            return false;
        }
        if (!confirmpass.equals(pass)){
            edCPass.setError("Password is not matches");
            return false;
        }
        return true;
    }
}
