package com.example.victoraso.pmdm4.EjercicioDos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadService extends Service {
    private final String urlString = "https://victorasou.xyz/imagenes.txt";
    private GaleriaActivity galeriaActivity = new GaleriaActivity();

    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mostrarMensaje("Creando el servicio");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        URL url = null;
        try {
            url = new URL(urlString);
            descargaOkHTTP(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mostrarMensaje("Error en la URL: " + urlString);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mostrarMensaje("Servicio destruido");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void descargaOkHTTP(URL web) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(web).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                galeriaActivity.writeFileOnInternalStorage(urlString, e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.isSuccessful()) {
                        String myResponse = response.body().string();
                        enviarRespuesta(myResponse);
                    }
                }
            }
        });
    }

    private void enviarRespuesta (String mensaje) {
        Intent i = new Intent();
        i.setAction(GaleriaActivity.ACTION_RESP);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.putExtra("response", mensaje);
        sendBroadcast(i);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
