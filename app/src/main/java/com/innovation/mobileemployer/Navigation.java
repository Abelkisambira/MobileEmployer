package com.innovation.mobileemployer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.innovation.mobileemployer.databinding.ActivityNavigationBinding;

public class Navigation extends AppCompatActivity {
    ActivityNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new Home());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (itemID == R.id.home) {
                replaceFragment(new Home());
            } else if (itemID==R.id.book) {
                replaceFragment(new Booking());

            } else if (itemID == R.id.chats) {
                replaceFragment(new Chats());

            } else if (itemID == R.id.account) {
                replaceFragment(new Settings());

            }
            return true;

        });
    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}