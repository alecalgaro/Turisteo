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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    TextView tv_goToRegister;

    // Para login con Firebase usando email y password
    TextInputEditText textField_email;
    TextInputEditText textField_password;
    private FirebaseAuth mAuth;

    // Para login usando Google
    SignInButton btn_loginGoogle;
    GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textField_email = findViewById(R.id.textField_email);
        textField_password = findViewById(R.id.textField_password);
        btn_login = findViewById(R.id.btn_login);
        tv_goToRegister = findViewById(R.id.tv_goToRegister);
        btn_loginGoogle = findViewById(R.id.btn_loginGoogle);

        mAuth = FirebaseAuth.getInstance();

        // Boton para iniciar sesion con email y password
        btn_login.setOnClickListener(v -> {
            validateData();     // valido los datos ingresados
        });

        // Boton para iniciar sesion con Google
        btn_loginGoogle.setOnClickListener(v -> {
            signInWithGoogle();
        });

        // Configuracion para el metodo de login con Google (le pasamos el token que definimos en el archivo "strings")
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

    // Metodo para iniciar sesion con Google:
    public void signInWithGoogle(){
        // Se llama a un activity nuevo que es la ventana que se abre siempre al presionar el boton de login con Google para elegir la cuenta
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Este metodo se sobreescribe porque es parte tambien del login con Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWthGoogle(account.getIdToken());       // si inicia sesion correctamente nos va a devolver ese token
            } catch (ApiException e){
                Toast.makeText(LoginActivity.this, "Hubo un fallo al iniciar sesion con Google", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Llamamos este metodo cuando se inicia sesion correctamente con Google y se recibe el token que nos devuelve
    private void firebaseAuthWthGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)      // le pasamos la credencial
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){    // si es exitoso el inicio de sesion
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }else{      // si los datos no coinciden con los usuarios registrados en Firebase
                            Toast.makeText(LoginActivity.this, "Hubo un fallo en el inicio de sesion", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}