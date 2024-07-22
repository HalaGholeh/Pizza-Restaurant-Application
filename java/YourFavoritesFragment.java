package com.example.a1201418_1200435_project;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;


public class YourFavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private YourFavoritesAdapter adapter;
    private List<Favorite> favoritesList;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoritesList = new ArrayList<>();
        adapter = new YourFavoritesAdapter(favoritesList);
        recyclerView.setAdapter(adapter);

        databaseHelper = new DatabaseHelper(getContext());

        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);
        if (userEmail != null) {
            favoritesList.clear();
            favoritesList.addAll(databaseHelper.getUserFavorites(userEmail));
            adapter.notifyDataSetChanged();
        }
    }
}
