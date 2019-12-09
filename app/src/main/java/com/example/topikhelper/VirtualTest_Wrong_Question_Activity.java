package com.example.topikhelper;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VirtualTest_Wrong_Question_Activity extends AppCompatActivity implements Runnable{


    private MediaPlayer mMediaplayer;
    private int playbackPosition =0;
    private TextView tx;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button next;
    private Button pre;
    private Button play;
    private Button pause;
    private Button stop;
    private Button adding;

    private PhotoView img;

    private TextView txt;

    SeekBar seekBar;
    boolean wasPlaying = true;
    String t = "00:00";

    Intent intent;
    int[] my;
    int[] op;
    int[] q;
    String dbname;
    String num;
    Bundle extras;
    DatabaseReference ref;
    DatabaseReference ref1;
    String[] url;
    String[] mp3;
    String[] u;
    String[] m;
    int type;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_test_wrong_question);



        intent = getIntent();
        extras = getIntent().getExtras();

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        next = findViewById(R.id.next);
        pre = findViewById(R.id.pre);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        adding = findViewById(R.id.adding_solution);
        tx = findViewById(R.id.textView6);

        txt = findViewById(R.id.time);
        final TextView seekBarHint = findViewById(R.id.time);

        seekBar = findViewById(R.id.seekbar);

        num = intent.getStringExtra("num");         // n회
        dbname = intent.getStringExtra("dbname");   // 모의고사1 or 모의고사2
        op = extras.getIntArray("op");
        my = extras.getIntArray("my");
        q = extras.getIntArray("q");
        m = extras.getStringArray("mp3");
        u = extras.getStringArray("url");

        type = intent.getIntExtra("type", -1);
        if(type == 0)
            tx.setText("Virtual Test - All");
        ref = FirebaseDatabase.getInstance().getReference(dbname).child(num);
        ref1 = FirebaseDatabase.getInstance().getReference();

        url = new String[my.length];
        mp3 = new String[my.length];

        img = findViewById(R.id.img);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mMediaplayer == null || (mMediaplayer != null && !mMediaplayer.isPlaying()))
                    playAudio();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaplayer !=null){
                    //현재 재생위치 저장
                    playbackPosition = mMediaplayer.getCurrentPosition();
                    mMediaplayer.pause();
                    wasPlaying = false;
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaplayer != null){
                    playbackPosition = 0;
                    mMediaplayer.stop();
                    mMediaplayer = null;
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNext();
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPre();
            }
        });

        adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(my[count] == op[count]) {
                    Intent i = new Intent(VirtualTest_Wrong_Question_Activity.this, Solution_AddingPopup_Activity.class);
                    startActivityForResult(i, 2);
                }
                else{
                    Toast.makeText(VirtualTest_Wrong_Question_Activity.this, "Only the correct answerer can register the solution.", Toast.LENGTH_LONG).show();
                }
            }
        });
        getData();
        showFirst();

        if(wasPlaying) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    seekBarHint.setVisibility(View.VISIBLE);
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                    if(fromTouch && mMediaplayer != null && mMediaplayer.isPlaying()) {
                        mMediaplayer.seekTo(progress);
                    }
                    seekBarHint.setVisibility(View.VISIBLE);
                    int m = progress / 60000;
                    int s = (progress % 60000) / 1000;
                    String strTime = String.format("%02d:%02d", m, s);
                    seekBarHint.setText(strTime + " / " + t);

                    if (progress > 0 && mMediaplayer != null && !mMediaplayer.isPlaying()) {
                        //clearMediaPlayer();
                        //fab.setImageDrawable(ContextCompat.getDrawable(One_Solve_Writing_Activity.this, android.R.drawable.ic_media_play));
                        VirtualTest_Wrong_Question_Activity.this.seekBar.setProgress(0);
                    }

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mMediaplayer != null && mMediaplayer.isPlaying()) {
                        mMediaplayer.seekTo(seekBar.getProgress());
                    }
                }
            });
        }
    }

    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        ref.child(q[count] + "번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sol = "";
                sol = String.valueOf(dataSnapshot.child("해설").getValue());
                Intent i = new Intent(VirtualTest_Wrong_Question_Activity.this, Solution_Popup_Activity.class);
                i.putExtra("sol", sol);
                startActivityForResult(i, 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*
        Intent i = new Intent(this, Solution_Popup_Activity.class);
        i.putExtra("data", "솔루션");
        startActivityForResult(i, 1);
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String sol = data.getStringExtra("sol");
                if(!sol.equals(""))
                    ref1.child("해설등록 요청").child(dbname).child(num).child(q[count] + "번").push().setValue(sol);
            }
        }
    }

    public void getData(){
        for(int i = 0; i < my.length; i++) {
            int x = q[i];
            url[i] = u[x - 1];
            if(x <= 50)
                mp3[i] = m[x - 1];
            else
                mp3[i] = "";
        }
    }

    public void showNext(){
        if(mMediaplayer != null){
            mMediaplayer.stop();
        }
        mMediaplayer = null;
        if(count >= my.length - 1){
            Toast.makeText(VirtualTest_Wrong_Question_Activity.this, "The next problem does not exist.", Toast.LENGTH_LONG).show();
        }
        else{
            count++;
            setButton();
            viewButton(q[count]);
            Glide.with(VirtualTest_Wrong_Question_Activity.this).load(url[count]).into(img);
        }
    }

    public void showPre(){
        if(mMediaplayer != null){
            mMediaplayer.stop();
        }
        mMediaplayer = null;
        if(count == 0)
            Toast.makeText(this, "The previous problem does not exist.", Toast.LENGTH_SHORT).show();
        else{
            count--;
            setButton();
            viewButton(q[count]);
            Glide.with(VirtualTest_Wrong_Question_Activity.this).load(url[count])
                    .into(img);
        }
    }

    public void showFirst(){
        setButton();
        viewButton(q[0]);
        ref.child(q[0] + "번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s = "";
                s = dataSnapshot.child("url").getValue().toString();
                Glide.with(VirtualTest_Wrong_Question_Activity.this).load(s).into(img);
                mp3[0] = String.valueOf(dataSnapshot.child("mp3").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void viewButton(int n){
        if(n > 50){
            play.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
            stop.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            txt.setVisibility(View.GONE);
        }
        else{
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            txt.setVisibility(View.VISIBLE);
        }
    }

    public void setButton(){
        b1.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b2.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b3.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b4.setBackgroundColor(Color.parseColor("#FFEEE8AA"));

        switch(my[count]){
            case 1 :
                b1.setBackgroundColor(Color.RED);
                break;
            case 2 :
                b2.setBackgroundColor(Color.RED);
                break;
            case 3 :
                b3.setBackgroundColor(Color.RED);
                break;
            case 4 :
                b4.setBackgroundColor(Color.RED);
        }
        switch(op[count]){
            case 1 :
                b1.setBackgroundColor(Color.BLUE);
                break;
            case 2 :
                b2.setBackgroundColor(Color.BLUE);
                break;
            case 3 :
                b3.setBackgroundColor(Color.BLUE);
                break;
            case 4 :
                b4.setBackgroundColor(Color.BLUE);
        }

    }

    private void playAudio() throws Exception{
        if(mMediaplayer == null) {
            mMediaplayer = new MediaPlayer();

            mMediaplayer.setDataSource(mp3[count]);
            mMediaplayer.prepare();
            mMediaplayer.start();
        }
        else{
            mMediaplayer.start();
            mMediaplayer.seekTo(playbackPosition);
        }
        mMediaplayer.setLooping(false);
        wasPlaying = true;
        seekBar.setMax(mMediaplayer.getDuration());
        int m = mMediaplayer.getDuration() / 60000;
        int s = (mMediaplayer.getDuration() % 60000) / 1000;
        t = String.format("%02d:%02d", m, s);
        new Thread(this).start();

    }

    protected void onDestroy(){
        killMediaPlayer();
        super.onDestroy();
    }

    private void killMediaPlayer(){
        if(mMediaplayer !=null && !mMediaplayer.isPlaying()){
            try{
                mMediaplayer.release();
            }catch(Exception e){
                Log.e("error",e.getMessage());
            }
        }
    }

    public void onBackPressed() {
        if(mMediaplayer != null && mMediaplayer.isPlaying()) {
            mMediaplayer.stop();
            mMediaplayer = null;
        }
        finish();
        super.onBackPressed();
    }

    public void run() {

        int currentPosition = mMediaplayer.getCurrentPosition();
        int total = mMediaplayer.getDuration();


        while (mMediaplayer != null && mMediaplayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mMediaplayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            seekBar.setProgress(currentPosition);
        }
    }

}