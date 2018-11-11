package com.example.eddie.uber_school;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Padre extends AppCompatActivity implements View.OnClickListener{

    Button btn_sesion;
    TextView tv_registrar;
    EditText editTextEmail;
    EditText editTextPassword;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__padre);

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

                                String id_user = mAuth.getUid();
                                DatabaseReference referencia = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id_user);
                                referencia.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String tipo = dataSnapshot.child("Tipo").getValue().toString();
                                        if (tipo.equals("Padre")){
                                            Toast.makeText(getApplicationContext(), "Correcto",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Login_Padre.this, Principal_Padre.class));
                                            finish();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Tipo de Usuario Incorrecto",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
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
            startActivity(new Intent(Login_Padre.this, Registro_Padre.class));
        }
    }
}
