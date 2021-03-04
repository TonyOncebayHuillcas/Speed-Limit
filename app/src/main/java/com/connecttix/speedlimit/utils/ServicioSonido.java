package com.connecttix.speedlimit.utils;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.connecttix.speedlimit.R;

public class ServicioSonido extends Service {

    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Servicio Creado", Toast.LENGTH_LONG).show();
        player = MediaPlayer.create(this, R.raw.velocidad);
        player.setLooping(true);
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Servicio Iniciado", Toast.LENGTH_LONG).show();
        player.start();

        //player.loop(Clip.LOOP_CONTINUOUSLY);
        //super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Detenido", Toast.LENGTH_LONG).show();
        player.stop();
        super.onDestroy();
    }
}
