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
import com.example.turisteo.home.HomeActivity;
import com.example.turisteo.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    TextView tvSplashScreen;
    ImageView imgLogo;

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

        tvSplashScreen = findViewById(R.id.tvSplashScreen);
        imgLogo = findViewById(R.id.imgLogo);
        tvSplashScreen.setAnimation(animationTextSplash);
        imgLogo.setAnimation(animationLogo);

        // Tiempo despues del cual el Splass Screen desaparece y se inicia el nuevo activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Obtengo si hay un usuario con sesion iniciada (con email y contraseña)
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Y para la parte de login con gmail se usa la siguiente linea, y nos va a decir si el usuario ya inicio sesion
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SplashScreenActivity.this);

                // Si hay un usuario con sesion iniciada abro la pantalla de Home luego del Splash Screen.
                // Compruebo si se inicio sesion con email y contraseña (user) o si se inicio sesion con gmail (account)
                if(user != null && account != null){
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else{     // si no hay una cuenta iniciada abro la pantalla de login luego del Splash Screen
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        }, 4000);   // luego de 4 segundos se abre la pantalla de login
    }
}