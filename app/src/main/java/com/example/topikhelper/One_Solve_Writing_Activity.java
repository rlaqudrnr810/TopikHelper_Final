package com.example.topikhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class One_Solve_Writing_Activity extends AppCompatActivity {
    private Button next;
    private Button pre;
    private EditText text;
    private PhotoView imageView;

    String[] url = new String[4];
    String[] answer = new String[4];
    String[] userAnswer = new String[4];

    int index = 0;
    int count = 0;
    int[] arr = shuffle();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("유형");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_solve_writing);

        imageView = (PhotoView) findViewById(R.id.img);
        next = (Button) findViewById(R.id.next);
        pre = (Button) findViewById(R.id.pre);
        text = findViewById(R.id.text);

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPre();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNext();
            }
        });

        showFirst();
        getData();
    }
    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent i = new Intent(One_Solve_Writing_Activity.this, Solution_Popup_Activity.class);
        i.putExtra("sol", answer[count]);
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    public void restart(){
        arr = shuffle();
        count = 0;
        String n = Integer.toString(arr[count]);
        for(int i = 0; i < 4; i++){
            userAnswer[i] = "";
        }
        ref.child("쓰기").child(n+"번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = "";
                url = dataSnapshot.child("url").getValue().toString();

                Glide.with(One_Solve_Writing_Activity.this).load(url)
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void showFirst(){
        ref.child("쓰기").child(arr[count] + "번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s = "";
                s = dataSnapshot.child("url").getValue().toString();
                Glide.with(One_Solve_Writing_Activity.this).load(s).into(imageView);
                answer[0] = dataSnapshot.child("정답").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getData(){
        for(int i = 0; i < 4; i++) {
            ref.child("쓰기").child(arr[i] + "번").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String s = "";
                    s = dataSnapshot.child("정답").getValue().toString();
                    answer[index] = s;
                    url[index] = dataSnapshot.child("url").getValue().toString();
                    index++;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void showNext(){
        userAnswer[count] = text.getText().toString().trim();
        count++;
        if(count >= 4){
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(One_Solve_Writing_Activity.this);
            alert_confirm.setMessage("RETRY?").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            restart();
                        }
                    }
            );
            alert_confirm.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alert_confirm.show();
        }
        else{
            Glide.with(One_Solve_Writing_Activity.this).load(url[count]).into(imageView);
            if(userAnswer[count] != null && !userAnswer[count].isEmpty()) {
                text.setText(userAnswer[count]);
            }
            else
                text.setText(null);
        }
    }

    public void showPre(){
        userAnswer[count] = text.getText().toString().trim();
        if(count == 0)
            Toast.makeText(this, "The previous problem does not exist.", Toast.LENGTH_SHORT).show();
        else{
            count--;
            Glide.with(One_Solve_Writing_Activity.this).load(url[count])
                    .into(imageView);
            if(userAnswer[count] != null && !userAnswer[count].isEmpty())
                text.setText(userAnswer[count]);
            else
                text.setText(null);
        }
    }

    public int[] shuffle(){

        int[] arr = new int[4];

        for(int i = 0; i < arr.length; i++){
            arr[i] = i + 1;
        }

        for(int x = 0; x < arr.length; x++){
            int i = (int)(Math.random()*arr.length);
            int j = (int)(Math.random()*arr.length);

            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }
}