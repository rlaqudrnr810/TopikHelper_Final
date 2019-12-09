package com.example.topikhelper;

public class DailyVocaItem {

    private long frequency;
    private String meaning;
    private String name;
    private String day;
    private String pronun;

    public DailyVocaItem(){}

    public DailyVocaItem(long frequency, String meaning, String name){
        this.frequency = frequency;
        this.meaning = meaning;
        this.name = name;
    }

    public String getFrequency() { return String.valueOf(frequency); }
    public String getMeaning() { return meaning; }
    public String getPronun(){return pronun;}
    public String getName() { return name; }
    public void setDay(String day){this.day = day;}
    public String getDay(){ return day; }

}
