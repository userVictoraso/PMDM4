package com.example.victoraso.pmdm4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.victoraso.pmdm4.EjercicioDos.GaleriaActivity;
import com.example.victoraso.pmdm4.EjercicioUno.MalagaActivity;
import com.example.victoraso.pmdm4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        activityOne();
        activityTwo();
    }

    public void activityTwo(){
        binding.buttonA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GaleriaActivity.class));
            }
        });
    }

    public void activityOne(){
        binding.buttonA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MalagaActivity.class));
            }
        });
    }
}