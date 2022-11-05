package com.example.turisteo.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turisteo.MainActivity;
import com.example.turisteo.R;
import com.example.turisteo.home.HomeActivity;
import com.example.turisteo.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    TextView tv_goToRegister;
    TextInputEditText textField_email;
    TextInputEditText textField_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textField_email = findViewById(R.id.textField_email);
        textField_password = findViewById(R.id.textField_password);
        btn_login = findViewById(R.id.btn_login);
        tv_goToRegister = findViewById(R.id.tv_goToRegister);

        mAuth = FirebaseAuth.getInstance();

        // Boton para iniciar sesion
        btn_login.setOnClickListener(v -> {
            validateData();     // valido los datos ingresados
        });

        // Si no tiene una cuenta
        tv_goToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    // Metodo para validar el correo y contraseña que ingreso el usuario
    public void validateData(){
        String email = textField_email.getText().toString().trim();      // trim() para quitar espacios no deseados
        String password = textField_password.getText().toString().trim();

        // Android trae esta libreria "Patterns" que nos permite hacer la validacion de si es o no un email valido
        // y para validar que una contraseña por ejemplo incluya numeros como hice en este caso.

        // A este "if" ingresa si dejo el campo vacio o si no es un correo valido
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textField_email.setError("Debe ingresar un correo valido");
            return;
        } else {
            textField_email.setError(null);
        }

        // Validacion del password (veo si el campo esta vacio o si tiene menos de 6 caracteres)
        if(password.isEmpty() || password.length() < 6){
            textField_password.setError("La contraseña debe tener 6 caracteres como mínimo");
            return;
        } else if(!Pattern.compile("[0-9]").matcher(password).find()){
            textField_password.setError("La contraseña debe tener al menos un número");
            return;
        } else {
            textField_password.setError(null);
        }

        // Si paso las validaciones, llamo al metodo para iniciar sesion
        login(email, password);
    }

    // Metodo para iniciar sesion luego de las validaciones
    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){    // si la comprobacion con Firebase fue correcta, inicia la sesion
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }else{      // si los datos no coinciden con los usuarios registrados en Firebase
                            Toast.makeText(LoginActivity.this, "No existe un usuario con esos datos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}