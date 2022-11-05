package com.example.turisteo.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turisteo.R;
import com.example.turisteo.home.MainActivity;
import com.example.turisteo.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText textField_email, textField_password;
    MaterialButton btn_register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textField_email = findViewById(R.id.textField_email);
        textField_password = findViewById(R.id.textField_password);
        btn_register = findViewById(R.id.btn_register);
        TextView tv_goToLogin = findViewById(R.id.tv_goToLogin);

        // Boton para registrar usuario
        btn_register.setOnClickListener(v -> {
            validateData();
        });

        // Con esta instancia de FirebaseAuth podemos llamar a los metodos que tiene para registar usuarios
        mAuth = FirebaseAuth.getInstance();

        // Si ya tiene una cuenta puede ir a la pantalla de login
        tv_goToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //finish();
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

        // Si paso las validaciones, llamo al metodo para registrar al usuario
        register(email, password);
    }

    // Metodo para registar el usuario en Firebase luego de las validaciones
    public void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {     // luego de intentar realizar el registro entra a este metodo
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){    // si se registro con exito
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{      // si hubo un error y no se pudo registrar el usuario
                            Toast.makeText(RegisterActivity.this, "Ocurrió un fallo al registrarse", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}