package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private boolean keikokuflag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //画面生成時
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavController navcontroller = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(navigation, navcontroller);
    }
}
