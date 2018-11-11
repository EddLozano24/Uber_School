package com.example.eddie.uber_school;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Principal_Padre extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private Button btn;
    private Button activar;
    private Button desactivar;
    private Button sesion;
    private FirebaseAuth.AuthStateListener mout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal__padre);

        sesion = (Button) findViewById(R.id.cerrar_sesion);
        btn = (Button) findViewById(R.id.coordenadas);
        activar = (Button) findViewById(R.id.activar);
        desactivar = (Button) findViewById(R.id.desactivar);
        mAuth = FirebaseAuth.getInstance();
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);

        setUpFirebaseListener();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Principal_Padre.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(Principal_Padre.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            String id_user = mAuth.getUid();
                            DatabaseReference lat = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id_user).child("Latitud");
                            lat.setValue(location.getLatitude());
                            DatabaseReference lon = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id_user).child("Longitud");
                            lon.setValue(location.getLongitude());
                            Toast.makeText(Principal_Padre.this, "Ubicacion Actualizada", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Principal_Padre.this, "No se pudo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        activar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_user = mAuth.getUid();
                DatabaseReference act = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id_user).child("Activo");
                act.setValue(1);
            }
        });

        desactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_user = mAuth.getUid();
                DatabaseReference act = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id_user).child("Activo");
                act.setValue(0);
            }
        });

        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void setUpFirebaseListener() {
        mout = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                    Toast.makeText(getApplicationContext(), "cerrando sesion", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login_Padre.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    public void onStart(){
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mout);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mout != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mout);
        }
    }

}
