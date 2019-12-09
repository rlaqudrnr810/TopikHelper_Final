package com.example.topikhelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    //public static int TIME_OUT = 1001;
    private long backKeyPressedTime = 0;

    private Toast toast;
    //define view objects

    private String email = "";
    private String password = "";

    EditText editTextEmail;
    EditText editTextPassword;

    Button buttonSignin;

    TextView textviewSingUp;
    TextView textviewFindPassword;

    ProgressDialog progressDialog;

    //define firebase object
    FirebaseAuth firebaseAuth;
    FirebaseUser mFirebaseUser;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
/*
        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 menu 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), Menu_Activity.class)); //추가해 줄 ProfileActivity
        }
*/
        //initializing views
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        editTextEmail.setText(pref.getString("email", ""));
        editTextPassword.setText(pref.getString("pw",""));

        textviewSingUp= (TextView) findViewById(R.id.textViewSignUp);
        //textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        textviewFindPassword = (TextView) findViewById(R.id.textViewFindpassword);
        buttonSignin = (Button) findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);

        //button click event
        buttonSignin.setOnClickListener(this);
        textviewSingUp.setOnClickListener(this);
        textviewFindPassword.setOnClickListener(this);

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
        toast = Toast.makeText(this, "Press the \'back button\' again to exit.\n",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void userLogin(){

        if (mFirebaseUser != null)
            FirebaseAuth.getInstance().signOut();

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        //이메일 빈칸인 경우 알림
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter the ID.\n", Toast.LENGTH_SHORT).show();
            return;
        }

        //패스워드 빈칸인 경우 알림
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter the Password.\n", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            mFirebaseUser = firebaseAuth.getCurrentUser();
                            pref = getSharedPreferences("pref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", email);
                            editor.putString("pw",password);
                            editor.commit();
                            if(mFirebaseUser != null){
                                if(!(mFirebaseUser.isEmailVerified())){
                                    Toast.makeText(Login.this, "Please verify your E-mail", Toast.LENGTH_LONG).show();
                                    return;
                                }else{
                                    editTextEmail.setText(null);
                                    editTextPassword.setText(null);
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), Menu_Activity.class);
                                    startActivity(intent);
                                    Toast.makeText(Login.this, "The login was successful", Toast.LENGTH_LONG).show();
                                }


                            }

                        } else { //로그인 실패
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    @Override
    public void onClick(View view) {
        if(view == buttonSignin) {
            userLogin();
        }
        if(view == textviewSingUp) {
            startActivity(new Intent(this, Signup_Activity.class));
            finish();
        }
        if(view == textviewFindPassword) {
            startActivity(new Intent(this, FindPassword.class));
            finish();
        }
    }
}
