package com.example.agenda_firebase_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.DataCollectionDefaultChange;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tarea.Tarea;


public class AgendaActivity extends AppCompatActivity {

    private List<Tarea> tareaList= new ArrayList<Tarea>();
    ArrayAdapter<Tarea> arrayTarea;

    EditText task, date, time;
    Button logout, send;
    TextView user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView tareas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        task = findViewById(R.id.text_task);
        date = findViewById(R.id.text_date);
        time = findViewById(R.id.text_time);
        tareas = findViewById(R.id.list_tasks);
        user = findViewById(R.id.text_user);

        //Setup
        Bundle bundle = getIntent().getExtras();
        String correo = bundle.getString("email");
        setup(correo);

        send = findViewById(R.id.btn_send_task);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(
                        task.getText().toString(),
                        correo,
                        date.getText().toString(),
                        time.getText().toString()
                );
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

        getTasks(correo);


    }

    private void setup(String email){
        setTitle("Tareas");
        user.setText(email);

    }

    private void addTask(String addTask, String email, String addDate, String addTime){
        Map<String, Object> tasks = new HashMap<>();
        tasks.put("from", email);
        tasks.put("task", addTask);
        tasks.put("date", addDate);
        tasks.put("time", addTime);

        db.collection("tasks")
                .add(tasks)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                       task.setText("");
                       date.setText("");
                       time.setText("");
                    }
                });
    }

    private void getTasks(String email){
        db.collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        tareaList.clear();
                        for (DocumentSnapshot document: value.getDocuments()){
                            Tarea t = document.toObject(Tarea.class);
                            if (email.equals(t.getFrom())){
                                tareaList.add(t);
                            }
                            arrayTarea = new ArrayAdapter<Tarea>(AgendaActivity.this, android.R.layout.simple_list_item_1, tareaList);
                            tareas.setAdapter(arrayTarea);
                        }
                    }
                });

    }

}