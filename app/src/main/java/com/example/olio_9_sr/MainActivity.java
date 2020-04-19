package com.example.olio_9_sr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    Spinner teatterit;
    Button hae;
    MainClass main;
    TextView nimi;
    LinearLayout linearLayout;
    EditText pvm;
    EditText klo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            main = new MainClass();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        linearLayout = findViewById(R.id.linear_layout);
        teatterit = findViewById(R.id.teatterit);

        hae = findViewById(R.id.hae);
        nimi = findViewById(R.id.nimi);
        klo = findViewById(R.id.klo);
        pvm = findViewById(R.id.pvm);

        try {
            main.parseXML(main.result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addAdapter();
        hae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTextViews();
                main.clearMovies();
                String text = teatterit.getSelectedItem().toString();
                int length = 0;
                String url = null;
                try {
                    url = main.parseUrl(text, pvm.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    url = main.makeRequest(url);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    length = main.parseMovies(url, klo.getText().toString());
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (length == 0) {
                    createTextViews(length);
                }
                else {
                    createTextViews(length);
                }
            }
        });


    }

    void addAdapter(){
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, main.getLocations());
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        teatterit.setAdapter(adapter1);
    }

    void removeTextViews(){
        Log.d("RemoveTextViews:", "Removing textviews");
        linearLayout.removeAllViews();
    }
    void createTextViews(int length){
        Log.d("child count", String.valueOf(linearLayout.getChildCount()));
        for(int i = 0; i < length; i++) {
            TextView textView = new TextView(this);
            textView.setId(i);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText(main.movies.movies.get(i).title + "\n" + main.movies.movies.get(i).showStart + "\n" + main.movies.movies.get(i).location + "\n");
            linearLayout.addView(textView);
            Log.d("Length:", String.valueOf(linearLayout.getChildCount()));
        }
        if(length == 0){
            TextView textView = new TextView(this);
            textView.setText("Ei löytynyt elokuvia!");
            linearLayout.addView(textView);
            Log.d("Length:", String.valueOf(linearLayout.getChildCount()));
        }
    }




}

