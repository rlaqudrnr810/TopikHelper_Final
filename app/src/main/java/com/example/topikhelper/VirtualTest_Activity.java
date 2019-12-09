package com.example.topikhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class VirtualTest_Activity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    class Node{
        Node(int key, String url){
            this.key = key;
            this.url = url;
        }
        int key;
        String url;

    }

    private long backKeyPressedTime = 0;

    Intent intent;
    String num;
    String dbname;
    String answer = "";
    StringBuilder userAnswer = new StringBuilder("0");
    String[] imgurl = new String[100];
    String[] musicurl = new String[50];
    List<Node> url;
    List<Node> mp3;
    List<Node> an;

    boolean buttoncheck = false;

    private PhotoView image;

    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button pre_btn;
    private Button next_btn;
    boolean round2;
    private int count = 1;
    private boolean sol = false;
    boolean[] mCheck = new boolean[50];
    /***************************************************************/


    /***********************00:00 카운트 다운************************/
    //듣기, 쓰기는 110분
    private static final long START_TIME_IN_MILLIS = 4200000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    /***************************************************************/

    private MediaPlayer mMediaplayer;
    /***************************************************************/

    DatabaseReference ref;
    //액티비티가 종료될 때 이 곳을 실행한다. 화면 넘어가면 음악 끄는 기능.

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtualtest);

        intent = getIntent();
        num = intent.getStringExtra("num");         // n회
        dbname = intent.getStringExtra("dbname");   // 모의고사1 or 모의고사2
        ref = FirebaseDatabase.getInstance().getReference(dbname);


        url = new ArrayList<>();
        mp3 = new ArrayList<>();
        an = new ArrayList<>();
        image = findViewById(R.id.img);


        b1=findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol)
                    sol = true;
                setButton(1);
                userAnswer.setCharAt(count - 1, '1');
            }
        });

        b2=findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol)
                    sol = true;
                setButton(2);
                userAnswer.setCharAt(count - 1, '2');
            }
        });

        b3=findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol)
                    sol = true;
                setButton(3);
                userAnswer.setCharAt(count - 1, '3');
            }
        });

        b4=findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sol)
                    sol = true;
                setButton(4);
                userAnswer.setCharAt(count - 1, '4');
            }
        });

        pre_btn=findViewById(R.id.pre);
        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPre();
            }
        });

        next_btn=findViewById(R.id.next);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sol || (userAnswer.length() >= count && userAnswer.charAt(count - 1) != '0')) {
                    sol = false;
                    showNext();
                }
                else
                    viewMessage1();
            }
        });

        /***********************00:00 카운트 다운************************/
        mTextViewCountDown = findViewById(R.id.countdown);

        mButtonStartPause = findViewById(R.id.button_start);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count >= 50) {
                    if (mTimerRunning) {
                        pauseTimer();
                    } else {
                        startTimer();
                    }
                }
            }
        });
        updateCountDownText();

        showFirst();
        getData();

    }

    public void showFirst(){
        ref.child(num).child("1번").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s = "";
                s = dataSnapshot.child("url").getValue().toString();
                String str = "";
                str = String.valueOf(dataSnapshot.child("mp3").getValue());
                musicurl[0] = str;
                Glide.with(VirtualTest_Activity.this).load(s).into(image);
                try {
                    playAudio();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void playAudio() throws Exception{
        if(mMediaplayer == null && count <= 50 && !mCheck[count - 1]) {
            mMediaplayer = new MediaPlayer();
            mMediaplayer.setDataSource(musicurl[count - 1]);
            mMediaplayer.prepare();
            mMediaplayer.start();
            mCheck[count - 1] = true;
        }
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

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
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
                break;
        }
    }

    public void viewMessage1(){
        Toast.makeText(this, "Solve the question", Toast.LENGTH_LONG).show();
    }

    public void showNext(){
        if(count >= 100){
            // 문제풀기 끝
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(VirtualTest_Activity.this);
            alert_confirm.setMessage("The test is over. Check your score.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(mMediaplayer != null)
                                mMediaplayer.stop();
                            mMediaplayer = null;
                            gotoresult();
                        }
                    }
            );
            alert_confirm.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert_confirm.show();
        }
        else if(count <= 49){
            //듣기
            if(mMediaplayer != null && mMediaplayer.isPlaying()){      //음악 재생중
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(VirtualTest_Activity.this);
                alert_confirm.setMessage("The music is on playing. Are you sure?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(mMediaplayer != null)
                                    mMediaplayer.stop();
                                mMediaplayer = null;
                                count++;
                                if(!mCheck[count - 1]){
                                    try {
                                        playAudio();
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                setButton(0);
                                if(userAnswer.length() < count){
                                    buttoncheck = false;
                                    userAnswer.append(0);
                                }
                                else{
                                    int x = userAnswer.charAt(count - 1) - '0';
                                    switch(x){
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
                                }
                                Glide.with(VirtualTest_Activity.this).load(imgurl[count - 1])
                                        .into(image);

                            }
                        }
                );
                alert_confirm.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert_confirm.show();
            }
            else{
                if(mMediaplayer != null)
                    mMediaplayer.stop();
                mMediaplayer = null;
                count++;
                if(!mCheck[count - 1]){
                    try {
                        playAudio();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                setButton(0);
                if(userAnswer.length() < count){
                    buttoncheck = false;
                    userAnswer.append(0);
                }
                else{
                    int x = userAnswer.charAt(count - 1) - '0';
                    switch(x){
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
                }
                Glide.with(VirtualTest_Activity.this).load(imgurl[count - 1])
                        .into(image);
            }
        }
        else if(count == 50){   //듣기 끝
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(VirtualTest_Activity.this);
            alert_confirm.setMessage("This is the last question of the listening test.\n" +
                    "Do you want to take the reading test?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(mMediaplayer != null)
                                mMediaplayer.stop();
                            mMediaplayer = null;
                            round2 = true;
                            count++;
                            setButton(0);
                            if(userAnswer.length() < count){
                                buttoncheck = false;
                                userAnswer.append(0);
                            }
                            else{
                                int x = userAnswer.charAt(count - 1) - '0';
                                switch(x){
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
                            }
                            Glide.with(VirtualTest_Activity.this).load(imgurl[count - 1])
                                    .into(image);
                            startTimer();
                        }
                    }
            );
            alert_confirm.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert_confirm.show();
        }
        else{   //읽기
            count++;
            setButton(0);
            if(userAnswer.length() < count){
                buttoncheck = false;
                userAnswer.append(0);
            }
            else{
                int x = userAnswer.charAt(count - 1) - '0';
                switch(x){
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
            }
            Glide.with(VirtualTest_Activity.this).load(imgurl[count - 1])
                    .into(image);
        }
    }

    public void gotoresult(){
        int x = 0;
        for (int i = 0; i < userAnswer.length(); i++) {
            if (answer.charAt(i) != userAnswer.charAt(i)) {
                x++;
            }
        }
        int[] a = new int[100];
        int[] b = new int[100];
        int[] my = new int[x];
        int[] op = new int[x];
        int[] q = new int[x];
        int index = 0;
        for (int i = 0; i < userAnswer.length(); i++) {
            a[i] = userAnswer.charAt(i) - '0';
            b[i] = answer.charAt(i) - '0';
            if (answer.charAt(i) != userAnswer.charAt(i)) {
                my[index] = userAnswer.charAt(i) - '0';
                op[index] = answer.charAt(i) - '0';
                q[index] = i + 1;
                index++;
            }
        }

        Intent intent = new Intent(this, VirtualTest_Result_Activity.class);
        intent.putExtra("num", num);
        intent.putExtra("dbname", dbname);
        intent.putExtra("my", my);
        intent.putExtra("op", op);
        intent.putExtra("q", q);
        intent.putExtra("url", imgurl);
        intent.putExtra("mp3", musicurl);
        intent.putExtra("allUserAnswer", a);
        intent.putExtra("allAnswer", b);
        startActivity(intent);
        finish();

    }

    public void showPre(){
        if(count == 1){
            Toast.makeText(this, "The previous problem does not exist.", Toast.LENGTH_LONG).show();
        }
        else if(count == 51){
            Toast.makeText(this, "The previous problem does not exist.", Toast.LENGTH_LONG).show();
        }
        else{
            if(count < 50 && mMediaplayer != null && mMediaplayer.isPlaying()){
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(VirtualTest_Activity.this);
                alert_confirm.setMessage("The music is on playing. Are you sure?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(mMediaplayer != null)
                                    mMediaplayer.stop();
                                mMediaplayer = null;
                                count--;
                                sol = true;

                                Glide.with(VirtualTest_Activity.this).load(imgurl[count - 1])
                                        .into(image);

                                int x = userAnswer.charAt(count - 1) - '0';

                                setButton(x);
                            }
                        }
                );
                alert_confirm.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert_confirm.show();
            }
            else {
                if(mMediaplayer != null)
                    mMediaplayer.stop();
                mMediaplayer = null;
                count--;
                sol = true;

                Glide.with(VirtualTest_Activity.this).load(imgurl[count - 1])
                        .into(image);

                int x = userAnswer.charAt(count - 1) - '0';

                setButton(x);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getData(){

        ref.child(num).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Log.d("태그", ds.getKey());
                    String u = "";
                    String m = "";
                    String a = "";
                    u = ds.child("url").getValue().toString();
                    m = String.valueOf(ds.child("mp3").getValue());
                    a = ds.child("정답").getValue().toString();
                    String s = ds.getKey();
                    s = s.substring(0, s.length() - 1);
                    int x = Integer.parseInt(s);
                    url.add(new Node(x, u));
                    if(m != null)
                        mp3.add(new Node(x, m));
                    an.add(new Node(x, a));
                }
                sorting();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    Comparator<Node> comp = new Comparator<Node>(){
        public int compare(Node a, Node b){
            return a.key - b.key;
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sorting(){
        url.sort(comp);
        mp3.sort(comp);
        an.sort(comp);

        for(int i = 0; i < 100; i++){
            imgurl[i] = url.get(i).url;
            answer += an.get(i).url;
            if(i < 50) {
                musicurl[i] = mp3.get(i).url;
            }
        }
    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                //mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonStartPause.setBackground(ContextCompat.getDrawable(this, R.drawable.timer_off));
        //mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("start");
        mButtonStartPause.setBackground(ContextCompat.getDrawable(this, R.drawable.timer));
        //mButtonReset.setVisibility( View.VISIBLE);
    }

    private void updateCountDownText(){
        int minutes = (int)mTimeLeftInMillis / 1000 / 60;
        int seconds = (int)mTimeLeftInMillis / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }


    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "Press the back button again to exit.\n" +
                    "It is not saved.",Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            if(mMediaplayer != null)
                mMediaplayer.stop();
            mMediaplayer = null;
            finish();
        }
    }

}