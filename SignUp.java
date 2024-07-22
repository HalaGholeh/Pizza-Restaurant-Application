package com.example.a1201418_1200435_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    private EditText emailEditText, phoneNumberEditText, firstNameEditText, lastNameEditText, passwordEditText, confirmPasswordEditText;
    private Spinner genderSpinner;
    private Button signUpButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailSignUp);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        genderSpinner = findViewById(R.id.spinner);
        passwordEditText = findViewById(R.id.passwordSignUp);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.signUp_clicked);

        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        dbHelper = new DatabaseHelper(SignUp.this);


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String gender = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (email.isEmpty() || phoneNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = validateSignUpData(email, phoneNumber, firstName, lastName, gender, password, confirmPassword);
                    if (isValid) {

                        String hashedPassword = PasswordHash.hashPassword(password);
                        User user = new User(email, phoneNumber, firstName, lastName, gender, false, hashedPassword);
                        user.setEmail(email);
                        user.setPhoneNumber(phoneNumber);
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        user.setGender(gender);
                        user.setPassword(hashedPassword);

                        boolean registrationSuccess = dbHelper.insertUser(user);

                        if (registrationSuccess) {
                            Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, Login.class);
                            SignUp.this.startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUp.this, "Please try again.", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private boolean validateSignUpData(String email, String phoneNumber, String firstName, String lastName, String gender, String password, String confirmPassword) {
        if (!(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))){
            Toast.makeText(SignUp.this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(phoneNumber.matches("05\\d{8}"))){
            Toast.makeText(SignUp.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (firstName.length() < 3 || lastName.length() < 3){
            Toast.makeText(SignUp.this, "Name should be 3 character at least", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*"))){
            Toast.makeText(SignUp.this, "Password should be 8 digits at least including characters and numbers", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)){
            Toast.makeText(SignUp.this, "Unmatched password!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}