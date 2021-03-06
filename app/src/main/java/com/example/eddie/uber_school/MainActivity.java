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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button padre_familia;
    Button conductor;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        padre_familia = (Button) findViewById(R.id.padre_familia);
        conductor = (Button) findViewById(R.id.conductor);
        mAuth = FirebaseAuth.getInstance();

        padre_familia.setOnClickListener(this);

        conductor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == conductor){
            startActivity(new Intent(MainActivity.this, Login_Conductor.class));
        }else{
            startActivity(new Intent(MainActivity.this, Login_Padre.class));
        }
    }
}