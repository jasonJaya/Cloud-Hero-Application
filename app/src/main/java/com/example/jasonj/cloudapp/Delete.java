package com.example.jasonj.cloudapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason J on 6/14/2017.
 */

public class Delete extends AppCompatActivity {
    EditText emailId;
    Button deleteEmail;
    private static final String TAG = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);

        emailId = (EditText) findViewById(R.id.email);
        deleteEmail = (Button) findViewById(R.id.del);

        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        DatabaseReference myRef = myDb.getReference("name");


        Click();

    }

    public void Click() {
        deleteEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = emailId.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("name");

                ref.orderByChild("email").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                dataSnapshot.getChildren().iterator().hasNext()) {

                            //deleting from the database
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String key = childSnapshot.getKey();
                                System.out.println(key);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("name").child(key);
                                ref.removeValue();
                                Toast.makeText(Delete.this, "Successfully deleted.", Toast.LENGTH_LONG).show();
                                Log.i(TAG, "Successfully deleted");
                            }
                        } else {
                            Toast.makeText(Delete.this, "Invalid Email Address.", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Invalid email address");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Delete.this, "An error has occurred.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error has occurred");
                    }
                });
            }
        });
    }
}