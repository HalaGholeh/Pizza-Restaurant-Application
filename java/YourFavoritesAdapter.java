package com.example.a1201418_1200435_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class YourFavoritesAdapter extends RecyclerView.Adapter<YourFavoritesAdapter.FavoriteViewHolder> {

    private static List<Favorite> favoritesList;
    DatabaseHelper databaseHelper ;

    public YourFavoritesAdapter(List<Favorite> favoritesList) {
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite favorite = favoritesList.get(position);
        holder.bindFavorite(favorite);
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private TextView favoriteDetailsTextView;
        private Button undoButton;
        private Button orderButton;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteDetailsTextView = itemView.findViewById(R.id.text_view_favorite_details);
            undoButton = itemView.findViewById(R.id.button_undo_fav);
            orderButton = itemView.findViewById(R.id.button_order_from_fav);
            databaseHelper = new DatabaseHelper(itemView.getContext());
        }

        public void bindFavorite(Favorite favorite) {
            String pizzaName = databaseHelper.getPizzaNameById(favorite.getPizzaId());
            favoriteDetailsTextView.setText(pizzaName);

            undoButton.setOnClickListener(v -> {
                removeFavorite(favorite);
            });
            orderButton.setOnClickListener(v -> {
                orderPizza(favorite.getPizzaId());
            });
        }
        private void removeFavorite(Favorite favorite) {
            DatabaseHelper databaseHelper = new DatabaseHelper(itemView.getContext());
            boolean success = databaseHelper.removeFavorite(favorite);

            if (success) {
                favoritesList.remove(favorite);
                notifyDataSetChanged();
                Toast.makeText(itemView.getContext(), "Favorite removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(itemView.getContext(), "Failed to remove favorite", Toast.LENGTH_SHORT).show();
            }
        }
        private void orderPizza(int pizzaId){
            String pizzaName = databaseHelper.getPizzaNameById(pizzaId);
            showUserDialog(pizzaName);

        }
        private void showUserDialog(String pizzaName) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
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

                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
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
                            Toast.makeText(itemView.getContext(), "Added order successfully", Toast.LENGTH_SHORT).show();
                            Toast.makeText(itemView.getContext(), String.format("Total price: $%.2f", price), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(itemView.getContext(), "Size should be chosen", Toast.LENGTH_SHORT).show();
                    }

                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }

    }
}
