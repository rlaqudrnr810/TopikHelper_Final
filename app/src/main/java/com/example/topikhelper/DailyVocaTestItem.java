package com.example.topikhelper;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

public class DailyVocaTestItem {

    private String questions;
    private String answers;
    public void setQuestions(String questions){this.questions=questions;}
    public void setAnswers(String answers){this.answers = answers;}
    public String getQuestions(){return questions;}
    public String getAnswers(){return answers;}
    private String name;
    private String meaning;
    private TextWatcher mTextWatcher;
    private Button button;

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public TextWatcher getmTextWatcher() {
        return mTextWatcher;
    }

    public DailyVocaTestItem(){
        //EditText 변경 리스너 생성
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //변경된 값을 저장한다
            }

            @Override
            public void afterTextChanged(Editable s) {
                //equals하면 바로 변화 볼 수 있음.
                if (!getMeaning().toLowerCase().equals(s.toString().toLowerCase().trim())) {
                    Button button = getButton();
                    button.setVisibility(View.VISIBLE);
                    //button.setBackgroundColor(Color.RED);
                    button.setBackgroundResource(R.drawable.check_red);
                }
                else {
                    button.setBackgroundColor(Color.BLUE);
                    button.setBackgroundResource(R.drawable.check_blue);
                }
            }
        };


    }

    public DailyVocaTestItem(String meaning){
        this.meaning = meaning;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }
    public String getName() {
        return name;
    }


}

