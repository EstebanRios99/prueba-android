package com.example.agenda_firebase_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AgendaActivity extends AppCompatActivity {

    TextView emailv, providerv, mensajes;
    EditText mensaje;
    Button logout, send;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        emailv = findViewById(R.id.view_email);
        providerv = findViewById(R.id.view_proveedor);
        mensaje = findViewById(R.id.mensaje);
        mensajes = findViewById(R.id.mensajes);
        //Setup
        Bundle bundle = getIntent().getExtras();
        String correo = bundle.getString("email");
        String prov = "Esta es una cuenta creada solo con usuario y contraseña";
        setup(correo, prov);

        send = findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChatMessage(mensaje.getText().toString(), correo);
            }
        });

        logout = findViewById(R.id.btn_logOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });


    }

    private void setup(String email, String provider){
        setTitle("Inicio");
        emailv.setText(email);
        providerv.setText(provider);
        getMessage();
    }

    private void addChatMessage(String msg, String email){
        Map<String, Object> message = new HashMap<>();
        message.put("msg", msg);
        message.put("from", email);

        db.collection("messages")
                .add(message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mensaje.setText("");
                    }
                });
    }

    private void getMessage(){
        db.collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> msg = document.getData();
                                Object msn = msg.get("msg");
                                mensajes.setText(msn.toString());
                                mensajes.setText(msn.toString());
                                mensajes.setText(msn.toString());
                            }
                        }
                    }});
    }
}