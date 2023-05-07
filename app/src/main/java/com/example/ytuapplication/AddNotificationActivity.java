package com.example.ytuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ytuapplication.Entities.Notification;
import com.example.ytuapplication.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddNotificationActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText, expirationDateEditText;
    private Button submitButton;
    private DatabaseReference databaseReference, userReference;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        titleEditText = findViewById(R.id.title_edittext);
        contentEditText = findViewById(R.id.content_edittext);
        expirationDateEditText = findViewById(R.id.expiration_date_edittext);
        submitButton = findViewById(R.id.submit_button);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/");
        userReference = database.getReference("users");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    userReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User owner = dataSnapshot.getValue(User.class);
                            if (owner != null) {
                                submitNotification(owner);
                                Intent intent = new Intent(AddNotificationActivity.this, NotificationsActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AddNotificationActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AddNotificationActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddNotificationActivity.this, "No user is logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitNotification(User owner) {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();
        String expirationDateString = expirationDateEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty() || expirationDateString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate expirationDate;

        try {
            expirationDate = LocalDate.parse(expirationDateString);
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid date format. Please use yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return;
        }

        String releaseDate = LocalDate.now().toString();

        Notification notification = new Notification(owner, title, content, releaseDate, expirationDateString, null);
        databaseReference = database.getReference("notifications");
        String notificationId = databaseReference.push().getKey();

        if (notificationId != null) {
            databaseReference.child(notificationId).setValue(notification).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddNotificationActivity.this, "Notification added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddNotificationActivity.this, "Failed to add notification", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AddNotificationActivity.this, "Failed to generate notification ID", Toast.LENGTH_SHORT).show();
        }
    }
}
