package com.example.topikhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VirtualTest_Result_Activity extends AppCompatActivity {
    private ListView resultList, resultList2, resultList3;
    private TextView score;

    private Button wrong;
    private Button all;

    Intent intent;
    int[] my;
    int[] op;
    int[] q;
    int[] userAll;
    int[] answer;
    int[] qAll = new int[100];
    private String[] url;
    private String[] mp3;
    private String dbname;
    private String num;
    private Bundle extras;
    private DatabaseReference ref;
    private DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("사용자");
    private FirebaseAuth firebaseAuth;
    String userKey = "";
    String email = "";
    String history = "";
    String grade = "";
    String date = "";
    private int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtualtest_result);

        VirtualTestResultAdapter adapter;
        adapter = new VirtualTestResultAdapter();

        resultList = findViewById(R.id.list);
        resultList.setAdapter(adapter);
        score = findViewById(R.id.score);
        wrong = findViewById(R.id.wrong);
        all = findViewById(R.id.all);


        intent = getIntent();
        extras = getIntent().getExtras();
        num = intent.getStringExtra("num");         // n회
        dbname = intent.getStringExtra("dbname");   // 모의고사1 or 모의고사2
        my = extras.getIntArray("my");
        op = extras.getIntArray("op");
        q = extras.getIntArray("q");
        url=extras.getStringArray("url");
        mp3=extras.getStringArray("mp3");
        userAll = extras.getIntArray("allUserAnswer");
        answer = extras.getIntArray("allAnswer");


        firebaseAuth = FirebaseAuth.getInstance();

        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();

        email = user.getEmail();

        int x = 200 - (my.length * 2);
        grade = Integer.toString(x);
        score.setText(grade + " / 200");

        date = getToDay();
        updateHistory();

        ref = FirebaseDatabase.getInstance().getReference(dbname);

        for(int i=0; i<q.length; i++) {
            String s = Integer.toString(q[i]);
            adapter.addItem(count + "",s + "번", Integer.toString(op[i]), Integer.toString(my[i]));
            count++;
        }
        for(int i = 0; i < qAll.length; i++)
            qAll[i] = i + 1;

        wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VirtualTest_Result_Activity.this, VirtualTest_Wrong_Question_Activity.class);
                i.putExtra("num", num);
                i.putExtra("dbname", dbname);
                i.putExtra("my", my);
                i.putExtra("op", op);
                i.putExtra("q", q);
                i.putExtra("url", url);
                i.putExtra("mp3", mp3);
                i.putExtra("type", 1);
                startActivity(i);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VirtualTest_Result_Activity.this, VirtualTest_Wrong_Question_Activity.class);
                i.putExtra("num", num);
                i.putExtra("dbname", dbname);
                i.putExtra("my", userAll);
                i.putExtra("op", answer);
                i.putExtra("q", qAll);
                i.putExtra("url", url);
                i.putExtra("mp3", mp3);
                i.putExtra("type", 0);
                startActivity(i);
            }
        });
    }

    public String getToDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       //시간까지
        return sdf.format(new Date());
    }

    public void updateHistory(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if( ds.child("email").getValue().toString().equals(email)) {
                        history = String.valueOf(ds.child("history").getValue());
                        String s = num.substring(0,1);
                        Map<String, Object> userUpdates = new HashMap<>();
                        if(history.equals("null"))
                            history = "#" + s + "_" + grade + "_" + date;
                        else
                            history += "#" + s + "_" + grade + "_" + date;
                        userKey = ds.getKey();
                        userUpdates.put(userKey + "/history", history);
                        DatabaseReference bk = FirebaseDatabase.getInstance().getReference("사용자");
                        bk.updateChildren(userUpdates);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("on Cancelled ERROR!", databaseError.getMessage());
            }
        };
        ref1.addListenerForSingleValueEvent(valueEventListener);
    }

}