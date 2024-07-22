package com.example.a1201418_1200435_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textView = view.findViewById(R.id.restaurent_history);

        textView.setText("Since 1990...\nFrom Italy to Palestine\nExplore a world of delicious \npizzas, crafted with fresh \ningredients and love");

        return view;

    }
}