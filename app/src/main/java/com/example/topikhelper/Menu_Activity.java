package com.example.topikhelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menu_Activity extends AppCompatActivity {
        private MainBackPressCloseHandler mainBackPressCloseHandler;
        private long backKeyPressedTime = 0;
        private Toast toast;

        private Button button1; // 모의고사 풀기
        private Button button2; // 한문제씩 풀기
        private Button button3; // 사전
        private Button button4; // 마이페이지
        private Button button5; // 번역

        String email = "";
        String nickname = "";
        String history = "";
        ProgressDialog progressDialog;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("사용자");
        private FirebaseAuth firebaseAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
            mainBackPressCloseHandler = new MainBackPressCloseHandler(this);

            firebaseAuth = FirebaseAuth.getInstance();

            //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
            if(firebaseAuth.getCurrentUser() == null) {
                finish();
                startActivity(new Intent(this, Login.class));
            }

            //유저가 있다면, null이 아니면 계속 진행
            FirebaseUser user = firebaseAuth.getCurrentUser();

            email = user.getEmail();
            getUserData();


            progressDialog = new ProgressDialog(this);

            button1 = findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Menu_Activity.this, VirtualTest_Select_Activity.class);
                    startActivity(intent); //액티비티 이동
                }
            });

            button2 = findViewById(R.id.button2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Menu_Activity.this, One_Solve_Select_Activity.class);
                    startActivity(intent); //액티비티 이동
                }
            });

            button3 = findViewById(R.id.button3);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Menu_Activity.this, Dictionary_Select_Activity.class);
                    startActivity(intent); //액티비티 이동
                }
            });

            button4 = findViewById(R.id.button4);
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Menu_Activity.this, Mypage_Activity.class);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("history", history);
                    intent.putExtra("email", email);
                    startActivity(intent); //액티비티 이동
                }
            });
            button5 = findViewById(R.id.button5);
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Menu_Activity.this, Translation_Activity.class);
                    startActivity(intent); //액티비티 이동
                }
            });

        }
    public void getUserData(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if( ds.child("email").getValue().toString().equals(email)) {
                        nickname = String.valueOf(ds.child("nickname").getValue());
                        history = String.valueOf(ds.child("history").getValue());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("on Cancelled ERROR!", databaseError.getMessage());
            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }


    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            ActivityCompat.finishAffinity(this);
        }
    }
    public void showGuide() {
        toast = Toast.makeText(this, "Press the \'back button\' again to exit.",
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
