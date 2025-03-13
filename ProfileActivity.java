package com.flightpath.fullflights;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName, profileEmail, flightSummary;
    private ImageView profileImage;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch darkModeSwitch;

    // Activity Result Launcher for returning updated profile data
    private final ActivityResultLauncher<Intent> editProfileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadProfileData(); // Refresh profile info after editing
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        flightSummary = findViewById(R.id.flight_summary);
        darkModeSwitch = findViewById(R.id.switch_dark_mode);
        Button editProfileBtn = findViewById(R.id.btn_edit_profile);

        // Load user profile data
        loadProfileData();

        // Dark Mode Toggle
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        darkModeSwitch.setChecked(prefs.getBoolean("dark_mode", false));

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // **Edit Profile Button Click Listener**
        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            editProfileLauncher.launch(intent);
        });
    }

    // Load saved profile data from SharedPreferences
    protected void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        profileName.setText(prefs.getString("name", "John Doe"));
        profileEmail.setText(prefs.getString("email", "johndoe@example.com"));

        String savedImageUri = prefs.getString("profile_image", null);
        if (savedImageUri != null) {
            profileImage.setImageURI(Uri.parse(savedImageUri));
        }
    }
}
