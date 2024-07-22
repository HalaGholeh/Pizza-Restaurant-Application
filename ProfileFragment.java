package com.example.a1201418_1200435_project;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;


public class ProfileFragment extends Fragment {

    private TextView emailTextView;
    private EditText phoneNumberEditText, firstNameEditText, lastNameEditText, currentPasswordEditText, passwordEditText, confirmPasswordEditText;
    private Button updateProfile;
    private DatabaseHelper databaseHelper;
    private static final int PICK_IMAGE = 1;
    private ImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        emailTextView = view.findViewById(R.id.email_update_profile);
        phoneNumberEditText = view.findViewById(R.id.phoneNumber_update_profile);
        firstNameEditText = view.findViewById(R.id.firstName_update_profile);
        lastNameEditText = view.findViewById(R.id.lastName_update_profile);
        currentPasswordEditText = view.findViewById(R.id.current_password_update_profile);
        passwordEditText = view.findViewById(R.id.new_password_update_profile);
        confirmPasswordEditText = view.findViewById(R.id.confirm_new_password_update_profile);
        updateProfile = view.findViewById(R.id.update_profile_clicked);


        databaseHelper = new DatabaseHelper(getContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        profileImage = view.findViewById(R.id.profile_image);
        loadProfileImage(userEmail);
        Button selectImageButton = view.findViewById(R.id.change_profile_picture_button);
        selectImageButton.setOnClickListener(v -> openGallery());


        emailTextView.setText(userEmail);
        User user = databaseHelper.getUserByEmail(userEmail);
        phoneNumberEditText.setText(user.getPhoneNumber());
        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String currentPassword = currentPasswordEditText.getText().toString().trim();
                String newPassword = passwordEditText.getText().toString().trim();
                String confirmNewPassword = confirmPasswordEditText.getText().toString().trim();

                if (phoneNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    String hashedCurrentPassword = PasswordHash.hashPassword(currentPassword);
                    boolean currentPasswordMatched = checkCurrentPassword(hashedCurrentPassword, userEmail);
                    if (currentPasswordMatched) {
                        boolean isValid = validateData(phoneNumber, firstName, lastName, newPassword, confirmNewPassword);
                        if (isValid) {
                            String hashedPassword = PasswordHash.hashPassword(newPassword);


                            boolean updatingSuccess = databaseHelper.updateUserProfile(userEmail, phoneNumber, firstName, lastName, hashedPassword);

                            if (updatingSuccess) {
                                Toast.makeText(getContext(), "Information updated successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), "Updating information failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Unmatched current password!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        return view;
    }

    private boolean validateData( String phoneNumber, String firstName, String lastName, String newPassword, String confirmNewPassword) {

        if (!(phoneNumber.matches("05\\d{8}"))){
            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (firstName.length() < 3 || lastName.length() < 3){
            Toast.makeText(getContext(), "Name should be 3 character at least", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(newPassword.length() >= 8 && newPassword.matches(".*\\d.*") && newPassword.matches(".*[a-zA-Z].*"))){
            Toast.makeText(getContext(), "Password should be 8 digits at least including characters and numbers", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPassword.equals(confirmNewPassword)){
            Toast.makeText(getContext(), "Unmatched password!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean checkCurrentPassword(String currentPassword, String userEmail){
        if (databaseHelper.checkOldPassword(currentPassword, userEmail)){
            return true;
        }else{
            return false;
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                Glide.with(this)
                        .load(selectedImage)
                        .transform(new CircleCrop())
                        .into(profileImage);

                saveProfileImage(selectedImage.toString());
            }
        }
    }

    private void loadProfileImage(String userEmail) {
        String profileImageUri = databaseHelper.getProfileImage(userEmail);
        if (profileImageUri != null && !profileImageUri.isEmpty()) {
            Glide.with(this)
                    .load(profileImageUri)
                    .circleCrop()
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.user1);
        }
    }

    private void saveProfileImage(String imageUri) {

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        boolean pictureChanged = databaseHelper.changeProfilePicture(imageUri, userEmail);
        if (pictureChanged){
            Toast.makeText(getContext(), "Image changed successfully!", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getContext(), "Failed changing image!", Toast.LENGTH_SHORT).show();
        }

    }

}