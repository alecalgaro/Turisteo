package com.example.turisteo.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.example.turisteo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    MaterialButton btn_forgotPassword;
    TextInputEditText textField_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btn_forgotPassword = findViewById(R.id.btn_forgotPassword);
        textField_email = findViewById(R.id.textField_email);

        btn_forgotPassword.setOnClickListener(v -> {
            validateData();
        });
    }

    // Metodo para validar que el correo ingresado sea valido
    public void validateData(){
        String email = textField_email.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textField_email.setError("Ingrese un correo valido");
            return;
        }else{
            sendEmail(email);   // si el correo es valido llamo al metodo sendEmail
        }
    }

    // Con este metodo definimos que hacer si el usuario presiona la flecha de volver hacia atrás
    // En este caso lo envio a la pantalla de Login
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    // Metodo para enviar el correo de recuperación de contraseña al email ingresado
    public void sendEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){       // si es exitoso
                            Toast.makeText(ForgotPasswordActivity.this, "Correo enviado", Toast.LENGTH_LONG).show();
                            // Abro de nuevo el Login para que puede volver a ingresar la contraseña una vez recuperada
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {    // si no fue exitosa la recuperacion de la contraseña por error con el correo
                            Toast.makeText(ForgotPasswordActivity.this, "Correo invalido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}