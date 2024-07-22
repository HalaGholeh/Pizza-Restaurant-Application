package com.example.a1201418_1200435_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button getStartButton;
    private ArrayList<String> pizzaTypes = new ArrayList<>();
    private ArrayList<String> pizzaCategories = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        try {
            databaseHelper = new DatabaseHelper(MainActivity.this);
            databaseHelper.initializeAdminUser();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to open database", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getStartButton = findViewById(R.id.get_startButton);

        getStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPizzaTypes().execute();
            }
        });
    }

    private class GetPizzaTypes extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                System.out.println(conn.getInputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                conn.disconnect();
                return content.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(MainActivity.this, "Successfully loaded pizza types", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("types");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        pizzaTypes.add(jsonArray.getString(i));
                    }

                    initializePizzaCategories();

                    databaseHelper.insertPizzaTypes(pizzaTypes, pizzaCategories);

                    Intent intent = new Intent(MainActivity.this, logIn_signUp.class);
                    intent.putStringArrayListExtra("pizzaTypes", pizzaTypes);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error parsing pizza types", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Error loading pizza types", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializePizzaCategories() {
        pizzaCategories.add("Veggies");
        pizzaCategories.add("Veggies");
        pizzaCategories.add("Beef");
        pizzaCategories.add("Beef");
        pizzaCategories.add("Veggies");
        pizzaCategories.add("Veggies");
        pizzaCategories.add("Chicken");
        pizzaCategories.add("Chicken");
        pizzaCategories.add("Seafood");
        pizzaCategories.add("Veggies");
        pizzaCategories.add("Chicken");
        pizzaCategories.add("Veggies");
        pizzaCategories.add("Chicken");
    }
}

