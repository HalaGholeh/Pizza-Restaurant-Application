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

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private List<Offer> offerList;
    private DatabaseHelper databaseHelper;
    private Context context;

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        public TextView pizzaOfferedTextView;
        public TextView startDateTextView;
        public TextView durationTextView;
        public TextView discountTextView;
        public TextView priceTextView;
        public Button orderNow;

        public OfferViewHolder(View itemView) {
            super(itemView);
            pizzaOfferedTextView = itemView.findViewById(R.id.text_view_offer_pizza);
            startDateTextView = itemView.findViewById(R.id.text_view_offer_start_date);
            durationTextView = itemView.findViewById(R.id.text_view_offer_duration);
            discountTextView = itemView.findViewById(R.id.text_view_offer_discount);
            priceTextView = itemView.findViewById(R.id.text_view_offer_price);
            orderNow = itemView.findViewById(R.id.order_offer_btn);
        }
    }

    public OfferAdapter(Context context, List<Offer> offerList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.offerList = offerList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offered, parent, false);
        return new OfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        String pizzaOfferedName = databaseHelper.getPizzaNameById(offer.getPizzaId());

        if (pizzaOfferedName == null || pizzaOfferedName.isEmpty()) {
            Toast.makeText(context, "Pizza not found", Toast.LENGTH_SHORT).show();
        } else {
            double pizzaPrice = databaseHelper.getPizzaPrice(offer.getPizzaId());
            holder.pizzaOfferedTextView.setText(pizzaOfferedName);
            holder.startDateTextView.setText(offer.getDate());
            holder.durationTextView.setText("For" +  String.valueOf(offer.getPeriod()) + " Days");
            holder.discountTextView.setText(String.format("%.0f%%", offer.getDiscount() * 100));
            holder.priceTextView.setText(String.format("$%.2f", pizzaPrice - pizzaPrice*offer.getDiscount()) + " Instead of " + String.format("$%.2f", pizzaPrice));
            double newPrice = pizzaPrice - pizzaPrice*offer.getDiscount();

            holder.orderNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderPizza(offer.getPizzaId(), newPrice);
                }
            });
        }
    }

    private void orderPizza(int pizzaId, double newPrice){
        String pizzaName = databaseHelper.getPizzaNameById(pizzaId);
        showUserDialog(pizzaName, newPrice);

    }
    private void showUserDialog(String pizzaName, double newPrice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
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

                SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String currentUserEmail = sharedPreferences.getString("user_email", null);
                int quantity = 1;

                if (currentUserEmail != null && !size.equals("")) {
                    databaseHelper.getAllPizzas();
                    int pizzaId = databaseHelper.getPizzaIdByNameAndSize(pizzaName,size);
                    double price = newPrice;
                    if (extraCheese){price = price+1;}
                    if (extraSauce){price = price+1;}
                    if (mixWithBeef){price = price+1;}
                    boolean added = databaseHelper.addOrder(currentUserEmail,pizzaId , quantity, extraSauce, extraCheese, mixWithBeef, note, currentDate, price);
                    if (added) {
                        Toast.makeText(context, "Added order successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, String.format("Total price: $%.2f", price), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Size should be chosen", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}
