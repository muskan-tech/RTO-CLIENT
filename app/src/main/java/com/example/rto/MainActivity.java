package com.example.rto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button login;
    EditText vehicle;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        login=(Button)findViewById(R.id.submit);
        vehicle=(EditText)findViewById(R.id.vehicle);
        vehicle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)){
                    login.performClick();

                }
                return false;

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String v=vehicle.getText().toString();
                if(TextUtils.isEmpty(v)){
                    Toast.makeText(getApplicationContext(),"Enter Vehicle No",Toast.LENGTH_LONG).show();
                }
                else{
                    String str=vehicle.getText().toString();
                    startActivity(new Intent(MainActivity.this,Login.class).putExtra("name",str));

            }
        }

    });


}

}