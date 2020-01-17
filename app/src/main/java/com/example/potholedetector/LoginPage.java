package com.example.potholedetector;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;

public class LoginPage extends AppCompatActivity {
    private String email,password;
    private Button login;
    private EditText editTextUserPassword,editTextEmail;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        login = (Button)findViewById(R.id.btn_login);
        editTextEmail = (EditText)findViewById(R.id.emailLogin);
        editTextUserPassword = (EditText)findViewById(R.id.passwordLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString().trim();
                password = editTextUserPassword.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(LoginPage.this,"Please enter Email",Toast.LENGTH_SHORT).show();
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Toast.makeText(LoginPage.this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
                    }
                }
                if(password.isEmpty()){
                    Toast.makeText(LoginPage.this,"Please enter Password",Toast.LENGTH_SHORT).show();
                    if(!isValidPassword(password)){
                        Toast.makeText(LoginPage.this,"Password lenght : 6-10 chars",Toast.LENGTH_SHORT).show();
                    }
                }
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginPage.this,"Login sucessful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginPage.this,RegisterPothole.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Some problem has occured!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private boolean isValidPassword(String password) {
        if(password.length() >= 6 && password.length() <=10){
            return true;
        }else{
            return false;
        }
    }

}
