package com.example.rto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Login extends AppCompatActivity {
    Button paid, unpaid,track;
    TextView txt, heading;
    ListView list;
    ArrayList al = new ArrayList();
    ArrayList l = new ArrayList();
    ArrayList c_date = new ArrayList();
    ArrayList l_date = new ArrayList();
    ArrayAdapter<String> adapter;
    String fullname = "a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        paid = (Button) findViewById(R.id.paid);
        unpaid = (Button) findViewById(R.id.unpaid);
        txt = (TextView) findViewById(R.id.txt1);
        heading = (TextView) findViewById(R.id.heading);
        list = (ListView) findViewById(R.id.list);
        track=(Button)findViewById(R.id.track);
        Intent intent = getIntent();
        String str = intent.getStringExtra("name");
    Log.i("str", str);

    track.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(Login.this,MapsActivity.class));
        }
    });

        FirebaseFirestore.getInstance().collection("Personal Details").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot s : value) {
                    Log.i("ABC", String.valueOf(s.getId()));
                    if (s.getId().toString().equals(str)) {
                        fullname = s.getString("FirstName") + " " + s.getString("LastName");
                        Log.i("ABC", fullname);
                        txt.setText(fullname);
                    }
                }
            }

        });


        FirebaseFirestore.getInstance().collection(str).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                al.clear();
                l.clear();
                for (DocumentSnapshot s : value) {
                    Log.i("s.getId()",s.getId());
                    if (s.getBoolean("Transaction")) {
                        c_date.add(s.getDate("Date").toString());
                        al.add(s.getString("Name"));
                    } else {
                        l_date.add(s.getDate("Date").toString());
                        l.add(s.getString("Name"));
                    }
                }

            }
        });
        paid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                heading.setText("Viewing challans that are already paid....");
                CustomAdapter adapter = new CustomAdapter(al, c_date);
                list.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "Click on the Name to get Details", Toast.LENGTH_SHORT).show();
                Log.i("ABC", "Beforeonclick");
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        myclass obj = new myclass();
                        final Double[] am = new Double[1];
                        FirebaseFirestore.getInstance().collection("Challan").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                for (DocumentSnapshot s : value) {
//                                    Log.i("ABC", String.valueOf(s.getId()));
                                    if (s.getString("Name").toString().equals(al.get(i))) {
                                        am[0] = s.getDouble("Amount");
                                        Log.i("ABC", "challan");
                                    }
                                }
                                FirebaseFirestore.getInstance().collection(str).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                        for (DocumentSnapshot s : value) {
                                            if (s.getDate("Date").toString().equals(c_date.get(i))) {
                                                obj.setChallan(s.getString("Name"));
                                                Log.i("Challan", String.valueOf(obj.getChallan()));
                                                obj.setDate(s.getDate("Date"));
                                                Log.i("date", String.valueOf(obj.getDate()));
                                                obj.setName(fullname);
                                                Log.i("name", String.valueOf(obj.getName()));
                                                obj.setAmount(am[0]);
                                                Log.i("Amount", String.valueOf(obj.getAmount()));
                                                obj.setTrans_date(s.getDate("Trans_date"));
                                                obj.setVehicle(str);
                                                Log.i("Vehicle", str);
                                            }
                                        }
                                        startActivity(new Intent(Login.this, paid.class).putExtra("Details", obj));
                                        finish();
                                        Log.i("ABC", "SUCCESS SUCCESS");
                                    }
                                });


                            }

                        });


                        Log.i("ABC", String.valueOf(i));

                    }
                });

            }


        });
        unpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heading.setText("Viewing challans that are left to be paid....");
                CustomAdapter adapter = new CustomAdapter(l, l_date);
                list.setAdapter(adapter);
                final String[] id = new String[1];
                Toast.makeText(getApplicationContext(), "Click on the Name to get Details", Toast.LENGTH_LONG).show();
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long h) {
                        myclass mycl = new myclass();
                        final Double[] am = new Double[1];
                        FirebaseFirestore.getInstance().collection("Challan").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                for (DocumentSnapshot s : value) {
//                                    Log.i("ABC", String.valueOf(s.getId()));
                                    if (s.getString("Name").equals(l.get(i))) {
                                        am[0] = s.getDouble("Amount");
                                        Log.i("ABC", "challan");
                                    }
                                }
                                FirebaseFirestore.getInstance().collection(str).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                        for (DocumentSnapshot s : value) {
                                            Log.i("ABC","Inside Snapshot");

                                            if (s.getDate("Date").toString().equals(l_date.get(i))) {
                                                id[0]=s.getId();
                                                Log.i("ABC","Inside if");
                                                mycl.setChallan(s.getString("Name"));
                                                Log.i("challan", String.valueOf(mycl.getChallan()));
                                                mycl.setDate(s.getDate("Date"));
                                                Log.i("date", String.valueOf(mycl.getDate()));
                                                mycl.setName(fullname);
                                                Log.i("name", String.valueOf(mycl.getName()));
                                                mycl.setAmount(am[0]);
                                                Log.i("Amount" , String.valueOf(mycl.getAmount()));

                                                mycl.setVehicle(str);
                                                Log.i("ABC","AFter storing in class");
                                                break;
                                            }
                                        }
                                        Intent intent=new Intent(Login.this, pending.class);
                                        intent.putExtra("Details", mycl);
                                        intent.putExtra("Id",id[0]);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        });


                        Log.i("ABC", String.valueOf(i));
                    }
                });
            }
        });

    }

    private class CustomAdapter extends BaseAdapter {
        ArrayList temp = new ArrayList();
        ArrayList d = new ArrayList();

        public CustomAdapter(ArrayList a, ArrayList b) {
            this.temp = a;
            this.d = b;
        }

        @Override
        public int getCount() {
            return temp.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.list_custom, null);
            TextView n = view1.findViewById(R.id.text1);
            TextView dt = view1.findViewById(R.id.date);

            Log.i("X", String.valueOf(i));
            n.setText(String.valueOf(temp.get(i)));
            dt.setText(String.valueOf(d.get(i)));

            return view1;

        }
    }

}
    class myclass implements Serializable {
        String challan;
        Double amount;
        String name;
        Date date;
        Date Trans_date;
        String vehicle;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getChallan() {
            return challan;
        }

        public void setChallan(String challan) {
            this.challan = challan;
        }

        public String getVehicle() {
            return vehicle;
        }

        public void setVehicle(String vehicle) {
            this.vehicle = vehicle;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }


        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }


        public Date getTrans_date() {
            return Trans_date;
        }

        public void setTrans_date(Date date) {
            this.Trans_date = date;
        }


    }



