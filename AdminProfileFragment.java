package com.example.a1201418_1200435_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminProfileFragment extends Fragment {

    private EditText etFirstName, etLastName, etPhone, etEmail , etCurrentPass , etNewPass;
    private Button btnSave , btSavePass;
    private DatabaseHelper databaseHelper;

    private String loggedInEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etPhone = view.findViewById(R.id.et_phone);
        btnSave = view.findViewById(R.id.btn_save);
        etEmail = view.findViewById(R.id.et_email);
        etCurrentPass = view.findViewById(R.id.et_current_password);
        etNewPass = view.findViewById(R.id.et_new_password);
        btSavePass = view.findViewById(R.id.btn_save_password);

        databaseHelper = new DatabaseHelper(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            loggedInEmail = bundle.getString("email");
        }

        loadAdminProfile();

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveAdminProfile();
            }
        });

        btSavePass.setOnClickListener(v -> {
            updatePassword();
        });

        return view;
    }

    private void loadAdminProfile() {
        try {
            Cursor cursor = databaseHelper.getUserByEmailA(loggedInEmail);
            if (cursor.moveToFirst()) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.getColumnNameFirstName()));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.getColumnNameLastName()));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.getColumnNamePassword()));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.getColumnNamePhoneNumber()));

                etFirstName.setText(firstName);
                etLastName.setText(lastName);
                etPhone.setText(phoneNumber);
                etEmail.setText(loggedInEmail);
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to open database", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etFirstName.getText())) {
            etFirstName.setError("First name is required");
            return false;
        }
        if (TextUtils.isEmpty(etLastName.getText())) {
            etLastName.setError("Last name is required");
            return false;
        }

        if (TextUtils.isEmpty(etPhone.getText())) {
            etPhone.setError("Phone number is required");
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Email is required");
        }
        return true;
    }

    private void saveAdminProfile() {
        try {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (firstName.length() < 3 || lastName.length() < 3) {
                Toast.makeText(getContext(), "Name should be 3 characters at least", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
                Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!(phone.matches("05\\d{8}"))) {
                Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                return;
            }



            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.getColumnNameFirstName(), firstName);
            values.put(DatabaseHelper.getColumnNameLastName(), lastName);
            values.put(DatabaseHelper.getColumnNamePhoneNumber(), phone);
            values.put(DatabaseHelper.getColumnEmail(), email);

            int rowsAffected = db.update(DatabaseHelper.getTableUsers(), values, DatabaseHelper.getColumnIsAdmin() + " = ?", new String[]{"1"});
            db.close();

            if (rowsAffected > 0) {
                Toast.makeText(getContext(), "Profile saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to save profile!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to open databasesssssssss", Toast.LENGTH_SHORT).show();
        }

    }

    private void updatePassword() {
        try {

            String currentPassword = etCurrentPass.getText().toString().trim();
            String newPassword = etNewPass.getText().toString().trim();

            if (!(newPassword.length() >= 8 && newPassword.matches(".*\\d.*") && newPassword.matches(".*[a-zA-Z].*"))) {
                Toast.makeText(getContext(), "New password should be at least 8 characters long and contain both letters and numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(currentPassword)) {
                etCurrentPass.setError("Current password is required");
                return;
            }

            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            String currentPasswordHashed = PasswordHash.hashPassword(currentPassword);

            Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.getTableUsers() + " WHERE " + DatabaseHelper.getColumnEmail() + "=? AND " + DatabaseHelper.getColumnNamePassword() + "=?", new String[]{loggedInEmail, currentPasswordHashed});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.close();

                String newPasswordHashed = PasswordHash.hashPassword(newPassword);

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.getColumnNamePassword(), newPasswordHashed);

                int rowsAffected = db.update(DatabaseHelper.getTableUsers(), values,
                        DatabaseHelper.getColumnEmail() + " = ?", new String[]{loggedInEmail});
                db.close();

                if (rowsAffected > 0) {
                    Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Incorrect current password", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to open database", Toast.LENGTH_SHORT).show();
        }
    }
}
