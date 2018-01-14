package com.example.pedro.mediaplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    private static final String videoAgua = "https://static.videezy.com/system/resources/previews/000/002/448/original/slow-motion-drop-hd-stock-video.mp4";
    private static final String videoGlobo = "https://static.videezy.com/system/resources/previews/000/005/680/original/342572022.mp4";
    private static final String videoParque = "http://mazwai.com/system/posts/videos/000/000/223/original/lodewijk-van-eekhout_wiffling-grayleg-goose.mp4?1476537792";
    private static final int PETICION_ACCESO_INTERNET = 1;
    String videoElegido = "";
    SurfaceView sv;
    MediaPlayer mp;
    TextView tvDuracion, tvAncho, tvAlto;
    boolean permisoInternet = false;
    View controlPanel, progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.bPlay)).setOnClickListener(this);
        (findViewById(R.id.bPause)).setOnClickListener(this);
        (findViewById(R.id.bStop)).setOnClickListener(this);
        (findViewById(R.id.bBack)).setOnClickListener(this);
        (findViewById(R.id.bForward)).setOnClickListener(this);
        (findViewById(R.id.bUp)).setOnClickListener(this);
        (findViewById(R.id.bDown)).setOnClickListener(this);

        sv = findViewById(R.id.surfaceView);
        tvDuracion = findViewById(R.id.duration);
        tvAncho = findViewById(R.id.anchor);
        tvAlto = findViewById(R.id.height);
        controlPanel = findViewById(R.id.controlPanel);
        progressBar = findViewById(R.id.progressBar_overlay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bPlay:
                playVideo();
                break;
            case R.id.bPause:
                pauseVideo();
                break;
            case R.id.bStop:
                stopVideo();
                break;
            case R.id.bBack:
                backVideo();
                break;
            case R.id.bForward:
                forwardVideo();
                break;
            case R.id.bUp:
                volumeUp();
                break;
            case R.id.bDown:
                volumeDown();
                break;
        }
    }

    private void volumeDown() {
    }

    private void volumeUp() {
    }

    private void forwardVideo() {
    }

    private void backVideo() {
    }

    private void pauseVideo() {
        mp.pause();
    }

    private void playVideo() {
        if(mp!=null){
            mp.start();
        }
    }

    private void stopVideo() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }


    public void selectVideo(View v) {
        switch (v.getId()) {
            case R.id.bPlayAgua:
                videoElegido = videoAgua;
                break;
            case R.id.bPlayGlobo:
                videoElegido = videoGlobo;
                break;
            case R.id.bPlayParque:
                videoElegido = videoParque;
                break;
        }
        pedirPermiso();
        if (!permisoInternet) {
            return;
        }
        cargarVideo();
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        int ancho = mp.getVideoWidth();
        int alto = mp.getVideoHeight();
        int duracion = mp.getDuration();
        tvAncho.setText(String.valueOf(ancho));
        tvAlto.setText(String.valueOf(alto));
        tvDuracion.setText(String.valueOf(duracion));
        sv.getHolder().setFixedSize(ancho, alto);
        controlPanel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PETICION_ACCESO_INTERNET:
                if (grantResults.length > 0) {
                    permisoInternet = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    cargarVideo();
                }
        }
    }

    private void cargarVideo() {
        if (mp == null) {
            mp = new MediaPlayer();
            try {
                mp.setDataSource(this, Uri.parse(videoElegido));
                mp.setOnPreparedListener(this);
                mp.setDisplay(sv.getHolder());
                mp.prepareAsync();
                progressBar.setVisibility(View.VISIBLE);
            } catch (IOException e) {

            }
        } else {
            stopVideo();
            cargarVideo();
        }
    }

    public void pedirPermiso() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PETICION_ACCESO_INTERNET);
        }else{
            permisoInternet = true;
        }
    }


}
