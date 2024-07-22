package com.example.a1201418_1200435_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AddSpecialOfferFragment extends Fragment {

    private Spinner spinner;
    private EditText duratioin;
    private EditText discounttxt;
    private RadioGroup radioGroup;
    private Button addOfferBtn;

    private ArrayList<Pizza> pizzaList;
    private ArrayAdapter<Pizza> pizzaAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_special_offer, container, false);

        spinner = view.findViewById(R.id.spinner);
        duratioin = view.findViewById(R.id.editText1);
        discounttxt = view.findViewById(R.id.editText2);
        radioGroup = view.findViewById(R.id.radioGroup);
        addOfferBtn = view.findViewById(R.id.addOffer_Button);

        databaseHelper = new DatabaseHelper(getContext());
        pizzaList = new ArrayList<>();

        pizzaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pizzaList);
        pizzaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(pizzaAdapter);

        loadPizzaData();

        addOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    Pizza selectedPizza = (Pizza) spinner.getSelectedItem();
                    String size = ((RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    int pizzaId = databaseHelper.getPizzaIdByNameAndSize(selectedPizza.getName(), size);

                    if (pizzaId != -1) {
                        // Inputs have been validated, so we can safely use them
                        double discount = Double.parseDouble(discounttxt.getText().toString().trim());
                        int period = Integer.parseInt(duratioin.getText().toString().trim());

                        boolean success = databaseHelper.addSpecialOffer(pizzaId, discount, period);
                        if (success) {
                            Toast.makeText(getContext(), "Offer added successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to add offer.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Pizza not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return view;
    }

    private void loadPizzaData() {
        pizzaList.clear();
        pizzaList.addAll(databaseHelper.getDistinctPizzaTypes());
        pizzaAdapter.notifyDataSetChanged();
    }

    private boolean validateInputs() {
        Pizza selectedPizza = (Pizza) spinner.getSelectedItem();
        String durationStr = duratioin.getText().toString().trim();
        String discountStr = discounttxt.getText().toString().trim();
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (selectedPizza == null || durationStr.isEmpty() || discountStr.isEmpty() || selectedRadioButtonId == -1) {
            Toast.makeText(getContext(), "Please fill all inputs.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if duration is an integer
        int duration;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0) {
                Toast.makeText(getContext(), "Duration must be a positive integer.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Duration must be an integer.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if discount is a double and within the range 0 to 1
        try {
            double discount = Double.parseDouble(discountStr);
            if (discount < 0 || discount > 1) {
                Toast.makeText(getContext(), "Discount must be between 0 and 1.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Discount must be a number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
