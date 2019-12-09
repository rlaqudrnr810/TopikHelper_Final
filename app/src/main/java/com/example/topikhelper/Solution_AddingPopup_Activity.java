package com.example.topikhelper;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Solution_AddingPopup_Activity extends AppCompatActivity {
    EditText txtText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_solution_adding_popup);

        //UI 객체생성
        txtText = findViewById(R.id.txtText);

    }
    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Solution_AddingPopup_Activity.this);
        alert_confirm.setMessage("Do you want to register solution?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        String sol = "";
                        sol = txtText.getText().toString().trim();
                        if(sol != null && sol.isEmpty()){
                            Toast.makeText(Solution_AddingPopup_Activity.this, "Enter Solution", Toast.LENGTH_LONG).show();
                        }
                        else {
                            intent.putExtra("sol", sol);
                            setResult(RESULT_OK, intent);

                            //액티비티(팝업) 닫기
                            finish();
                        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
