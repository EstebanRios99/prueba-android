package com.example.agenda_firebase_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tarea.Tarea;


public class AgendaActivity extends AppCompatActivity {

    private List<Tarea> tareaList= new ArrayList<Tarea>();
    ArrayAdapter<Tarea> arrayTarea;


    EditText task, date, time;
    Button logout, send, update, delete;
    TextView user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView tareas;
    Tarea taskSelected;


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

        getTasks(correo);

        tareas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskSelected = (Tarea) parent.getItemAtPosition(position);
                task.setText(taskSelected.getTask());
                date.setText(taskSelected.getDate());
                time.setText(taskSelected.getTime());
            }
        });

        update = findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask(correo);
            }
        });

        delete = findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
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

    private void setup(String email){
        setTitle("Tareas");
        user.setText(email);

    }

    private void addTask(String addTask, String email, String addDate, String addTime){
        Tarea t = new Tarea();
        t.setUid(UUID.randomUUID().toString());
        t.setTask(addTask);
        t.setFrom(email);
        t.setDate(addDate);
        t.setTime(addTime);

        db.collection("tasks")
            .add(t)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                   task.setText("");
                   date.setText("");
                   time.setText("");
                }
            });
    }

    private  void updateTask (String email){
        String newID = taskSelected.getUid();

        db.collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot document: value.getDocuments()){
                            Tarea t = document.toObject(Tarea.class);
                            if(newID.equals(t.getUid())){
                                Tarea t1 = new Tarea();
                                t1.setUid(taskSelected.getUid());
                                t1.setTask(task.getText().toString().trim());
                                t1.setDate(date.getText().toString().trim());
                                t1.setTime(time.getText().toString().trim());
                                t1.setFrom(email);

                                db.collection("tasks").document(document.getId()).update("date", t1.getDate());
                                db.collection("tasks").document(document.getId()).update("from", t1.getFrom());
                                db.collection("tasks").document(document.getId()).update("task", t1.getTask());
                                db.collection("tasks").document(document.getId()).update("time", t1.getTime());
                                db.collection("tasks").document(document.getId()).update("uid", t1.getUid());
                            }
                        }
                    }
                });
    }

    private void deleteTask(){
        String newID = taskSelected.getUid();

        db.collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot document: value.getDocuments()){
                            Tarea t = document.toObject(Tarea.class);
                            if(newID.equals(t.getUid())){
                                db.collection("tasks").document(document.getId()).delete();
                            }
                        }
                    }
                });

    }

    private void getTasks(String email){
        db.collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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