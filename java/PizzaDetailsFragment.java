package com.example.a1201418_1200435_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PizzaDetailsFragment extends Fragment {
    private static final String ARG_PIZZA_NAME = "pizzaName";
    private static final String ARG_PIZZA_CATEGORY = "pizzaCategory";
    private static final String ARG_PIZZA_PRICE = "pizzaPrice";
    private static final String ARG_PIZZA_SIZE = "pizzaSize";
    private static final String ARG_PIZZA_POSITION = "pizzaPosition";
    private String pizzaName;
    private String pizzaCategory;
    private double pizzaPrice;
    private String pizzaSize;
    DatabaseHelper databaseHelper;
    private int pizzaPosition;
    private int[] pizzaImages = {
            R.drawable.margarita,
            R.drawable.neapolitan,
            R.drawable.hawaiian,
            R.drawable.pepperoni,
            R.drawable.new_york_style,
            R.drawable.calzone,
            R.drawable.tandoori_chicken,
            R.drawable.bbq,
            R.drawable.seafood_pizza,
            R.drawable.vegetarian,
            R.drawable.buffalo_chicken,
            R.drawable.mushroom,
            R.drawable.pesto_chicken
    };

    public static PizzaDetailsFragment newInstance(String name, String category, double price, String size) {
        PizzaDetailsFragment fragment = new PizzaDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PIZZA_NAME, name);
        args.putString(ARG_PIZZA_CATEGORY, category);
        args.putDouble(ARG_PIZZA_PRICE, price);
        args.putString(ARG_PIZZA_SIZE, size);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
            pizzaCategory = getArguments().getString(ARG_PIZZA_CATEGORY);
            pizzaPrice = getArguments().getDouble(ARG_PIZZA_PRICE);
            pizzaSize = getArguments().getString(ARG_PIZZA_SIZE);
        }
        databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_details, container, false);

        ImageView pizzaImage = view.findViewById(R.id.pizza_imageView);
        TextView nameTextViewM = view.findViewById(R.id.pizza_nameM);
        Button addToFavM = view.findViewById(R.id.add_to_favM);
        Button orderM = view.findViewById(R.id.order_nowM);

        ArrayList<Pizza> pizzaDetails = databaseHelper.getPizzaDetailsByName(pizzaName);
        if (!pizzaDetails.isEmpty()) {

                Pizza pizzaM = pizzaDetails.get(0);
                int imageIndex = (pizzaM.getId() / 3);


                if (imageIndex >= 0 && imageIndex < pizzaImages.length) {
                    pizzaImage.setImageResource(pizzaImages[imageIndex]);
                } else {
                    pizzaImage.setImageResource(R.drawable.margarita);
                }


            String pizzaDetailsStringM = "Pizza Name: " + pizzaM.getName() +
                        "\nCategory: " + pizzaM.getCategory();
                nameTextViewM.setText(pizzaDetailsStringM);
                addToFavM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        String currentUserEmail = sharedPreferences.getString("user_email", null);
                        if (currentUserEmail != null) {
                            databaseHelper.addToFavorites(currentUserEmail, pizzaM);
                            addToFavM.setBackgroundResource(R.drawable.favorite);
                            Toast.makeText(getContext(), "Pizza added to favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                orderM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUserDialog(pizzaM.getName());
                    }
                });



        } else {
            nameTextViewM.setText("Pizza not found");
        }

        return view;
    }

    private void showUserDialog(String pizzaName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.user_order_dialog, null);
        builder.setView(dialogView);


        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupForOrders);
        CheckBox checkBoxExtraSauce = dialogView.findViewById(R.id.checkbox_extra_sauce);
        CheckBox checkBoxExtraCheese = dialogView.findViewById(R.id.checkbox_extra_cheese);
        CheckBox checkBoxMixWithBeef = dialogView.findViewById(R.id.checkbox_mix_with_beef);
        EditText editTextNote = dialogView.findViewById(R.id.edit_text_note);

        RadioButton small = dialogView.findViewById(R.id.smallRadioButton);
        RadioButton medium = dialogView.findViewById(R.id.meduimRadioButton);
        RadioButton large = dialogView.findViewById(R.id.largeRadioButton);

        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
        Button buttonConfirm = dialogView.findViewById(R.id.button_confirm);


        AlertDialog alertDialog = builder.create();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size="";
                if (small.isChecked())
                    size = "Small";
                else if (medium.isChecked())
                    size = "Medium";
                else if (large.isChecked())
                    size = "Large";


                boolean extraSauce = checkBoxExtraSauce.isChecked();
                boolean extraCheese = checkBoxExtraCheese.isChecked();
                boolean mixWithBeef = checkBoxMixWithBeef.isChecked();
                String note = editTextNote.getText().toString().trim();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String currentUserEmail = sharedPreferences.getString("user_email", null);
                int quantity = 1;

                if (currentUserEmail != null && !size.equals("")) {
                    databaseHelper.getAllPizzas();
                    int pizzaId = databaseHelper.getPizzaIdByNameAndSize(pizzaName,size);
                    double price = databaseHelper.getPizzaPrice(pizzaId);
                    if (extraCheese){price = price+1;}
                    if (extraSauce){price = price+1;}
                    if (mixWithBeef){price = price+1;}
                    boolean added = databaseHelper.addOrder(currentUserEmail,pizzaId , quantity, extraSauce, extraCheese, mixWithBeef, note, currentDate, price);
                    if (added) {
                        Toast.makeText(getContext(), "added order successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), String.format("Total price: $%.2f", price), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_email", null);
    }
}
