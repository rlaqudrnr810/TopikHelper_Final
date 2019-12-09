package com.example.topikhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;



public class VirtualTest_Select_Activity extends AppCompatActivity {
    ListView virtualtestlist;
    VirtualTestAdapter adapter;
    int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtualtest_select);

        //리스트에 쓰일 곳 객체 생성
        virtualtestlist=findViewById(R.id.virtualtestlist);

        adapter = new VirtualTestAdapter();

        virtualtestlist.setAdapter(adapter);

        for(int i = 1; i <= 10;i++){
            adapter.addItem(i+"회", "모의고사2");
        }

        virtualtestlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ListViewItem item = (ListViewItem) adapterView.getItemAtPosition(position) ;

                Intent intent = new Intent(getApplicationContext(), VirtualTest_Activity.class);
                //Intent intent = new Intent(getApplicationContext(), VirtualTest_Two_Activity.class);

                intent.putExtra("num", item.getTitle());
                intent.putExtra("dbname", item.getText());
                startActivity(intent);
            }
        });

    }


}