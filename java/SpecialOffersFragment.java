package com.example.a1201418_1200435_project;

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

public class SpecialOffersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OfferAdapter offerAdapter;
    private ArrayList<Offer> offerList;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offers, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_offers);
        databaseHelper = new DatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        offerList = new ArrayList<>();
        offerAdapter = new OfferAdapter(getContext(), offerList, databaseHelper);
        recyclerView.setAdapter(offerAdapter);

        fetchOffersFromDatabase();

        return view;
    }

    private void fetchOffersFromDatabase() {
        offerList.clear();
        ArrayList<Offer> offersFromDb = databaseHelper.getAllSpecialOffers();
        if (offersFromDb != null) {
            offerList.addAll(offersFromDb);
        }
        offerAdapter.notifyDataSetChanged();
    }
}
