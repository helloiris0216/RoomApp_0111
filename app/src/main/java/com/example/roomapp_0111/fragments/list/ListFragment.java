package com.example.roomapp_0111.fragments.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.roomapp_0111.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static androidx.navigation.fragment.NavHostFragment.findNavController;


public class ListFragment extends Fragment {


    private FloatingActionButton btn_actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        btn_actionBar = (FloatingActionButton) view.findViewById(R.id.btn_floatingAction_bar);
        btn_actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_listFragment_to_addFragment);
            }
        });

        return view;
    }

}