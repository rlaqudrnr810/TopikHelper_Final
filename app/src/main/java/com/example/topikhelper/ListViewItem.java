package com.example.topikhelper;


import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String textStr;

    public void setIcon(Drawable icon) { iconDrawable = icon ;   }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setText(String text){textStr=text;}


    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getText() {return this.textStr;}
}