package com.example.victoraso.pmdm4.EjercicioDos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.victoraso.pmdm4.databinding.ActivityGaleriaBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

        intent = new Intent(GaleriaActivity.this, DownloadService.class);
        startService(intent);
    }

    private void showImages() {
        String[] lines = getUrlImages().split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++) {
            showMessage(lines[i]);
            setImageGlide(lines[i]);
        }
    }

    private void setImageGlide(String url) {
        Glide.with(getApplicationContext()).load(url)
                //.transition(DrawableTransitionOptions.withCrossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        writeFileOnInternalStorage(url, e.toString());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageViewAnimation);
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

    /**
     * FILE
     **/

    public void writeFileOnInternalStorage(String url, String error){
        File dir = new File(getApplicationContext().getFilesDir(), "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, "errores.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append("Enlace " + url + "; Error " + error + "; "  + System.currentTimeMillis());
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  public void writeFileOnInternalStorage(String url, String error) throws IOException {
     *         if (!getFile().exists()) {
     *             try {
     *                 getFile().createNewFile();
     *             } catch (IOException e) {
     *                 e.printStackTrace();
     *             }
     *         }
     *         try {
     *             FileOutputStream fOut = new FileOutputStream(getFile(), true);
     *             OutputStreamWriter outputWriter = new OutputStreamWriter(fOut);
     *             outputWriter.append("876878678678Enlace " + url + "; Error " + error + "; "  + System.currentTimeMillis());
     *             outputWriter.close();
     *
     *             showMessage("Error añadido a " + FILE_NAME);
     *         } catch (Exception e) {
     *             e.printStackTrace();
     *         }
     *     }
     *
     *     public File getDirectory() {
     *         File directory = new File(this.getFilesDir() + File.separator + "/MyFolder");
     *         if (!directory.exists())
     *             directory.mkdir();
     *         return directory;
     *     }
     *
     *     public File getFile() {
     *         File newFile = new File(getDirectory(), FILE_NAME);
     *         return newFile;
     *     }**/
}