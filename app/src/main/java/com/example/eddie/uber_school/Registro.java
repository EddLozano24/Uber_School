package com.example.eddie.uber_school;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {

    //definiendo objetos
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        String email  = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(!email.isEmpty() && !password.isEmpty()){
            progressDialog.setMessage("Registrando...");
            progressDialog.show();

            //creating a new user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if(task.isSuccessful()){
                                //display some message here
                                Toast.makeText(getApplicationContext(), "Registro Completo",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                //display some message here
                                Toast.makeText(getApplicationContext(), "La contraseña debe tener minimo 6 caracteres",Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "Faltan de Completar Datos",Toast.LENGTH_LONG).show();
        }
    }
}
