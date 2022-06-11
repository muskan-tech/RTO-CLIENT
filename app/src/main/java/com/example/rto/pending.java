package com.example.rto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class pending extends AppCompatActivity {
    TextView name,v,trans,date,off;
    Button payment;
    String document_id;
    DocumentReference ref;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid);
        name=(TextView)findViewById(R.id.Name_edit);
        v=(TextView)findViewById(R.id.vehicle_edit);
        date=(TextView)findViewById(R.id.date_edit);
        trans=(TextView)findViewById(R.id.trans_edit);
        off=(TextView)findViewById(R.id.offense_edit);


        payment=(Button)findViewById(R.id.print);
        Intent intent = getIntent();
        myclass obj = (myclass) intent.getSerializableExtra("Details");
        id=intent.getStringExtra("Id");
        name.setText(obj.getName());
        v.setText(obj.getVehicle());
        date.setText(obj.getDate().toString());
        trans.setText(obj.getAmount().toString());
        off.setText(obj.getChallan());

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ABC", "after getting id form firebaes");
                Log.i("ABC",id);
                FirebaseFirestore.getInstance().collection(obj.getVehicle()).document(id).update("Transaction", true);
                FirebaseFirestore.getInstance().collection(obj.getVehicle()).document(id).update("Trans_date", Timestamp.now());
                Toast.makeText(getApplicationContext(), "Payment Successful !", Toast.LENGTH_SHORT).show();
            }
            });


    }
}