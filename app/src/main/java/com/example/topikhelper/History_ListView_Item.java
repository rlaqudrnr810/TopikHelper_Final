package com.example.topikhelper;

public class History_ListView_Item {
    private String num ;
    private String titleStr ;
    private String descStr ;

    public void setNum(String num1) {
        num = num1 ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public String getNum() {
        return this.num ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}