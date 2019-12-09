package com.example.topikhelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Customer_Service_Activity extends AppCompatActivity {

    static SharedPreferences sp;
    private TextView q1;
    private TextView q2;
    private TextView q3;
    private TextView q4;
    private TextView q5;

    private TextView a1;
    private TextView a2;
    private TextView a3;
    private TextView a4;
    private TextView a5;

    boolean[] press = new boolean[5];
    //SharedPreferences.Editor editor = sp.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        //String keyword = sp.getString("keyword","");

        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
        a5 = findViewById(R.id.a5);
        q1 = findViewById(R.id.q1);
        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!press[0]){
                    a1.setVisibility(View.VISIBLE);
                    press[0] = true;
                }
                else{
                    a1.setVisibility(View.GONE);
                    press[0] = false;
                }
            }
        });
        q2 = findViewById(R.id.q2);
        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!press[1]){
                    a2.setVisibility(View.VISIBLE);
                    press[1] = true;
                }
                else{
                    a2.setVisibility(View.GONE);
                    press[1] = false;
                }
            }
        });
        q3 = findViewById(R.id.q3);
        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!press[2]){
                    a3.setVisibility(View.VISIBLE);
                    press[2] = true;
                }
                else{
                    a3.setVisibility(View.GONE);
                    press[2] = false;
                }
            }
        });
        q4 = findViewById(R.id.q4);
        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!press[3]){
                    a4.setVisibility(View.VISIBLE);
                    press[3] = true;
                }
                else{
                    a4.setVisibility(View.GONE);
                    press[3] = false;
                }
            }
        });
        q5 = findViewById(R.id.q5);
        q5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!press[4]){
                    a5.setVisibility(View.VISIBLE);
                    press[4] = true;
                }
                else{
                    a5.setVisibility(View.GONE);
                    press[4] = false;
                }
            }
        });

    }
}
