package com.example.a1201418_1200435_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class YourOrdersFragment extends Fragment {
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_orders, container, false);
        ordersRecyclerView = view.findViewById(R.id.recycler_view_your_orders);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        try {
            databaseHelper = new DatabaseHelper(getContext());
            loadOrders();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to open database", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    private void loadOrders() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String currentUserEmail = sharedPreferences.getString("user_email", null);

        if (currentUserEmail != null) {
            ArrayList<Order> ordersList = databaseHelper.getOrdersByUserEmail(currentUserEmail);
            if (ordersList.isEmpty()) {
                Toast.makeText(getContext(), "No orders found", Toast.LENGTH_SHORT).show();
            } else {

                orderAdapter = new OrderAdapter(ordersList, new OrderAdapter.OnOrderClickListener() {
                    @Override
                    public void onOrderClick(Order order) {
                        showOrderDetailsDialog(order);
                    }
                });
                ordersRecyclerView.setAdapter(orderAdapter);
            }
        }
    }

    private void showOrderDetailsDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.order_details, null);
        builder.setView(dialogView);

        TextView textViewOrderDetails = dialogView.findViewById(R.id.text_view_order_details);
        String pizzaName = databaseHelper.getPizzaNameById(order.getPizzaId());
        String orderDetails = "Order Number: " + order.getOrderId() + "\n" +
                "Pizza Type: " + pizzaName + "\n" +
                "Quantity: " + order.getQuantity() + "\n" +
                "Extra Sauce: " + (order.isExtraSauce() ? "Yes" : "No") + "\n" +
                "Extra Cheese: " + (order.isExtraCheese() ? "Yes" : "No") + "\n" +
                "Mix With Beef: " + (order.isMixWithBeef() ? "Yes" : "No") + "\n" +
                "Additional Notes: " + order.getOptional() + "\n" +
                "Date Of Order: " + order.getOrderDate() + "\n" +
                "Total price for order: " + order.getPrice();
        textViewOrderDetails.setText(orderDetails);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
