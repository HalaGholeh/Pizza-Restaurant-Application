package com.example.a1201418_1200435_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class ShowOrdersFragment extends Fragment {

    private TextView textViewOrdersLeft;
    private TextView textViewPizzasRight;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_orders, container, false);
        textViewOrdersLeft = view.findViewById(R.id.textViewOrdersLeft);
        textViewPizzasRight = view.findViewById(R.id.textViewOrdersRight);
        dbHelper = new DatabaseHelper(getContext());

        loadOrders();
        loadPizzaTypes();

        return view;
    }

    private void loadOrders() {
        ArrayList<Order> orders = dbHelper.getAllOrders();
        StringBuilder ordersText = new StringBuilder();
        for (Order order : orders) {
            ordersText.append("Order ID: ").append(order.getOrderId())
                    .append("\n")
                    .append("Name: ").append(order.getFirstName()).append(" ").append(order.getLastName())
                    .append("\n")
                    .append("Date: ").append(order.getOrderDate()).append("\n")
                    .append("\n");

        }
        textViewOrdersLeft.setText(ordersText.toString());
    }

    private void loadPizzaTypes() {
        ArrayList<Pizza> pizzaInfoList = dbHelper.getPizzaOrderInfo();
        StringBuilder pizzasText = new StringBuilder();
        double totalIncome = 0.0;

        for (Pizza pizzaInfo : pizzaInfoList) {
            pizzasText.append("Pizza: ").append(pizzaInfo.getName())
                    .append("\n")
                    .append("Ordered Quantity: ").append(pizzaInfo.getOrderCount())
                    .append("\n")
                    .append("Total Income: $").append(String.format("%.2f", pizzaInfo.getTotalIncome()))
                    .append("\n\n");

            totalIncome += pizzaInfo.getTotalIncome();
        }

        pizzasText.append("Total Income: $").append(String.format("%.2f", totalIncome)).append("\n");

        textViewPizzasRight.setText(pizzasText.toString());
    }
}
