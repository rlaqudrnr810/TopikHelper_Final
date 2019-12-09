package com.example.topikhelper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Daily_Voca_Activity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerAdapter mAdapter;
    DailyVocaItem dailyVocaItems;
    ArrayList<DailyVocaItem> mList = new ArrayList<DailyVocaItem>();
    String day;
    String day2;
    Intent intent;
    TextView textview;
    Dialog dialog;
    private Button test; // 번역

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_voca);
        //textview = findViewById(R.id.customdaytext);

        //테스트 화면으로 이동
        test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Daily_Voca_Activity.this, Daily_Voca_Test_Activity.class);
                intent.putExtra("key2",day);
                startActivity(intent); //액티비티 이동
            }
        });




        mRecyclerView = findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        intent = getIntent();
        day = intent.getStringExtra("key1");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("단어장");
        //일수는 이전 인텐츠에서 넘겨받음
        ref.child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //각 데이당 30개씩 있으니 이건 반복문으로 생성
                for(int i = 1; i <= 30; i++) {
                    //여기서 각 번호별로 안에 들어있는 데이터를 클래스에 매핑시켜줌
                    dailyVocaItems = dataSnapshot.child(i + "번").getValue(DailyVocaItem.class);
                    //각 단어마다 어느날짜의 단어인지 만들어줄거
                    dailyVocaItems.setDay(day);
                    //요건 내가 로그에 확인해보려고 사용한 코드 신경 노노
                    Log.d("cau: ", String.valueOf(dailyVocaItems));
                    //매핑된 저옵를 어레이 리스트로 저장 이건 좀 짱인듯
                    mList.add(dailyVocaItems);
                }
                //리사이클러 뷰 어뎁터에 연결
                mAdapter = new RecyclerAdapter(getApplicationContext(),mList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        //팝업창
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                final DailyVocaItem items = mList.get(position);

                TextView title1 = (TextView) dialog.findViewById(R.id.title1);
                TextView title2 = (TextView) dialog.findViewById(R.id.title2);
                TextView title3 = (TextView) dialog.findViewById(R.id.title3);
                TextView frequency = (TextView) dialog.findViewById(R.id.frequency);


                title1.setText(items.getName());
                title2.setText(items.getPronun());
                title3.setText(items.getMeaning());
                frequency.setText("frequency : " + items.getFrequency());

                dialog.show();

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        */
    }



    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }



    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Daily_Voca_Activity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Daily_Voca_Activity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

    }

    @Override
    public void onBackPressed(){
        Intent refresh = new Intent(this, Dictionary_Select_Activity.class);
        startActivity(refresh);
        finish();

    }

}