package com.example.a1201418_1200435_project;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddAdminFragment extends Fragment {

    private EditText etFirstName, etLastName, etEmail, etPassword, etPhoneNumber;
    private Spinner spinnerGender;
    private Button btnAddAdmin;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);

        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        spinnerGender = view.findViewById(R.id.spinner_gender);
        btnAddAdmin = view.findViewById(R.id.btn_add_admin);

        databaseHelper = new DatabaseHelper(getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        btnAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    addAdmin();;
                }
            }
        });

        return view;
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
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Password is required");
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())){
            etEmail.setError("Email is required");
        }
        if (TextUtils.isEmpty(etPhoneNumber.getText())) {
            etPhoneNumber.setError("Phone number is required");
            return false;
        }
        return true;
    }

    private void addAdmin() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        String hashedPass = PasswordHash.hashPassword(password);

        if (firstName.length() < 3 || lastName.length() < 3){
            Toast.makeText(getContext(), "Name should be 3 characters at least", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))){
            Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*"))){
            Toast.makeText(getContext(), "Password should be at least 8 characters long and contain both letters and numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!(phoneNumber.matches("05\\d{8}"))){
            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email already exists in the database
        if (databaseHelper.checkIfEmailExists(email)) {
            Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phoneNumber.isEmpty()) {
            boolean success = databaseHelper.insertUser(new User(email, phoneNumber, firstName, lastName, gender, true, hashedPass));

            if (success) {
                Toast.makeText(getContext(), "Admin added successfully", Toast.LENGTH_SHORT).show();
                etFirstName.setText("");
                etLastName.setText("");
                etEmail.setText("");
                etPassword.setText("");
                etPhoneNumber.setText("");
            } else {
                Toast.makeText(getContext(), "Failed to add admin", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

}
