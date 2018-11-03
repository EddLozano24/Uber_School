package com.example.eddie.uber_school;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_sesion;
    TextView tv_registrar;
    EditText editTextEmail;
    EditText editTextPassword;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;
    private Typeface Ubuntu;
    private Typeface UbuntuBold;
    private TextView titulo;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String fuente1 = "fonts/Ubuntu-Regular.ttf";
        String fuente2 = "fonts/Ubuntu-Bold.ttf";
        this.UbuntuBold = Typeface.createFromAsset(getAssets(), fuente2);
        this.Ubuntu = Typeface.createFromAsset(getAssets(), fuente1);
        titulo = (TextView) findViewById(R.id.titulo);
        titulo.setTypeface(UbuntuBold);
        tv_registrar = (TextView) findViewById(R.id.tv_registrar);
        btn_sesion = (Button) findViewById(R.id.btn_sesion);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        btn_sesion.setOnClickListener(this);

        tv_registrar.setOnClickListener(this);
    }

    private void loginuser() {

        String email  = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(!email.isEmpty() && !password.isEmpty()){
            progressDialog.setMessage("Iniciando sesion");
            progressDialog.show();

            //creating a new user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if(task.isSuccessful()){
                                //display some message here
                                Toast.makeText(getApplicationContext(), "Correcto",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, Principal.class));
                                finish();
                            }else{
                                //display some message here
                                Toast.makeText(getApplicationContext(), "Incorrecto",Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "Faltan de Completar Datos",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == btn_sesion){
            loginuser();
        }else{
            startActivity(new Intent(MainActivity.this, Registro.class));
        }
    }
}