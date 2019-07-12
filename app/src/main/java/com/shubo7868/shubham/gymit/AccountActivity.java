package com.shubo7868.shubham.gymit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private CardView fabAdd, fabLogout;
    private ListView listView;
    private ArrayList<Exercise> exercisesList;
    private ArrayAdapter<Exercise> arrayAdapter;
    private Exercise exercise;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, exRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        fabLogout = findViewById(R.id.account_logout);
        exercise = new Exercise();
        fabAdd = findViewById(R.id.fabAddExercise);
        listView = findViewById(R.id.account_listView_exercises);
        exercisesList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<Exercise>(AccountActivity.this, R.layout.list_exercise, R.id.exerciseData, exercisesList);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        exRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        exRef = exRef.child("Exercises");

        exRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    exercise = ds.getValue(Exercise.class);
                    exercisesList.add(exercise);
                }
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // logout button click listener
        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                finish();
            }
        });

        // add exercise on click listener
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("New Exercise");
                Context context = AccountActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText et_exercise_name = new EditText(AccountActivity.this);
                et_exercise_name.setHint("Exercise Name");
                et_exercise_name.setInputType(InputType.TYPE_CLASS_TEXT);
                layout.addView(et_exercise_name);

                final EditText et_duration = new EditText(AccountActivity.this);
                et_duration.setHint("Duration");
                et_duration.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(et_duration);

                builder.setView(layout);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = et_exercise_name.getText().toString();
                        int duration = Integer.parseInt(et_duration.getText().toString());
                        final Exercise exercise = new Exercise(name, duration);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users"); // https://gymit-1b687.firebaseio.com/Users
                        databaseReference = databaseReference.child(user.getUid()).child("Exercises");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                databaseReference.push().setValue(exercise);
                                Toast.makeText(AccountActivity.this, "Exercise Added", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(AccountActivity.this, "Something went wrong, Try again!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }







}
