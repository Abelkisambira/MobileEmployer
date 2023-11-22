package com.innovation.mobileemployer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Chats extends Fragment {
    Button next;

    public Chats() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chats, container, false);
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);
        next=rootView.findViewById(R.id.ch);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),Chat.class);
                startActivity(intent);
            }
        });

        return rootView;

    }

}