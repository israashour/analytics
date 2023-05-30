package com.example.analytics;

import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analytics.adapters.NoteAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private FirebaseAnalytics firebaseAnalytics;
    private RecyclerView recyclerViewNotes;
    private NoteAdapter noteAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Calendar calendar = Calendar.getInstance();

    int hour = calendar.get(Calendar.HOUR);
    int min = calendar.get(Calendar.MINUTE);
    int sec = calendar.get(Calendar.SECOND);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(getNotes());
        recyclerViewNotes.setAdapter(noteAdapter);

        // Track screen view
        trackScreenView("CategoryActivity");
    }

    private void trackScreenView(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getSimpleName());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    private List<String> getNotes() {

        List<String> notes = new ArrayList<>();
        notes.add("Note 1");
        notes.add("Note 2");
        notes.add("Note 3");
        return notes;
    }

    protected void onPause() {
        Calendar calendar = Calendar.getInstance();
        int hour2 = calendar.get(Calendar.HOUR);
        int min2 = calendar.get(Calendar.MINUTE);
        int sec2 = calendar.get(Calendar.SECOND);

        int h = hour2 - hour;
        int m = min2 - min;
        int s = sec2 - sec;

        HashMap<String,Object> users = new HashMap<>();
        users.put("name","NoteDetailsScreen");
        users.put("hours",h);
        users.put("minute",m);
        users.put("seconds",s);
        db.collection("tracking")
                .add(users)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        super.onPause();
    }
}
