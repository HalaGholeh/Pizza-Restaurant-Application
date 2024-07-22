package com.example.a1201418_1200435_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private ArrayList<Pizza> pizzaList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pizza pizza);
    }

    public PizzaAdapter(ArrayList<Pizza> pizzaList, OnItemClickListener listener) {
        this.pizzaList = pizzaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pizza_card, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        holder.bind(pizza, listener);
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    public static class PizzaViewHolder extends RecyclerView.ViewHolder {
        private TextView pizzaNameTextView;
        private ImageView pizzaImage;

        public PizzaViewHolder(View itemView) {
            super(itemView);
            pizzaNameTextView = itemView.findViewById(R.id.pizza_name);

        }

        public void bind(final Pizza pizza, final OnItemClickListener listener) {
             pizzaNameTextView.setText(pizza.getName());

            itemView.setOnClickListener(v -> listener.onItemClick(pizza));
        }
    }
}
