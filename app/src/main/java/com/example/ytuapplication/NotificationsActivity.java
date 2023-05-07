package com.example.ytuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ytuapplication.Entities.Notification;
import com.example.ytuapplication.adapters.NotificationPreviewAdapter;
import com.example.ytuapplication.models.NotificationPreview;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView notificationsRecyclerView;
    private NotificationPreviewAdapter adapter;
    private List<NotificationPreview> notificationPreviews;
    private ImageView usersButton,searchButton,settingsButton;
    private Button addNotificationButton, uploadProfilePhotoButton;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationsRecyclerView = findViewById(R.id.notifications_RecyclerView);
        usersButton = findViewById(R.id.users_button);
        searchButton = findViewById(R.id.search_button);
        settingsButton = findViewById(R.id.settings_button);
        addNotificationButton = findViewById(R.id.add_notification_button);


        notificationsRecyclerView.setHasFixedSize(true);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationPreviews = new ArrayList<>();
        adapter = new NotificationPreviewAdapter(this, notificationPreviews);
        notificationsRecyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase
                .getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("notifications");

        fetchDataFromFirebase();

        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        addNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsActivity.this, AddNotificationActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fetchDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationPreviews.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotificationPreview notificationPreview = new NotificationPreview();
                    Notification notification = snapshot.getValue(Notification.class);
                    notificationPreview.setOwnerName(notification.getOwner().getFullName());
                    notificationPreview.setExpirationDate(notification.getExpirationDate());
                    notificationPreview.setTitle(notification.getTitle());
                    notificationPreview.setNotificationPhoto(notification.getNotificationPhoto());
                    // Check if the notification is not expired
                    LocalDate currentDate = LocalDate.now();
                    LocalDate expirationDate = notificationPreview.getFormattedDate(notificationPreview.getExpirationDate());

                    if (currentDate.isBefore(expirationDate) || currentDate.isEqual(expirationDate)) {
                        notificationPreviews.add(notificationPreview);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Error handling
            }
        });
    }
}