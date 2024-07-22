package com.example.a1201418_1200435_project;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class PizzaMenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private PizzaAdapter pizzaAdapter;
    private ArrayList<Pizza> pizzaList = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private static final String ARG_USER_EMAIL = "CurrentUserEmail";
    private String currentUserEmail;

    public static PizzaMenuFragment newInstance(String userEmail) {
        PizzaMenuFragment fragment = new PizzaMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserEmail = getArguments().getString(ARG_USER_EMAIL);
        }
        databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_menu, container, false);
        recyclerView = view.findViewById(R.id.recycler_View_pizza);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pizzaAdapter = new PizzaAdapter(pizzaList, new PizzaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pizza pizza) {
                PizzaDetailsFragment fragment = PizzaDetailsFragment.newInstance(pizza.getName(), pizza.getCategory(), pizza.getPrice(), pizza.getSize());
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView.setAdapter(pizzaAdapter);
        loadPizzaData();

        return view;
    }

    private void loadPizzaData() {
        pizzaList.clear();
        pizzaList.addAll(databaseHelper.getDistinctPizzaTypes());
        pizzaAdapter.notifyDataSetChanged();
    }
}
