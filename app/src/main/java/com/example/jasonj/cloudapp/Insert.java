package com.example.jasonj.cloudapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Jason J on 6/9/2017.
 */

public class Insert extends AppCompatActivity {
    EditText nm;
    EditText age;
    EditText mail;
    EditText bloodType;
    Button sv;
    private static final String TAG = null;


    FirebaseDatabase myDb = FirebaseDatabase.getInstance();
    DatabaseReference ref = myDb.getReference("name");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ins);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        nm = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        mail = (EditText) findViewById(R.id.email);
        bloodType = (EditText) findViewById(R.id.blood);
        sv = (Button) findViewById(R.id.save);
        sendData();
    }

    public void sendData() {
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String name = nm.getText().toString();
                String userAge = age.getText().toString();
                //input validations
                if (!isEmailValid(email)) {
                    mail.setError("Invalid email");
                    Log.e(TAG, "Invalid email");

                } else if (isNameValid(name)) {
                    nm.setError("Invalid name");
                    Log.e(TAG, "Invalid name");

                } else if (!isAgeValid(userAge)) {
                    age.setError("Invalid age");
                    Log.e(TAG, "Invalid age");

                } else {
                    ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                    dataSnapshot.getChildren().iterator().hasNext()) {
                                //If the email exists
                                Toast.makeText(Insert.this, "This user exists.", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "This user exists");
                            } else {
                                //if the email does not exist
                                String name = nm.getText().toString();
                                String userAge = age.getText().toString();
                                String userEmail = mail.getText().toString();
                                String userType = bloodType.getText().toString();

                                //writing to the database
                                String nameKey = ref.push().getKey();
                                Users user = new Users(name, userAge, userEmail, userType);
                                ref.child(nameKey).setValue(user);

                                Toast.makeText(Insert.this, "Successfully saved", Toast.LENGTH_LONG).show();
                                Log.i(TAG, "Successfully saved");
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.exit(0);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }

    //validations
    public boolean isEmailValid(String email) {
        String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pat = Pattern.compile(email_pattern);
        Matcher match = pat.matcher(email);
        return match.matches();
    }

    public boolean isNameValid(String name) {
        Pattern pat = Pattern.compile("([0-9]+)");
        Matcher match = pat.matcher(name);
        boolean found;
        if (match.find()) {
            found = true;
        } else {
            found = false;
        }
        System.out.println(found);
        return found;

    }

    public boolean isAgeValid(String age) {
        try {
            Integer.parseInt(age);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
