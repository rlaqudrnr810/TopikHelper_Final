package com.example.topikhelper;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class Random_Solve_Activity extends AppCompatActivity implements Runnable {
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button check;
    private Button next;
    private Button play;
    private Button pause;
    private Button stop;

    private TextView txt;
    private SeekBar seekBar;
    boolean wasPlaying = true;
    String t = "00:00";

    private MediaPlayer mMediaplayer;
    private PhotoView imageView;
    int playbackPosition = 0;
    boolean sol[] = new boolean[10];
    boolean[] ff = new boolean[4];
    boolean canAddingSol = false;
    int last = -1;
    int count = 0;
    int[] arr = shuffle();
    int[] num = randomNum();
    int[] userAnswer = new int[10];
    String mp3 = "";
    String n = "";
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("모의고사2");
    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_solve);

        imageView = findViewById(R.id.img);
        b1 = (Button) findViewById(R.id.one_random_b1);
        b2 = (Button) findViewById(R.id.one_random_b2);
        b3 = (Button) findViewById(R.id.one_random_b3);
        b4 = (Button) findViewById(R.id.one_random_b4);
        check = (Button) findViewById(R.id.one_random_check);
        next = (Button) findViewById(R.id.one_random_next);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        stop = (Button) findViewById(R.id.stop);

        seekBar = findViewById(R.id.seekbar);
        txt = findViewById(R.id.time);

        final TextView seekBarHint = findViewById(R.id.time);
        b1.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b2.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b3.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
        b4.setBackgroundColor(Color.parseColor("#FFEEE8AA"));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol[count]){
                    if(last != -1)
                        ff[last] = false;
                    last = 0;
                    ff[last] = true;
                    userAnswer[count] = 1;
                    b1.setBackgroundColor(Color.RED);
                    b2.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b3.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b4.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol[count]){
                    if(last != -1)
                        ff[last] = false;
                    last = 1;
                    ff[last] = true;
                    userAnswer[count] = 2;
                    b1.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b2.setBackgroundColor(Color.RED);
                    b3.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b4.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol[count]){
                    if(last != -1)
                        ff[last] = false;
                    last = 2;
                    ff[last] = true;
                    userAnswer[count] = 3;
                    b1.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b2.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b3.setBackgroundColor(Color.RED);
                    b4.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                }
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol[count]){
                    if(last != -1)
                        ff[last] = false;
                    last = 3;
                    ff[last] = true;
                    userAnswer[count] = 4;
                    b1.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b2.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b3.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
                    b4.setBackgroundColor(Color.RED);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wasPlaying = false;
                showNext();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol[count]){
                    if(last != -1){
                        sol[count] = true;
                        last = -1;
                        checkAnswer();
                    }
                    else{
                        message();
                    }
                }
            }
        });

        for(int i = 0; i < 10; i++){
            userAnswer[i] = -1;
        }

        String h = Integer.toString(num[count]);
        n = Integer.toString(arr[count]);
        viewButton(arr[count]);
        //Toast.makeText(Random_Solve_Activity.this, h + "회 - " + n + "번", Toast.LENGTH_LONG).show();
        ref.child(h+"회").child(n+"번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = "";
                url = dataSnapshot.child("url").getValue().toString();
                if(Integer.parseInt(n) <= 50) {
                    mp3 = String.valueOf(dataSnapshot.child("mp3").getValue());
                }
                Glide.with(Random_Solve_Activity.this).load(url)
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                        Random_Solve_Activity.this.seekBar.setProgress(0);
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

    public void message(){
        Toast.makeText(this, "press the answer number", Toast.LENGTH_LONG).show();
    }

    public void restart(){
        arr = shuffle();
        num = randomNum();
        count = 0;
        viewButton(arr[0]);
        for(int i = 0; i < 10; i++){
            userAnswer[i] = -1;
            sol[i] = false;
        }
        String h = Integer.toString(num[count]);
        n = Integer.toString(arr[count]);
        ref.child(h+"회").child(n+"번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = "";
                url = dataSnapshot.child("url").getValue().toString();
                if(Integer.parseInt(n) <= 50) {
                    mp3 = String.valueOf(dataSnapshot.child("mp3").getValue());
                }
                Glide.with(Random_Solve_Activity.this).load(url)
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showNext(){
        count++;
        if(count >= 10){
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Random_Solve_Activity.this);
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
            //Toast.makeText(Random_Solve_Activity.this, )
            if(mMediaplayer != null && mMediaplayer.isPlaying()){
                mMediaplayer.stop();
            }
            seekBar.setProgress(0);
            mMediaplayer = null;
            canAddingSol = false;
            String h = Integer.toString(num[count]);
            n = Integer.toString(arr[count]);
            viewButton(arr[count]);
            //Toast.makeText(Random_Solve_Activity.this, h + "회 - " + n + "번", Toast.LENGTH_LONG).show();
            ref.child(h+"회").child(n+"번").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String url = "";
                    url = dataSnapshot.child("url").getValue().toString();
                    if(Integer.parseInt(n) <= 50) {
                        mp3 = String.valueOf(dataSnapshot.child("mp3").getValue());
                    }
                    Glide.with(Random_Solve_Activity.this).load(url)
                            .into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            b1.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
            b2.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
            b3.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
            b4.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
            switch(userAnswer[count]){
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
                    break;
            }
            if(sol[count]){
                checkAnswer();
            }
        }

    }

    public void showPre(View v){
        if(count == 0){
            Toast.makeText(Random_Solve_Activity.this, "The previous problem does not exist.", Toast.LENGTH_LONG).show();
        }
        else{
            if(mMediaplayer != null && mMediaplayer.isPlaying()){
            mMediaplayer.stop();
            }
            seekBar.setProgress(0);
            mMediaplayer = null;
            count--;
            wasPlaying = false;
            canAddingSol = false;
            String h = Integer.toString(num[count]);
            n = Integer.toString(arr[count]);
            viewButton(arr[count]);
            //Toast.makeText(Random_Solve_Activity.this, h + "회 - " + n + "번", Toast.LENGTH_LONG).show();
            ref.child(h+"회").child(n+"번").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String url = "";
                    url = dataSnapshot.child("url").getValue().toString();
                    if(Integer.parseInt(n) <= 50) {
                        mp3 = String.valueOf(dataSnapshot.child("mp3").getValue());
                    }
                    Glide.with(Random_Solve_Activity.this).load(url)
                            .into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            b1.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
            b2.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
            b3.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
            b4.setBackgroundColor(Color.parseColor("#FFEEE8AA"));
            switch(userAnswer[count]){
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
                    break;
            }
            if(sol[count]){
                checkAnswer();
            }
        }
    }

    public void checkAnswer(){
        if(count >= 10){
            Toast.makeText(this, "The end.", Toast.LENGTH_LONG).show();
        }
        else{
            String h = Integer.toString(num[count]);
            String n = Integer.toString(arr[count]);
            ref.child(h+"회").child(n+"번").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String answer = dataSnapshot.child("정답").getValue().toString();
                    int a = Integer.parseInt(answer);
                    if(a == userAnswer[count]){
                        canAddingSol = true;
                    }
                    switch(a - 1) {
                        case 0:
                            b1.setBackgroundColor(Color.BLUE);
                            break;
                        case 1:
                            b2.setBackgroundColor(Color.BLUE);
                            break;
                        case 2:
                            b3.setBackgroundColor(Color.BLUE);
                            break;
                        case 3:
                            b4.setBackgroundColor(Color.BLUE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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

    public void addingSol(View v){
        if(sol[count] && canAddingSol) {
            Intent i = new Intent(Random_Solve_Activity.this, Solution_AddingPopup_Activity.class);
            startActivityForResult(i, 2);
        }
        else{
            Toast.makeText(Random_Solve_Activity.this, "Only the correct answerer can register the solution.", Toast.LENGTH_LONG).show();
        }
    }

    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        ref.child(num[count] + "회").child(arr[count] + "번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sol = "";
                sol = String.valueOf(dataSnapshot.child("해설").getValue());
                Intent i = new Intent(Random_Solve_Activity.this, Solution_Popup_Activity.class);
                i.putExtra("sol", sol);
                startActivityForResult(i, 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void playMusic(View v){
        if(arr[count] <= 50){
            try {
                if(mMediaplayer == null || (mMediaplayer != null && !mMediaplayer.isPlaying()))
                    playAudio();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void pauseMusic(View v){
        if(arr[count] <= 50){
            if(mMediaplayer !=null){
                //현재 재생위치 저장
                playbackPosition = mMediaplayer.getCurrentPosition();
                mMediaplayer.pause();
                wasPlaying = false;
            }
        }
        else
            Toast.makeText(Random_Solve_Activity.this, "This is not a listening problem.", Toast.LENGTH_SHORT).show();
    }

    public void stopMusic(View v){
        if(arr[count] <= 50){
            if(mMediaplayer != null){
                playbackPosition = 0;
                mMediaplayer.stop();
                mMediaplayer = null;
            }
        }
        else
            Toast.makeText(Random_Solve_Activity.this, "This is not a listening problem.", Toast.LENGTH_SHORT).show();
    }

    private void playAudio() throws Exception{
        if(mMediaplayer == null) {
            mMediaplayer = new MediaPlayer();

            mMediaplayer.setDataSource(mp3);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String sol = data.getStringExtra("sol");
                if(!sol.equals(""))
                    ref1.child("해설등록 요청").child(num[count] + "회").child(arr[count] + "번").push().setValue(sol);
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

    public int[] shuffle(){

        int[] arr = new int[100];

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

    public int[] randomNum(){
        int[] ret = new int[10];
        for(int i = 0; i < 10; i++) {
            ret[i] = (int) (Math.random() * 100);
            ret[i] %= 3;
            ret[i]++;
        }
        return ret;
    }
}