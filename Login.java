package com.example.a1201418_1200435_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText emailtv;
    private CheckBox rememberMe;
    private EditText passwordtv;
    private Button loginButton;

    private SharedPreferences sharedPreferences;
    private static final String PREF_EMAIL = "email";

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailtv = findViewById(R.id.email);
        rememberMe = findViewById(R.id.rememberMe);
        passwordtv = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_clicked);
        dbHelper = new DatabaseHelper(Login.this);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String savedEmail = sharedPreferences.getString(PREF_EMAIL, "");
        if (!savedEmail.isEmpty()) {
            emailtv.setText(savedEmail);
            rememberMe.setChecked(true);
        }

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String email = emailtv.getText().toString().trim();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREF_EMAIL, email);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(PREF_EMAIL);
                    editor.apply();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailtv.getText().toString().trim();
                String password = passwordtv.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean isValid = dbHelper.authenticateUser(email, password);
                    if (isValid) {
                        if(dbHelper.isAdmin(email)){
                            Intent adminMenuIntent = new Intent(Login.this, AdminMenu.class);
                            adminMenuIntent.putExtra("email", email);
                            startActivity(adminMenuIntent);
                            finish();
                        }else {
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_email", email);
                            editor.apply();

                            Intent intent = new Intent(Login.this, SpecialMenu.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
