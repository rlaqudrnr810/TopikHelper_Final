package com.example.topikhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Daily_Voca_Test_Activity extends AppCompatActivity{

    ListView listview ;
    DailyVocaTestAdapter adapter;
    Intent intent;
    String day2;
    int count = 1;
    Button vocaTestButton;

    private String meaning = "";
    private EditText answers;


    ArrayList<DailyVocaTestItem> mList = new ArrayList<DailyVocaTestItem>();
    DailyVocaTestItem dailyVocaTestItems;
    //   int[] arr = shuffle();
    String[] _mList;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("단어장");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_voca_test);

        listview = (ListView) findViewById(R.id.DailyVocaTestList);
        answers =(EditText)findViewById(R.id.VocaTestEdittext);
        vocaTestButton = (Button)findViewById(R.id.VocaTestButton);

        intent = getIntent();
        day2 = intent.getStringExtra("key2");



        ref.child(day2).addListenerForSingleValueEvent(new ValueEventListener() { //day값 intent로 넘겨받기
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot ds : dataSnapshot.getChildren()){ //children 전부 가져오기
                    dailyVocaTestItems = ds.getValue(DailyVocaTestItem.class);
                    mList.add(dailyVocaTestItems);

                }
                //mList에 있는 30개 섞어서 10개만 mList에 넣기
                mList = shuffle2(mList);

                // Adapter 생성
                adapter = new DailyVocaTestAdapter(getApplicationContext(), mList);

                // 리스트뷰 참조 및 Adapter달기
                listview.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public ArrayList<DailyVocaTestItem> shuffle2 (ArrayList<DailyVocaTestItem> mList) {
        DailyVocaTestItem[] arr = new DailyVocaTestItem[30];
        for (int i = 0; i < arr.length; i++) {
            arr[i] =  mList.get(i);
        }

        for (int x = 0; x < arr.length; x++) {
            int i = (int) (Math.random() * arr.length);
            int j = (int) (Math.random() * arr.length);

            DailyVocaTestItem tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }

        ArrayList<DailyVocaTestItem> ret = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            ret.add(arr[i]);
        return ret;
    }
}