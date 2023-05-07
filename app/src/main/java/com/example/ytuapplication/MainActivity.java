package com.example.ytuapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ytuapplication.adapters.UserPreviewAdapter;
import com.example.ytuapplication.models.UserPreview;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserPreviewAdapter userPreviewAdapter;
    private List<UserPreview> userPreviews;
    private DatabaseReference usersRef;
    private ImageView notificationsButton,searchButton,settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.users_RecyclerView);
        notificationsButton = findViewById(R.id.notifications_button);
        searchButton = findViewById(R.id.search_button);
        settingsButton = findViewById(R.id.settings_button);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userPreviews = new ArrayList<>();
        userPreviewAdapter = new UserPreviewAdapter(this, userPreviews);
        recyclerView.setAdapter(userPreviewAdapter);

        usersRef = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
        fetchUsers();

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
    private void fetchUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPreviews.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fullName = snapshot.child("fullName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String profilePhotoUrl = snapshot.child("details").child("profilePhoto").getValue(String.class);

                    UserPreview userPreview = new UserPreview(fullName, email, profilePhotoUrl != null ? Uri.parse(profilePhotoUrl) : null);
                    userPreviews.add(userPreview);
                }
                userPreviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}