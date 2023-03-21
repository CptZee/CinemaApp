package com.github.cptzee.cinemaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    Button btn_log_in, btn_sign_up;
    private int button_flag;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btn_log_in = view.findViewById(R.id.btn_log_in);
        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_flag = 1;
                Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                intent.putExtra("Value", button_flag);
                startActivity(intent);


            }
        });

        btn_sign_up = view.findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_flag = 2;
                Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                intent.putExtra("Value", button_flag);
                startActivity(intent);

            }
        });

        return view;
    }
}