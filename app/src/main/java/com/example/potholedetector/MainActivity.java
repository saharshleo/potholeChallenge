package com.example.potholedetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {
    private String email, password, username;
    private Button signup;
    private EditText editTextUserName, editTextEmail, editTextPassword;
    private TextView loginPage;

    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = (Button) findViewById(R.id.btn_signup);
        editTextUserName = (EditText) findViewById(R.id.username);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
    //        if(mAuth.getCurrentUser().isAnonymous() == false){
//            Intent intent = new Intent(MainActivity.this,RegisterPathole.class);
//            startActivity(intent);
//        }
    signupFunc();

    loginPageCreate();

}
    private void loginPageCreate() {
        loginPage = (TextView)findViewById(R.id.textViewLoginRedirect);
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterPothole.class);
                startActivity(intent);
            }
        });
    }

    private void signupFunc() {
        //FirebaseUser currentUser = mAuth.getCurrentUser();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkIfCorrect()){
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            private static final String TAG = "Signup";

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    // FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(MainActivity.this,RegisterPothole.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });
    }

    private boolean checkIfCorrect(){
        username = editTextUserName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        if(username.isEmpty() ){
            Toast.makeText(MainActivity.this,"Please enter Username",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty()){
            Toast.makeText(MainActivity.this,"Please enter Email",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(MainActivity.this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
                return false;
        }
        if(password.isEmpty()){
            Toast.makeText(MainActivity.this,"Please enter Password",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValidPassword(password)){
                Toast.makeText(MainActivity.this,"Password lenght : 6-10 chars",Toast.LENGTH_SHORT).show();
                return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if(password.length() >= 6 && password.length() <=10){
            return true;
        }else{
            return false;
        }
    }
}
