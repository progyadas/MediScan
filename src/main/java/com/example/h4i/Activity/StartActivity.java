package com.example.h4i.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.h4i.Fragments.DoctorFragment;
import com.example.h4i.Fragments.HomeFragment;
import com.example.h4i.Fragments.HospitalFragment;
import com.example.h4i.Fragments.ReminderFragment;
import com.example.h4i.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new DoctorFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.home);
        setSupportActionBar(toolbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.doctor:
                        fragment = new DoctorFragment();
                        setTitle("Top Doctors");
                        break;
                    case R.id.home:
                        fragment = new DoctorFragment();
                        setTitle("Top Doctors");
                        break;
                    case R.id.hospital:
                        fragment = new HospitalFragment();
                        setTitle("Hospitals Near Me");
                        break;
                    case R.id.notification:
                        fragment = new ReminderFragment();
                        setTitle("My Priorities");
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            //Profile Page
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void UploadActivity(View view) {
        startActivity(new Intent(StartActivity.this, UploadActivity.class));
    }
}