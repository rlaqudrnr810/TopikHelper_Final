package com.example.topikhelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Dictionary_Select_Activity extends AppCompatActivity {
    private Button button_searchvoca; // URL

    private ListView dailyvocalist;
    private DailyVocaAdapter adapter;
    private LinearLayout dictionaryLinear3;
    boolean layout1 = false;
    boolean layout2 = false;
    boolean layout3 = false;
    private ImageView url;

    EditText textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_select);
        dailyvocalist = findViewById(R.id.dailyvocalist);
        //dictionaryLinear = findViewById(R.id.dictionaryLinear);
        dictionaryLinear3 = findViewById(R.id.dictionaryLinear3);
        //final LinearLayout daylist = findViewById(R.id.daylist);

        Button daily_voca = findViewById(R.id.daily_voca);


        url = (ImageView)findViewById(R.id.url);
        //web = (WebView)findViewById(R.id.web);
        textview = (EditText)findViewById(R.id.dictionarytext);


        adapter = new DailyVocaAdapter(getApplicationContext());


        //이 부분은 나중에 디비에서 긁어온 자료로 다시 생성하세요
        dailyvocalist.setAdapter(adapter);

        for(int i = 1; i <= 30; i++) {
            adapter.addItem(i + "일");
        }

        //new getDayLIst().execute(1);

        dailyvocalist.setVisibility(View.GONE);
        daily_voca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!layout1) {
                    dailyvocalist.setVisibility(View.VISIBLE);
                    dictionaryLinear3.setVisibility(View.GONE);
                    layout2 = false;
                }
                else{
                    dailyvocalist.setVisibility(View.GONE);
                }
                layout1 = !layout1;


                dailyvocalist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int position, long id) {
                        ListViewItem item = (ListViewItem) adapterView.getItemAtPosition(position) ;
                        //view.setBackgroundColor(Color.CYAN);


                        Intent dva = new Intent(getApplicationContext(), Daily_Voca_Activity.class);
                        dva.putExtra("key1", item.getTitle());
                        startActivity(dva);
                        finish();
                    }
                });

            }
        });


        button_searchvoca = findViewById(R.id.search_voca);
        button_searchvoca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!layout3) {
                    dailyvocalist.setVisibility(View.GONE);
                    dictionaryLinear3.setVisibility(View.VISIBLE);
                    layout1=false;
                }
                else{
                    dictionaryLinear3.setVisibility(View.GONE);
                }
                layout3 = !layout3;

                //hw3();

                url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://endic.naver.com/search.nhn?sLn=kr&isOnlyViewEE=N&query="+textview.getText().toString()));
                        startActivity(intent);
                    }
                });
            }
        });

    }


}