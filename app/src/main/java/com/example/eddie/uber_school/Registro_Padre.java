package com.example.eddie.uber_school;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro_Padre extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextName;
    Button buttonRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__padre);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.alumno);
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
        final String name = editTextName.getText().toString().trim();

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
                                String padre_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference padre_db = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(padre_id);
                                padre_db.setValue(true);
                                DatabaseReference activo = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(padre_id).child("Activo");
                                activo.setValue(0);
                                DatabaseReference lat = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(padre_id).child("Latitud");
                                lat.setValue(0);
                                DatabaseReference lon = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(padre_id).child("Longitud");
                                lon.setValue(0);
                                DatabaseReference alumno = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(padre_id).child("Alumno");
                                alumno.setValue(name);
                                DatabaseReference tipo = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(padre_id).child("Tipo");
                                tipo.setValue("Padre");
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
