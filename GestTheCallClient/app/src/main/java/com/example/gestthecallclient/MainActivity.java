package com.example.gestthecallclient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.time.LocalDateTime.now;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void onClickCheck(View view) throws IOException {
        final TextView requestResult = (TextView) findViewById(R.id.requestResult);
        final TextView stringRequest = (TextView) findViewById(R.id.stringRequest);
        EditText ip = (EditText) findViewById(R.id.editIP);
        String ipSTR = ip.getText().toString();
        URL url = new URL("http://"+ipSTR+"/checkServer.php?check=client");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //Creating a Scanner object
            Scanner sc = new Scanner(in);
            //Reading line by line from scanner to StringBuffer
            StringBuilder sb = new StringBuilder();
            while(sc.hasNext()) {
                sb.append(sc.nextLine());
            }
            //Log.d("STRING", sb.toString());
            //Log.d("STRING", String.valueOf(sb.toString().equals("Server OK")));
            if (sb.toString().equals("Server OK")) {
                requestResult.setText("Config Status: "+sb.toString());
                stringRequest.setText(url.toString());
            }else{
                requestResult.setText("Config Status: "+"Not connected");
                stringRequest.setText(url.toString());
            }
        } finally {
                urlConnection.disconnect();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onSendReg(View view) throws IOException{
        //Creamos el formato de fecha necesario para la URL
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd%20HH:mm:ss");
        //Obtenemos la fecha
        LocalDateTime nowTime = LocalDateTime.now();
        //Formateamos la fecha
        String fDate = nowTime.format(myFormat);
        fDate = "time="+fDate;
        //Obtenemos el telefono
        EditText phone = (EditText) findViewById(R.id.editPhone);
        String phoneStr = "number="+phone.getText().toString();
        //Obtenemos la ip
        EditText ip = (EditText) findViewById(R.id.editIP);
        //Componemos el query completo
        String queryStr = "http://"+ip.getText().toString()+"/putEntrantes.php?"+fDate+"&"+phoneStr;
        //Mostramos el query completo
        TextView queryResult = (TextView) findViewById(R.id.queryResult);
        queryResult.setText(queryStr);
        //Hacemos la peticion
        URL url = new URL(queryStr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //Creating a Scanner object
            Scanner sc = new Scanner(in);
            //Reading line by line from scanner to StringBuffer
            StringBuilder sb = new StringBuilder();
            while(sc.hasNext()) {
                sb.append(sc.nextLine());
            }
            queryResult.setText(sb);
        } finally {
            urlConnection.disconnect();
        }
    }
}