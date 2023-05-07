package com.example.ytuapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingsActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private StorageReference storageReference;
    private EditText fullNameEditText, emailEditText, enteranceYearEditText, graduateYearEditText, graduateLevelEditText, jobEditText, phoneEditText, newPasswordEditText;
    private Button updateProfileButton, updatePasswordAndEmailButton, backButton, uploadProfilePhotoButton;

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private DatabaseReference usersRef, databaseReference;

    ActivityResultLauncher<String> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result != null) {
                //profileImageView.setImageURI(result);
                uploadProfilePhoto(result);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fullNameEditText = findViewById(R.id.full_name_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        enteranceYearEditText = findViewById(R.id.enterance_year_edittext);
        graduateYearEditText = findViewById(R.id.graduate_year_edittext);
        graduateLevelEditText = findViewById(R.id.graduate_level_edittext);
        jobEditText = findViewById(R.id.job_edittext);
        phoneEditText = findViewById(R.id.phone_edittext);
        newPasswordEditText = findViewById(R.id.new_password_edittext);
        backButton = findViewById(R.id.back_button);
        uploadProfilePhotoButton= findViewById(R.id.upload_profile_photo_button);
        updateProfileButton = findViewById(R.id.update_profile_button);
        updatePasswordAndEmailButton = findViewById(R.id.update_password_button);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
        usersRef = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        //uploadProfilePhotoButton.setOnClickListener(v -> galleryActivityResultLauncher.launch("image/*"));


        updatePasswordAndEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePasswordAndEmail();
                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            }
        });

        uploadProfilePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            uploadProfilePhoto(imageUri);
        }
    }

    private void uploadProfilePhoto(Uri imageUri) {
        if (imageUri != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            StorageReference profilePhotoRef = storageReference.child("profile_photos/" + user.getUid() + ".jpg");

            profilePhotoRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("users")
                                    .child(user.getUid());

                            userRef.child("details").child("profilePhoto").setValue(uri.toString());
                            Toast.makeText(SettingsActivity.this, "Profile photo uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, "Failed to upload profile photo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

   /* private void uploadProfilePhoto(Uri imageUri) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        StorageReference fileReference = storageReference.child(userId + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        DatabaseReference userDetailRef = databaseReference.child(userId).child("details");
                        userDetailRef.child("profilePhoto").setValue(uri.toString())
                                .addOnSuccessListener(aVoid -> Toast.makeText(SettingsActivity.this, "Profile photo updated.", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(SettingsActivity.this, "Failed to update profile photo.", Toast.LENGTH_SHORT).show());
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(SettingsActivity.this, "Failed to upload profile photo.", Toast.LENGTH_SHORT).show());

    }
*/

    private void updatePasswordAndEmail() {

        user = FirebaseAuth.getInstance().getCurrentUser();

       // EditText newPasswordEditText = findViewById(R.id.new_password_edittext);
        String newPassword = newPasswordEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();

        if (newPassword.isEmpty() || newPassword.length() < 6) {
            newPasswordEditText.setError("Password should be at least 6 characters");
            newPasswordEditText.requestFocus();
            return;
        }

        if(newEmail.isEmpty()){
            emailEditText.setError("Email should be added");
            return;
        }

        // Update the user's password
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Update the user's password in the Firebase Realtime Database
                            usersRef = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("users")
                                    .child(user.getUid())
                                    .child("password");
                            usersRef.setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Error updating password in database: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Error updating password in authentication: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    usersRef = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("users")
                            .child(user.getUid())
                            .child("email");
                    usersRef.setValue(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingsActivity.this, "Error updating email in database: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateProfile() {

        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String graduateYear = graduateYearEditText.getText().toString().trim();
        String enteranceYear = enteranceYearEditText.getText().toString().trim();
        String job = jobEditText.getText().toString().trim();
        String graduateLevel = graduateLevelEditText.getText().toString().trim();

        // Get the current user
        user = FirebaseAuth.getInstance().getCurrentUser();


        // Update the user's profile information in the Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://ytu-application-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users")
                .child(user.getUid());

        // Update fullName
        userRef.child("fullName").setValue(fullName);

        // Update user details
        userRef.child("details").child("phone").setValue(phone);
        userRef.child("details").child("graduateYear").setValue(Integer.parseInt(graduateYear));
        userRef.child("details").child("enteranceYear").setValue(Integer.parseInt(enteranceYear));
        userRef.child("details").child("job").setValue(job);
        userRef.child("details").child("graduateLevel").setValue(graduateLevel);

        Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

}