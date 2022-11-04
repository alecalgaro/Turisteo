package com.example.turisteo.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.turisteo.R;
import com.example.turisteo.login.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Para que la pantalla de splash screen se vea en pantalla completa, sin la barra de arriba
        // (no hace falta porque ya quitamos la barra de arriba en el tema)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Animaciones:
        Animation animationLogo = AnimationUtils.loadAnimation(this, R.anim.animation_logo);
        Animation animationTextSplash = AnimationUtils.loadAnimation(this, R.anim.animation_text_splash);

        TextView tvSplashScreen = findViewById(R.id.tvSplashScreen);
        ImageView imgLogo = findViewById(R.id.imgLogo);
        tvSplashScreen.setAnimation(animationTextSplash);
        imgLogo.setAnimation(animationLogo);

        // Tiempo despues del cual el Splass Screen desaparece y se inicia el nuevo activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                // animacion para la transicion: primero la de entrada del activity que se abre y luego la de salida del activity actual:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 4000);   // luego de 4 segundos se abre la pantalla de login
    }
}