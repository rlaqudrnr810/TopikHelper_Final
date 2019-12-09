package com.example.topikhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class History_Activity extends AppCompatActivity {
    private ListView listview ;
    private History_ListView_Adapter adapter;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("사용자");

    private Intent intent;
    private String history = "";
    private Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        intent = getIntent();
        //extras = getIntent().getExtras();
        history = intent.getStringExtra("history");

        adapter = new History_ListView_Adapter();

        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        viewList();
        go = findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(History_Activity.this, VirtualTest_Select_Activity.class));
                //finish();
            }
        });
    }

    public void viewList(){
        String[] str = history.split("#");

        for(int i = str.length - 1; i > 0; i--){
            String[] str1 = str[i].split("_");
            adapter.addItem(str1[0] + "회", str1[1] + " / 200", str1[2]);
        }
    }
}