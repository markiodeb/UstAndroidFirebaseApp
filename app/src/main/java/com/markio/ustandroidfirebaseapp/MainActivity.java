package com.markio.ustandroidfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ArrayList<Contactos> listaContactos;
    ListView listViewContactos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewContactos = findViewById(R.id.lvListaContactos);
        listaContactos = new ArrayList<Contactos>();
        inicializarFirebase();
        mostrarContactos();
    }

    private void mostrarContactos() {
        databaseReference.child("Contactos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaContactos.clear();
                for (DataSnapshot objetcSnapshot: snapshot.getChildren()
                     ) {
                    Contactos c = objetcSnapshot.getValue(Contactos.class);
                    listaContactos.add(c);
                }
                listViewContactos.setAdapter(new ArrayAdapter<Contactos>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        listaContactos
                ));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void agregarContacto(View view){
        EditText etNombre = findViewById(R.id.etName);
        EditText etDireccion = findViewById(R.id.etDireccion);
        EditText etCorreo = findViewById(R.id.etCorreo);
        Contactos c = new Contactos();
        c.setUUID(UUID.randomUUID().toString());
        c.setNombre(etNombre.getText().toString());
        c.setDireccion(etDireccion.getText().toString());
        c.setCorreo(etCorreo.getText().toString());
        databaseReference.child("Contactos").child(c.getUUID()).setValue(c);
        etNombre.setText("");
        etDireccion.setText("");
        etCorreo.setText("");
        Toast.makeText(this, "Nuevo Contacto Agregado", Toast.LENGTH_SHORT).show();
    }
}