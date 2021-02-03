package com.example.agenda_firebase_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "hola" ;
    Button btn_signUp, btn_signIn;
    EditText email, password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Object AlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        btn_signUp = findViewById(R.id.btn_signUp);
        btn_signIn = findViewById(R.id.btn_signIn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        // Setup
        setup();

    }

    private void setup() {
        setTitle("Autenticación");

        btn_signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((email.getText().toString()) != "" && (password.getText().toString()) != "") {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString()
                            , password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                String correo = task.getResult().getUser().getEmail();
                                showHome(correo);
                                Map<String, Object> user = new HashMap<>();
                                user.put("uid", uid);
                                user.put("email", correo);
                                db.collection("users")
                                        .add(user);
                            } else {
                                showAlert();
                            }
                        }
                    });
                }
            }
        });

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((email.getText().toString()) != "" && (password.getText().toString()) != "") {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString()
                            , password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String correo = task.getResult().getUser().getEmail();
                                showHome(correo);
                            } else {
                                showAlert();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showAlert() {
        AlertDialog = new AlertDialog.Builder(this);
        ((AlertDialog.Builder) AlertDialog).setTitle("Error");
        ((AlertDialog.Builder) AlertDialog).setMessage("No se pudo autenticar el usuario");
        ((AlertDialog.Builder) AlertDialog).setPositiveButton("Aceptar", null);
        android.app.AlertDialog dialog = ((AlertDialog.Builder) AlertDialog).create();
        dialog.show();
    }

    private void showHome(String email) {
        Intent email1 = new Intent(this, AgendaActivity.class).putExtra("email", email);

        startActivity(email1);
    }


}