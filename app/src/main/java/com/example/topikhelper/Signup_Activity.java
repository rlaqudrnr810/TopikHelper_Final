package com.example.topikhelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Signup_Activity extends AppCompatActivity implements View.OnClickListener{
    //private MainBackPressCloseHandler mainBackPressCloseHandler;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordCheck;
    EditText editTextNickname;
    Button buttonSignup;
    Button buttonBack;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;
    RadioGroup rg;

    private String email = "";
    private String password = "";
    private String nickname = "";
    private String sex = "";
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordCheck = (EditText) findViewById(R.id.editTextPasswordCheck);
        editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonBack = (Button) findViewById(R.id.already);

        progressDialog = new ProgressDialog(this);

        buttonSignup.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        rg = (RadioGroup)findViewById(R.id.radioGroup1);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("사용자");
        UserId = mFirebaseDatabase.push().getKey();

    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, Login.class)); //추가해 줄 로그인 액티비티

    }

    //Firebse creating a new user
    private void registerUser(){
        //사용자가 입력하는 email, password를 가져온다.
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        String passwordCheck = editTextPasswordCheck.getText().toString().trim();
        nickname = editTextNickname.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || TextUtils.isEmpty(password) || !password.equals(passwordCheck)) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please check the ID\n", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter a Password.\n", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(passwordCheck)) {
                Toast.makeText(this, "Please Check the password\n", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else {
            //email과 password가 제대로 입력되어 있다면 계속 진행
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            

            firebaseAuth.createUserWithEmailAndPassword(email, password)        //회원 등록
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null)
                                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {    //회원가입 성공
                                            int id = rg.getCheckedRadioButtonId();                  //id는 성별
                                            RadioButton rb = (RadioButton) findViewById(id);
                                            sex = rb.getText().toString();
                                            User user = new User(email, nickname, sex, "");
                                            mFirebaseDatabase.child(UserId).setValue(user);

                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                        } else {                    //회원가입 실패
                                            Toast.makeText(Signup_Activity.this, "Sign-Up ERROR!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        progressDialog.dismiss();

                                    }
                                });
                        }
                    });
        }

    }

    //button click event
    @Override
    public void onClick(View view) {
        if(view == buttonSignup) {
            //TODO
            registerUser();
        }

        if(view == buttonBack) {
            //TODO
            finish();
            startActivity(new Intent(this, Login.class)); //추가해 줄 로그인 액티비티
        }
    }
}
