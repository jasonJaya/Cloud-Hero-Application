package com.example.jasonj.cloudapp;

import android.os.Bundle;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jason J on 6/20/2017.
 */

public class Update extends AppCompatActivity {
    EditText name;
    EditText age;
    EditText email;
    EditText bloodType;
    Button update;
    private static final String TAG = null;

    FirebaseDatabase myDb = FirebaseDatabase.getInstance();
    DatabaseReference ref = myDb.getReference("name");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ins);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email);
        bloodType = (EditText) findViewById(R.id.blood);
        update = (Button) findViewById(R.id.save);

        updateData();
    }

    public void updateData() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String userName = name.getText().toString();
                String userAge = age.getText().toString();
                //input validations
                if (!isEmailValid(mail)) {
                    email.setError("Invalid email");
                    Log.e(TAG, "Invalid email");

                } else if (isNameValid(userName)) {
                    name.setError("Invalid name");
                    Log.e(TAG, "Invalid name");

                } else if (!isAgeValid(userAge)) {
                    age.setError("Invalid age");
                    Log.e(TAG, "Invalid age");

                } else {

                    ref.orderByChild("email").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                    dataSnapshot.getChildren().iterator().hasNext()) {


                                String newAge = age.getText().toString();
                                String newName = name.getText().toString();
                                String newBlood = bloodType.getText().toString();
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String key = childSnapshot.getKey();
                                    System.out.println(key);

                                    //updating the database
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("name");
                                    ref.child(key).child("name").setValue(newName);
                                    ref.child(key).child("age").setValue(newAge);
                                    ref.child(key).child("blood").setValue(newBlood);
                                    Toast.makeText(Update.this, "Successfully updated.", Toast.LENGTH_LONG).show();
                                    Log.i(TAG, "Successfully updated");
                                }

                            } else {

                                Toast.makeText(Update.this, "Invalid Email Address.", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Invalid email address");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Update.this, "Email does not exist.", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Email does not exist");
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