package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) { //画面生成時
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavController navcontroller = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(navigation, navcontroller);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, PeformanceActivity.class);
        startActivity(intent);
        return true;
    }
}
