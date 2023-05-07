package com.example.ytuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ytuapplication.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseDatabase database;

    private EditText signUpName, signUpEmail, signUpPassword;
    private Button signUpButton;
    private TextView loginRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        signUpName = findViewById(R.id.signup_name);
        signUpEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signup_button);
        loginRedirect = findViewById(R.id.login_redirect_text);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                database = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/");
                reference = database.getReference("users");

                String userName =signUpName.getText().toString();
                String userMail = signUpEmail.getText().toString().trim();
                String userPassword = signUpPassword.getText().toString().trim();

                if (userName.isEmpty()){
                    signUpName.setError("User name cannot be empty");
                } else if (userMail.isEmpty()) {
                    signUpEmail.setError("Email cannot be empty");
                } else if (userPassword.isEmpty()) {
                    signUpPassword.setError("Password cannot be empty");
                }  else{

                    auth.createUserWithEmailAndPassword(userMail, userPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"SignUp successful",Toast.LENGTH_SHORT).show();
                                        User user = new User(userName,userMail,userPassword);
                                        reference.child(auth.getCurrentUser().getUid()).setValue(user).addOnSuccessListener(aVoid -> {
                                            Toast.makeText(RegisterActivity.this, "Added to database", Toast.LENGTH_SHORT).show();
                                        });
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"SignUp Failed" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }

            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}