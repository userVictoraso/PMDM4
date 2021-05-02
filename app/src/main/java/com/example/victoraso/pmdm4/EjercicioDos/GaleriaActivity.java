package com.example.victoraso.pmdm4.EjercicioDos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.victoraso.pmdm4.databinding.ActivityGaleriaBinding;

public class GaleriaActivity extends AppCompatActivity {
    public static final String ACTION_RESP = "RESPUESTA_DESCARGA";

    private String urlImages;
    ActivityGaleriaBinding binding;

    Intent intent;
    IntentFilter intentFilter;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGaleriaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        intentFilter = new IntentFilter(ACTION_RESP);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastReceiver = new ReceptorOperacion();

        //LANZAR EL SERVICIO AL INICIAR LA APP
        /**IMPORTANTE AÑADIR EL SERVICIO EN EL MANIFEST*/
        intent = new Intent(GaleriaActivity.this, DownloadService.class);
        startService(intent);

    }

    private void showImages() {
        //TODO: 1.- tiempo entre una imagen y otra
        //TODO: 2.- obtener posibles errores al descargar?
        String[] lines = getUrlImages().split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++) {
            showMessage(lines[i]);
            Glide.with(getApplicationContext()).load(lines[i])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageViewAnimation);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showMessage("Servicio parado");
        stopService(intent);
    }

    public class ReceptorOperacion extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            showMessage("Enlaces obtenidos.");
            setUrlImages(response);
            showImages();
        }
    }

    private void showMessage(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private String getUrlImages() {
        return urlImages;
    }

    private void setUrlImages(String urlImages) {
        this.urlImages = urlImages;
    }
}