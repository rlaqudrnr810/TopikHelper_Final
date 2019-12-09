package com.example.topikhelper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Mypage_Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Mypage_Activity";

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    private TextView nickname;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button remove_btn;
    private Button editpassword;
    private Button testhistory;
    private Button customer;


    Intent intent;


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("사용자");
    String email = "";
    String history = "";
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        //extras = getIntent().getExtras();
        history = intent.getStringExtra("history");
        name = intent.getStringExtra("nickname");
        email = intent.getStringExtra("email");


        nickname = (TextView) findViewById(R.id.textviewUserNickname);
        nickname.setText(name);

        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        remove_btn = (Button) findViewById(R.id.textviewDelete);
        editpassword = (Button) findViewById(R.id.editpassword);
        testhistory = (Button) findViewById(R.id.testhistory);
        customer = (Button) findViewById(R.id.customer);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        //FirebaseUser user = firebaseAuth.getCurrentUser();

        //email = user.getEmail();

        //logout button event
        buttonLogout.setOnClickListener(this);
        remove_btn.setOnClickListener(this);
        editpassword.setOnClickListener(this);
        testhistory.setOnClickListener(this);
        customer.setOnClickListener(this);

        //getUserData();

        textViewUserEmail.setText("("+ email + ")");

    }

    public void getUserData(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if( ds.child("email").getValue().toString().equals(email)) {
                        //Log.d("빠끄", ds.child("nickname").getValue().toString());
                        nickname.setText(ds.child("nickname").getValue().toString());
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

    public void showVersion(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Version").setMessage("1.0.0");

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
    public void bookmark(){
        Intent intent = new Intent();
    }
    public void deleteUser(){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mypage_Activity.this);
        alert_confirm.setMessage("Do you really want to delete your account?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Query applesQuery = ref.orderByChild("email").equalTo(email);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Mypage_Activity.this, "The account has been deleted.", Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                    }
                                });
                    }
                }
        );
        alert_confirm.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Mypage_Activity.this, "cancel", Toast.LENGTH_LONG).show();
            }
        });
        alert_confirm.show();
    }

    @Override
    public void onClick(View view) {
        if(view == editpassword){
            sendMail();
        }
        if(view == testhistory){
            Intent intent = new Intent(Mypage_Activity.this, History_Activity.class);
            intent.putExtra("history", history);
            startActivity(intent);
        }
        if(view == customer){
            Intent intent = new Intent(Mypage_Activity.this, Customer_Service_Activity.class);
            startActivity(intent);
        }
        if (view == buttonLogout) {
            firebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        //회원탈퇴를 클릭하면 회원정보를 삭제한다.
        if(view == remove_btn) {
            deleteUser();
        }
    }

    public void sendMail(){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mypage_Activity.this);
        alert_confirm.setMessage("Would you like to send a password change email?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Mypage_Activity.this, "A Password change mail has been sent.\n Please confirm an email\n", Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                        } else {
                                            Toast toast = Toast.makeText(Mypage_Activity.this, "         FAILED\n This user is not registered..", Toast.LENGTH_LONG);
                                            ViewGroup group = (ViewGroup) toast.getView();
                                            TextView messageTextView = (TextView) group.getChildAt(0);
                                            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                                            toast.show();
                                        }
                                        progressDialog.dismiss();
                                    }
                                });
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
}
