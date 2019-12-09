package com.example.topikhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<DailyVocaItem> mData;
    private Context context; //어뎁터에서는 this같은 context를 받아오는 기능이 없어 상위 클래스에서 넘겨받아야함
    private int curPos = -1; //현재 위치를 -1로해야 아이템에 지장이 없지

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerAdapter(Context context, ArrayList<DailyVocaItem> list) {
        this.context = context;
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        @SuppressLint("ResourceType") View view = inflater.inflate(R.layout.daily_voca_list, parent, false);
        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {
        //아까 넘겨받은 context로 sharedference를 선언
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = pref.edit();

        final DailyVocaItem item = mData.get(position);

        holder.title1.setText(item.getName());
        holder.title2.setText("["+item.getPronun()+"]");
        holder.title3.setText(item.getMeaning());

        //item의 getName을 키값으로 불러옴 값이 있다면 해당값을 가져오고 없으면 false(오른쪽 인자가 디폴트)
        boolean chk = pref.getBoolean(item.getName(), false);

        //이건 설명안해도 되고 조건문 따위 훗
        if(chk){
            holder.chkbox.setChecked(true);
        }
        else{
            holder.chkbox.setChecked(false);
        }
        //이건 유저가 맘대로 체크박스를 사용하지 못하게 사용불가능으로 만듬
        holder.chkbox.setEnabled(false);

        //리사이클러 뷰는 아이템을 누를때 누르는 색상이 안변해서 쓰레드를 사용해 딜레이를 걸어 색상을 잠깐 바꿔줌
        holder.cl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                curPos=position;
                if(curPos==position){
                    holder.cl.setBackgroundColor(Color.parseColor("#FFD3D3D3"));

                    //클릭이벤트중 기존 아이템의 체크가 false인경우에만
                    if(!pref.getBoolean(item.getName(), false)) {
                        //해당 날짜의 이름을(ex: 1일, 2일) 키값으로 1씩 더해 정수를 저장 -> 해당 키값에 계속 정수를 덮어 30을 만들생각 존나 기발함
                        if(pref.getInt(item.getDay(), 0) >= 30)
                            editor.putInt(item.getDay(), 30);
                        else
                            editor.putInt(item.getDay(), pref.getInt(item.getDay(), 0) + 1);
                        editor.putBoolean(item.getName(), true);
                        editor.commit();
                        holder.chkbox.setChecked(true);
                    }
                    else{
                        if(pref.getInt(item.getDay(), 0) > 0)
                            editor.putInt(item.getDay(), pref.getInt(item.getDay(), 0) - 1);
                        else
                            editor.putInt(item.getDay(), 0);
                        editor.putBoolean(item.getName(), false);
                        editor.commit();
                        holder.chkbox.setChecked(false);
                    }
                    //이떄 누른 단어의 값을 sharedpreference로 저장 키값은 단어이름
                    //editor.putBoolean(item.getName(), true);

                    Log.d(item.getDay()+"click: ", String.valueOf(pref.getInt(item.getDay(),0)));

                    //editor.commit();
                    //holder.chkbox.setChecked(true);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            holder.cl.setBackgroundColor(Color.parseColor("#FFF8F8FF"));    //흰색
                            holder.chkbox.setEnabled(false);
                        }
                    }, 300);
                }
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title1;
        TextView title2;
        TextView title3;
        CheckBox chkbox;
        ConstraintLayout cl;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        DailyVocaItem item = mData.get(pos);
                    }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            title1 = itemView.findViewById(R.id.title1);
            title2 = itemView.findViewById(R.id.title2);
            title3 = itemView.findViewById(R.id.title3);
            chkbox = itemView.findViewById(R.id.chkbox);
            cl = itemView.findViewById(R.id.layoutColor);
        }
    }

}