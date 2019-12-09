package com.example.topikhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DailyVocaTestAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<DailyVocaTestItem> dailyVocaTestItem = new ArrayList<DailyVocaTestItem>();
    private Context context; //어뎁터에서는 this같은 context를 받아오는 기능이 없어 상위 클래스에서 넘겨받아야함

    EditText answers;
    Button VocaTestButtoN;


    // ListViewAdapter의 생성자
    public DailyVocaTestAdapter() {

    }

    public DailyVocaTestAdapter(Context context, ArrayList<DailyVocaTestItem> list) {
        this.context = context;
        dailyVocaTestItem = list;
    }



    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return dailyVocaTestItem.size() ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_vocatest, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        //TextView VocaTestNumber = (TextView)convertView.findViewById(R.id.VocaTestNumber);
        TextView VocaTestTextView = (TextView) convertView.findViewById(R.id.VocaTestTextview) ;
        answers =(EditText)convertView.findViewById(R.id.VocaTestEdittext);
        // EditText VocaTestEditText = (EditText) convertView.findViewById(R.id.VocaTestEdittext) ;
        VocaTestButtoN = (Button) convertView.findViewById(R.id.VocaTestButton) ;
        final String realAnswers=answers.getText().toString().trim();

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final DailyVocaTestItem listViewItem = dailyVocaTestItem.get(position);

        listViewItem.setButton(VocaTestButtoN);

        answers.addTextChangedListener(listViewItem.getmTextWatcher());


        // 아이템 내 각 위젯에 데이터 반영
        VocaTestTextView.setText(listViewItem.getName());
        //String meaning = listViewItem.getMeaning();

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return dailyVocaTestItem.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String questions, String answers, String desc) {
        DailyVocaTestItem item = new DailyVocaTestItem();

        item.setQuestions(questions);
        item.setAnswers(answers);

        dailyVocaTestItem.add(item);
    }

    public void addItem2(String questions){
        DailyVocaTestItem item = new DailyVocaTestItem();

        item.setQuestions(questions);

        dailyVocaTestItem.add(item);
    }
}
