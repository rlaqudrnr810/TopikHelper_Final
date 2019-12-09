package com.example.topikhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class One_Solve_Listening_Activity extends AppCompatActivity implements Runnable {

    private MediaPlayer mMediaplayer;

    private Button play;
    private Button stop;
    private Button pause;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button check;
    private Button next;
    private Button pre;
    private Button solution;
    private Button adding;

    private PhotoView imageView;

    boolean c = false;

    int index = 0;
    int count = 0;
    int[] arr = shuffle();

    String[] url = new String[10];
    String[] mp3 = new String[10];
    int[] answer = new int[10];
    int[] userAnswer = new int[10];
    boolean[] bk = new boolean[10];

    SeekBar seekBar;
    boolean wasPlaying = true;
    String t = "00:00";

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("유형");
    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    private int playbackPosition =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_solve_listening);
        mMediaplayer = new MediaPlayer();
        mMediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mMediaplayer = null;
        //ref_two.child(firebaseUser.getUid()).child("history").setValue(url);
        imageView = (PhotoView) findViewById(R.id.img);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        pause = (Button) findViewById(R.id.pause);

        b1 = (Button) findViewById(R.id.Listening_b1);
        b2 = (Button) findViewById(R.id.Listening_b2);
        b3 = (Button) findViewById(R.id.Listening_b3);
        b4 = (Button) findViewById(R.id.Listening_b4);
        check = (Button) findViewById(R.id.check);
        next = (Button) findViewById(R.id.next);
        pre = (Button) findViewById(R.id.pre);
        solution = (Button) findViewById(R.id.sol);
        adding = findViewById(R.id.adding_solution);

        setButton(0);

        adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bk[count] && userAnswer[count] == answer[count]) {
                    Intent i = new Intent(One_Solve_Listening_Activity.this, Solution_AddingPopup_Activity.class);
                    startActivityForResult(i, 2);
                }
                else{
                    Toast.makeText(One_Solve_Listening_Activity.this, "Only the correct answerer can register the solution.", Toast.LENGTH_LONG).show();
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mMediaplayer == null || (mMediaplayer != null && !mMediaplayer.isPlaying()))
                        //p = false;
                        playAudio();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mMediaplayer !=null){
                    //현재 재생위치 저장
                    playbackPosition = mMediaplayer.getCurrentPosition();
                    mMediaplayer.pause();
                    //wasPlaying = false;
                    //p = true;
                    seekBar.setProgress(playbackPosition);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mMediaplayer != null){
                    playbackPosition = 0;
                    mMediaplayer.stop();
                    seekBar.setProgress(0);
                    mMediaplayer = null;
                    //p = false;

                }
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bk[count]){
                    c = true;
                    setButton(1);
                    userAnswer[count] = 1;
                }
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bk[count]){
                    c = true;
                    setButton(2);
                    userAnswer[count] = 2;
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bk[count]){
                    c = true;
                    setButton(3);
                    userAnswer[count] = 3;
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bk[count]){
                    c = true;
                    setButton(4);
                    userAnswer[count] = 4;
                }
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaplayer != null && mMediaplayer.isPlaying())
                    mMediaplayer.stop();
                mMediaplayer = null;
                seekBar.setProgress(0);
                wasPlaying = false;
                showPre();
                //p = false;
            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bk[count]){
                    if(c){
                        c = false;
                        checkAnswer();
                    }
                    else{
                        viewMessage();
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaplayer != null && mMediaplayer.isPlaying())
                    mMediaplayer.stop();
                mMediaplayer = null;
                seekBar.setProgress(0);
                wasPlaying = false;
                //p = false;
                showNext();
            }
        });

        ref.child("듣기").child(arr[0]+"번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = "";
                url = dataSnapshot.child("url").getValue().toString();
                mp3[0] = "";
                mp3[0] = dataSnapshot.child("mp3").getValue().toString();
                Glide.with(One_Solve_Listening_Activity.this).load(url)
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getData();

        final TextView seekBarHint = findViewById(R.id.time);

        seekBar = findViewById(R.id.seekbar);

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

                    /*
                    seekBarHint.setVisibility(View.VISIBLE);
                    int x = (int) Math.ceil(progress / 1000f);
                    if (mMediaplayer != null) {
                        sec = (int) Math.ceil(Integer.parseInt(String.valueOf(mMediaplayer.getDuration())) / 1000f);
                        if (sec >= 60) {
                            min = sec / 60;
                            sec %= 60;
                        }
                    }


                     */

                    //double percent = progress / (double) seekBar.getMax();
                    //int offset = seekBar.getThumbOffset();
                    //int seekWidth = seekBar.getWidth();
                    //int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                    //int labelWidth = seekBarHint.getWidth();
                    //seekBarHint.setX(offset + seekBar.getX() + val
                    //        - Math.round(percent * offset)
                    //        - Math.round(percent * labelWidth / 2));

                    if (progress > 0 && mMediaplayer != null && !mMediaplayer.isPlaying()) {
                        //clearMediaPlayer();
                        //fab.setImageDrawable(ContextCompat.getDrawable(One_Solve_Writing_Activity.this, android.R.drawable.ic_media_play));
                        One_Solve_Listening_Activity.this.seekBar.setProgress(0);
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
        ref.child("듣기").child(arr[count] + "번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sol = "";
                sol = String.valueOf(dataSnapshot.child("해설").getValue());
                Intent i = new Intent(One_Solve_Listening_Activity.this, Solution_Popup_Activity.class);
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
                    ref1.child("해설등록 요청").child("유형").child("듣기").child(arr[count] + "번").push().setValue(sol);
            }
        }
    }

    public void getData(){

        for(int i = 0; i < 10; i++) {
            ref.child("듣기").child(arr[i] + "번").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String s = "";
                    s = dataSnapshot.child("정답").getValue().toString();
                    answer[index] = Integer.parseInt(s);
                    url[index] = dataSnapshot.child("url").getValue().toString();
                    mp3[index] = dataSnapshot.child("mp3").getValue().toString();
                    index++;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void viewMessage(){
        Toast.makeText(this, "press the answer number", Toast.LENGTH_LONG).show();
    }

    public void restart(){
        arr = shuffle();
        count = 0;
        String n = Integer.toString(arr[count]);
        ref.child("듣기").child(n+"번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = "";
                url = dataSnapshot.child("url").getValue().toString();

                Glide.with(One_Solve_Listening_Activity.this).load(url)
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showNext(){
        setButton(0);
        count++;
        if(count >= 10){
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(One_Solve_Listening_Activity.this);
            alert_confirm.setMessage("RETRY?").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            index = 0;
                            restart();
                            getData();
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
            Glide.with(One_Solve_Listening_Activity.this).load(url[count]).into(imageView);
            setButton(userAnswer[count]);
            if(bk[count]){
                checkAnswer();
            }
            if(answer[count] != 0)
                c = true;
        }

    }

    public void showPre(){
        if(count == 0)
            Toast.makeText(this, "The previous problem does not exist.", Toast.LENGTH_SHORT).show();
        else{
            count--;
            Glide.with(One_Solve_Listening_Activity.this).load(url[count])
                    .into(imageView);
            setButton(userAnswer[count]);
            if(bk[count])
                checkAnswer();
        }
    }

    public void checkAnswer(){
        if(count < 10){
            bk[count] = true;
            switch(answer[count]){
                case 1:
                    b1.setBackgroundColor(Color.BLUE);
                    break;
                case 2:
                    b2.setBackgroundColor(Color.BLUE);
                    break;
                case 3:
                    b3.setBackgroundColor(Color.BLUE);
                    break;
                case 4:
                    b4.setBackgroundColor(Color.BLUE);
            }
        }
    }

    public int[] shuffle(){

        int[] arr = new int[50];    //숫자는 데이터(듣기문제) 갯수만큼

        for(int i = 0; i < arr.length; i++){
            arr[i] = i + 1;
        }

        for(int x=0;x<arr.length;x++){
            int i = (int)(Math.random()*arr.length);
            int j = (int)(Math.random()*arr.length);

            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        int[] ret = new int[10];
        for(int i = 0; i < 10; i++)
            ret[i] = arr[i];
        return ret;
    }

    public void setButton(int n){

        b1.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b2.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b3.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b4.setBackgroundColor(Color.parseColor("#FFEEE8AA"));

        switch(n){
            case 1:
                b1.setBackgroundColor(Color.RED);
                break;
            case 2:
                b2.setBackgroundColor(Color.RED);
                break;
            case 3:
                b3.setBackgroundColor(Color.RED);
                break;
            case 4:
                b4.setBackgroundColor(Color.RED);
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

    public void onBackPressed() {
        if(mMediaplayer != null && mMediaplayer.isPlaying()) {
            mMediaplayer.stop();
            mMediaplayer = null;
        }
        finish();
        super.onBackPressed();

    }
}