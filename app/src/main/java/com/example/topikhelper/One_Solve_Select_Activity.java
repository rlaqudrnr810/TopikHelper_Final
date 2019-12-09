package com.example.topikhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class One_Solve_Select_Activity extends AppCompatActivity {
    private Button problem_random; // 랜덤별 풀기 버튼 클릭 이벤트
    private Button problem_type;  // 유형별 풀기 버튼 클릭 이벤트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_solve_select);


        problem_random = findViewById(R.id.problem_random);
        problem_type = findViewById(R.id.problem_type);

        problem_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(One_Solve_Select_Activity.this, Random_Solve_Activity.class);
                startActivity(intent); //액티비티 이동
            }
        });

        problem_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(One_Solve_Select_Activity.this, Type_Solve_Activity.class);
                startActivity(intent); //액티비티 이동
            }
        });

    }

}