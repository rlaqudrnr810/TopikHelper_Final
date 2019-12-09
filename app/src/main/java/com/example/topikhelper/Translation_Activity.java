package com.example.topikhelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translation_Activity extends AppCompatActivity {

    private Button button_dailyvoca; // 단어장
    private Button translation; //번역기
    private Button button_searchvoca; // URL

    private ListView dailyvocalist;
    private DailyVocaAdapter adapter;
    private LinearLayout dictionaryLinear;
    private LinearLayout dictionaryLinear3;
    boolean layout1 = false;
    boolean layout2 = false;
    boolean layout3 = false;
    private EditText translationText;
    private ImageView translationButton;
    private TextView resultText;
    private String result;
    private EditText dictionarytext;
    private ImageView url;

    WebSettings websettings;
    WebView web;
    EditText textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);




        translationText = (EditText)findViewById(R.id.translationText);
        translationButton = (ImageView) findViewById(R.id.translationButton);
        resultText = (TextView)findViewById(R.id.resultText);

        translationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Handler mHandler = new Handler();

                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        StringBuilder output = new StringBuilder();
                        String clientId = "kTMEKqvk5S6f4aUrqZ9K";
                        String clientSecret = "VK9IBsU4ji";
                        try{
                            @SuppressLint("WrongThread") String text = URLEncoder.encode(translationText.getText().toString(), "UTF-8");
                            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";

                            URL url = new URL(apiURL);
                            HttpURLConnection con = (HttpURLConnection)url.openConnection();
                            con.setRequestMethod("POST");
                            con.setRequestProperty("X-Naver-Client-Id", clientId);
                            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                            String postParams = "source=ko&target=en&text="+text;
                            con.setDoOutput(true);
                            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                            wr.writeBytes(postParams);
                            wr.flush();
                            wr.close();

                            int responseCode = con.getResponseCode();
                            BufferedReader br;
                            if(responseCode == 200){
                                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            }else{
                                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                            }

                            String inputLine;
                            while((inputLine = br.readLine()) != null){
                                output.append(inputLine);
                            }
                            br.close();
                        }catch(Exception ex){
                            Log.e("SampleHTTP","Exception in processing response.", ex);
                            ex.printStackTrace();
                        }
                        result = output.toString();
                        mHandler.post(new Runnable(){
                            @Override
                            public void run(){
                                JsonParser parser = new JsonParser();
                                JsonElement element = parser.parse(result);
                                if(element.getAsJsonObject().get("errorMessage")!=null){
                                    Log.e("번역 오류", "번역 오류가 발생하였습니다. "
                                            + "[오류 코드 : " + element.getAsJsonObject().get("errorCode").getAsString() + "]");
                                }else if(element.getAsJsonObject().get("message")!=null){
                                    resultText.setText(element.getAsJsonObject().get("message").getAsJsonObject().get("result")
                                            .getAsJsonObject().get("translatedText").getAsString());
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

}
