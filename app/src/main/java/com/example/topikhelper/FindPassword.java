package com.example.topikhelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPassword extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "FindPassword";

    private EditText editTextUserEmail;
    private Button buttonFind;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String emailAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        editTextUserEmail = (EditText) findViewById(R.id.editTextUserEmail);
        buttonFind = (Button) findViewById(R.id.buttonFind);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonFind.setOnClickListener(this);

    }
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, Login.class));
    }
    @Override
    public void onClick(View view) {

        emailAddress = editTextUserEmail.getText().toString().trim();
        if(view == buttonFind && !TextUtils.isEmpty(emailAddress)){
            progressDialog.setMessage("Please, Wait....");
            progressDialog.show();

            //비밀번호 재설정 이메일 보내기
            firebaseAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(FindPassword.this, "A Password change mail has been sent.\n Please confirm an email\n", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            } else {
                                Toast toast = Toast.makeText(FindPassword.this, "         FAILED\n This user is not registered..", Toast.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) toast.getView();
                                TextView messageTextView = (TextView) group.getChildAt(0);
                                messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                                toast.show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        else if(view == buttonFind && TextUtils.isEmpty(emailAddress)){
            Context context = getApplicationContext();
            String message = "Enter your E-mail";
            int d = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, d);
            toast.show();

        }
    }
}
