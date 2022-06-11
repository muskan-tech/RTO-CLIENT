package com.example.rto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class paid extends AppCompatActivity {
    ListView list;
    TextView name,v,trans,trans_date,date,off;
    Button print;
    ArrayList al = new ArrayList();
    ArrayAdapter<String> adapter;
    private PdfDocument.PageInfo myPageInfo;
    myclass obj;
    private PdfDocument mypdf;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid);
        getSupportActionBar().hide();
        Log.i("ABC", "Paid object is created");
        list = (ListView) findViewById(R.id.list);
        name=(TextView)findViewById(R.id.Name_edit);
        v=(TextView)findViewById(R.id.vehicle_edit);
        date=(TextView)findViewById(R.id.date_edit);
        trans=(TextView)findViewById(R.id.trans_edit);
        trans_date=(TextView)findViewById(R.id.transdate_edit);
        off=(TextView)findViewById(R.id.offense_edit);

        print=(Button)findViewById(R.id.print);
        Intent intent = getIntent();
        obj = (myclass) intent.getSerializableExtra("Details");
        Log.i("ABC", "before snapshot");
        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        Log.i("ABC","AFter");
        name.setText(obj.getName());
        v.setText(obj.getVehicle());
        date.setText(obj.getDate().toString());
        trans.setText(obj.getAmount().toString());
        trans_date.setText(obj.getTrans_date().toString());
        off.setText(obj.getChallan());
        Log.i("ABC","Collected details");

        printclick();




    }
    public void printclick(){
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Log.i("ABC","permission granted");
                    printPDF();

            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},1);

    }
    private boolean checkPermission () {
        Log.i("ABC","Inside check");
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        if ((permission1 == PackageManager.PERMISSION_GRANTED) && (permission2 == PackageManager.PERMISSION_GRANTED))
            return true;
        else
            return false;
    }


    public void printPDF()  {
        mypdf =new PdfDocument();
        Paint paint =new Paint();
        PdfDocument.PageInfo  myPageInfo=new PdfDocument.PageInfo.Builder(250,350,1).create();
        PdfDocument.Page mypage=mypdf.startPage(myPageInfo);
        Canvas canvas =mypage.getCanvas();
        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0,0,0));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD,Typeface.NORMAL));
        canvas.drawText("CHALLAN RECEIPT\n",20,20,paint);
        paint.setTextSize(8.5f);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        canvas.drawText("NAME :"+obj.getName().toString()+"\n",20,40,paint);
        canvas.drawText("VEHICLE NO :"+obj.getVehicle()+"\n",20,50,paint);
        canvas.drawText("DATE :"+obj.getDate().toGMTString()+"\n",20,60,paint);
        canvas.drawText("OFFENSE TYPE :"+obj.getChallan()+"\n",20,70,paint);
        canvas.drawText("TRANSACTION AMOUNT :"+obj.getAmount()+"\n",20,80,paint);
        canvas.drawText("TRANSACTION DATE :"+obj.getTrans_date().toGMTString()+"\n",20,90,paint);
        mypdf.finishPage(mypage);
        createfile();

    }

    private void createfile() {
        Intent intent =new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE,"challanreceipt.pdf");
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==Activity.RESULT_OK){
            Uri uri =null;
            if(data !=null){
                uri=data.getData();
            }
            if(mypdf!=null){
                ParcelFileDescriptor  pfd=null;

                try{
                    pfd=getContentResolver().openFileDescriptor(uri, "w");
                        mypdf.writeTo(new FileOutputStream(pfd.getFileDescriptor()));
                        mypdf.close();
                    Toast.makeText(getApplicationContext(),"Download Successful",Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext()," Unsuccessful",Toast.LENGTH_LONG).show();
                }
                
            }
        }

    }

}