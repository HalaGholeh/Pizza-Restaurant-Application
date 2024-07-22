package com.example.a1201418_1200435_project;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class FindUsFragment extends Fragment {


    private TransitionDrawable transitionDrawable;
    private Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_us, container, false);

        ImageView imageView = view.findViewById(R.id.imageViewfindUs);
        Button callButton = view.findViewById(R.id.button_call_restaurant);
        Button mapsButton = view.findViewById(R.id.button_open_maps);
        Button emailButton = view.findViewById(R.id.button_send_email);

        transitionDrawable = (TransitionDrawable) imageView.getDrawable();
        transitionDrawable.setCrossFadeEnabled(true);
        transitionDrawable.startTransition(1000);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transitionDrawable.startTransition(1000);
                handler.postDelayed(this, 2000);
            }
        }, 2000);

        callButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:0599000000"));
            startActivity(callIntent);
        });

        mapsButton.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:31.961013,35.190483?q=31.961013,35.190483(Advance Pizza)");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        });

        emailButton.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "AdvancePizza@Pizza.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Advance Pizza,");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        return view;
    }

}