package com.example.eddie.uber_school;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eddie.uber_school.Objetos.References;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Principal extends AppCompatActivity{

    ListView Lista;
    ArrayList<String> Arreglo =new ArrayList<>();

    private FirebaseAuth.AuthStateListener mAuth;
    FirebaseAuth esta;

    private Button mout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        final Query myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        final Query query = myRef.orderByChild("Activo").equalTo(1);

        mout = (Button) findViewById(R.id.logout);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Arreglo);
        final ListView Lista = findViewById(R.id.ListView);
        Lista.setAdapter(adapter);
        //Lista.setOnItemClickListener(this);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String name = dataSnapshot.child("Alumno").getValue().toString();
                Log.d("fbefje", name);
                Arreglo.add(name);
                adapter.notifyDataSetChanged();
                Lista.setAdapter(adapter);
                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final String referencia = (String) Lista.getItemAtPosition(position).toString();
                        Intent intentextras = new Intent(Principal.this, MapsActivity.class);
                        intentextras.putExtra("NOMBRE", referencia);
                        Log.d("ererf", referencia);
                        startActivity(intentextras);
                    }});
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setUpFirebaseListener();

        mout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    private void setUpFirebaseListener(){
        mAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                }else{
                    Toast.makeText(getApplicationContext(), "cerrando sesion", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    public void onStart(){
        super.onStart();
            FirebaseAuth.getInstance().addAuthStateListener(mAuth);
    }

    @Override
    protected void onStop() {
        super.onStop();
            if (mAuth != null){
                FirebaseAuth.getInstance().removeAuthStateListener(mAuth);
            }
    }
}