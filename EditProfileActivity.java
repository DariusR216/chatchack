package com.flightpath.fullflights;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editName, editEmail;
    private ImageView editProfileImage;
    private Button saveProfileBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfileImage = findViewById(R.id.edit_profile_image);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        saveProfileBtn = findViewById(R.id.btn_save_profile);

        // Load existing profile data
        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        editName.setText(prefs.getString("name", ""));
        editEmail.setText(prefs.getString("email", ""));

        String savedImageUri = prefs.getString("profile_image", null);
        if (savedImageUri != null) {
            editProfileImage.setImageURI(Uri.parse(savedImageUri));
        }

        // Handle profile image click
        editProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        // Save profile button click
        saveProfileBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", editName.getText().toString());
            editor.putString("email", editEmail.getText().toString());
            editor.apply();

            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish(); // Close the activity and return to ProfileActivity
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            editProfileImage.setImageURI(selectedImageUri);

            // Save the new profile image in SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("user_profile", MODE_PRIVATE).edit();
            editor.putString("profile_image", selectedImageUri.toString());
            editor.apply();
        }
    }
}
